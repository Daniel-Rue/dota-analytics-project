package ru.kon.generator.dto;

import java.io.Serializable;
import ru.kon.generator.enums.ReportStatus;

public record ReportResponse(
    Long reportId,
    ReportStatus status,
    String failureReason
) implements Serializable {
}
