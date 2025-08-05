package ru.kon.dotaanalytics.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
    int statusCode,
    String message,
    LocalDateTime timestamp
) {
}
