package ru.kon.dotaanalytics.listener;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.config.RabbitMQConfig;
import ru.kon.dotaanalytics.dto.response.ReportResponse;
import ru.kon.dotaanalytics.repository.ReportRepository;

@Component
@Slf4j
@AllArgsConstructor
public class ReportResultListener {

    private final ReportRepository reportRepository;

    @RabbitListener(queues = RabbitMQConfig.RESULT_QUEUE_NAME)
    @Transactional
    public void processReportResult(ReportResponse result) {
        log.info("Получен результат для отчета ID: {}. Статус: {}", result.reportId(), result.status());

        reportRepository.findById(result.reportId()).ifPresentOrElse(
            report -> {
                report.setStatus(result.status());
                report.setCompletedAt(LocalDateTime.now());
                reportRepository.save(report);
                log.info("Статус отчета ID {} успешно обновлен", report.getId());
            }, () -> {
                log.error("Получен результат для несуществующего отчета ID: {}", result.reportId());
            }
        );
    }
}
