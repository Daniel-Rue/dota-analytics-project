package ru.kon.generator.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.style.BorderStyle;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.kon.generator.config.RabbitMQConfig;
import ru.kon.generator.dto.PlayerAnalyticsDto;
import ru.kon.generator.dto.ReportRequest;
import ru.kon.generator.dto.ReportResponse;
import ru.kon.generator.enums.ReportStatus;
import ru.kon.generator.service.ReportGenerationService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportGenerationServiceImpl implements ReportGenerationService {

    private final RabbitTemplate rabbitTemplate;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    public void generateAndEmailReport(ReportRequest request) {
        try {
            log.info("Начинаю генерацию отчета для пользователя (email: {})", request.userEmail());

            PlayerAnalyticsDto stats = request.analyticsData();

            ByteArrayOutputStream reportStream = generateDocx(stats);

            sendEmail(request.userEmail(), stats.nickname(), reportStream);

            log.info("Отчет успешно сгенерирован и отправлен на почту {}", request.userEmail());

            sendResult(new ReportResponse(request.reportId(), ReportStatus.COMPLETED, null));

        } catch (Exception e) {
            log.error("Ошибка при генерации отчета для ID {}: {}", request.reportId(), e.getMessage(), e);
            sendResult(new ReportResponse(request.reportId(), ReportStatus.FAILED, e.getMessage()));
        }
    }

    private ByteArrayOutputStream generateDocx(PlayerAnalyticsDto stats) throws Exception {

        RowRenderData header = Rows.of("Герой", "K/D/A", "Результат")
            .textColor("FFFFFF")
            .bgColor("4472C4")
            .center()
            .create();

        List<RowRenderData> dataRows = stats.recentMatches().stream()
            .map(match -> {
                String kda = String.format("%d/%d/%d", match.kills(), match.deaths(), match.assists());
                return Rows.create(match.heroName(), kda, match.result());
            })
            .toList();

        List<RowRenderData> allRows = new ArrayList<>();
        allRows.add(header);
        allRows.addAll(dataRows);

        TableRenderData recentMatchesTable = Tables.of(allRows.toArray(new RowRenderData[0]))
            .border(BorderStyle.DEFAULT)
            .create();

        Map<String, Object> data = new HashMap<>();
        data.put("nickname", stats.nickname());
        data.put("generation_date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        data.put("total_matches", String.valueOf(stats.totalMatches()));
        data.put("win_rate", String.format("%.2f%%", stats.winRate()));
        data.put("avg_kda", String.format("%.2f", stats.averageKda()));

        data.put("recent_matches_table", recentMatchesTable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XWPFTemplate.compile(Objects.requireNonNull(getClass().getResourceAsStream("/template.docx")))
            .render(data)
            .write(baos);
        return baos;
    }

    private void sendEmail(String to, String nickname, ByteArrayOutputStream fileStream) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(mailFrom);
        helper.setTo(to);
        helper.setSubject("Ваш отчет по статистике Dota 2 готов!");
        helper.setText(String.format(
            "Здравствуйте, %s!\n\nВаш отчет по статистике прикреплен к этому письму.",
            nickname
        ));

        helper.addAttachment("Dota2-Report.docx", new ByteArrayResource(fileStream.toByteArray()));

        mailSender.send(message);
    }

    private void sendResult(ReportResponse result) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.RESULT_EXCHANGE_NAME,
            RabbitMQConfig.RESULT_ROUTING_KEY,
            result
        );
    }
}
