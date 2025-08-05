package ru.kon.dotaanalytics.dto.response;

import java.io.Serializable;
import ru.kon.dotaanalytics.entity.enums.ReportStatus;

public record ReportResponse(
    Long reportId,
    ReportStatus status,
    String failureReason
) implements Serializable {
}
