package ru.kon.dotaanalytics.dto.request;

public record RegisterRequest(
    String nickname,
    String email,
    String password
) {
}
