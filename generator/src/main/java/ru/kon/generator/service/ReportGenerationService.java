package ru.kon.generator.service;

import ru.kon.generator.dto.ReportRequest;

public interface ReportGenerationService {
    void generateAndEmailReport(ReportRequest request);
}
