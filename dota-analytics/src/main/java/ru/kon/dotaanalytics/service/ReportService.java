package ru.kon.dotaanalytics.service;

public interface ReportService {

    /**
     * Инициирует асинхронную генерацию отчета.
     * Создает запись в БД со статусом PENDING и отправляет задачу в RabbitMQ.
     *
     * @param userId ID пользователя, для которого запрашивается отчет.
     */
    void requestReportGeneration(Long userId);

}
