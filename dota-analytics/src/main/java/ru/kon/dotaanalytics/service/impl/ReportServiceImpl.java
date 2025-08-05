package ru.kon.dotaanalytics.service.impl;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.config.RabbitMQConfig;
import ru.kon.dotaanalytics.dto.PlayerAnalyticsDto;
import ru.kon.dotaanalytics.dto.request.ReportRequest;
import ru.kon.dotaanalytics.entity.Report;
import ru.kon.dotaanalytics.entity.User;
import ru.kon.dotaanalytics.entity.enums.ReportStatus;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;
import ru.kon.dotaanalytics.repository.ReportRepository;
import ru.kon.dotaanalytics.repository.UserRepository;
import ru.kon.dotaanalytics.service.PlayerAnalyticsService;
import ru.kon.dotaanalytics.service.ReportService;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@Slf4j
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final RabbitTemplate rabbitTemplate;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PlayerAnalyticsService playerAnalyticsService;

    @Override
    @Transactional
    public void requestReportGeneration(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(
                ErrorMessages.USER_NOT_FOUND_BY_ID,
                userId
            )));

        PlayerAnalyticsDto analyticsData = playerAnalyticsService.getPlayerAnalytics(userId);

        Report report = new Report();
        report.setUser(user);
        report.setStatus(ReportStatus.PENDING);
        report.setCreatedAt(LocalDateTime.now());
        Report savedReport = reportRepository.save(report);

        ReportRequest request = new ReportRequest(savedReport.getId(), user.getEmail(), analyticsData);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.REQUEST_EXCHANGE_NAME,
            RabbitMQConfig.REQUEST_ROUTING_KEY,
            request
        );
        log.info(
            "Отправлен запрос на генерацию отчета ID {} для пользователя {}",
            savedReport.getId(),
            user.getNickname()
        );
    }

}
