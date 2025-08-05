package ru.kon.dotaanalytics.dto.request;

import java.io.Serializable;
import ru.kon.dotaanalytics.dto.PlayerAnalyticsDto;

public record ReportRequest(
    Long reportId,
    String userEmail,
    PlayerAnalyticsDto analyticsData
) implements Serializable {
}
