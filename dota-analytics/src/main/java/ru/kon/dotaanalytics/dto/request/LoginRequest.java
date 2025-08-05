package ru.kon.dotaanalytics.dto.request;

public record LoginRequest(
    String email,
    String password
) {
}
