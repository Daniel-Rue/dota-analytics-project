package ru.kon.generator.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.kon.generator.config.RabbitMQConfig;
import ru.kon.generator.dto.ReportRequest;
import ru.kon.generator.service.ReportGenerationService;

@Component
@Slf4j
@AllArgsConstructor
public class ReportRequestListener {

    private final ReportGenerationService reportGenerationService;

    @RabbitListener(queues = RabbitMQConfig.REQUEST_QUEUE_NAME)
    public void processReportRequest(ReportRequest request) {
        log.info("Получена задача из очереди на генерацию отчета для пользователя с почтой: {}", request.userEmail());
        reportGenerationService.generateAndEmailReport(request);
    }
}
