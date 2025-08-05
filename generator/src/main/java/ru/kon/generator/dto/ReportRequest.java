package ru.kon.generator.dto;

import java.io.Serializable;

public record ReportRequest(
    Long reportId,
    String userEmail,
    PlayerAnalyticsDto analyticsData
) implements Serializable {
}
