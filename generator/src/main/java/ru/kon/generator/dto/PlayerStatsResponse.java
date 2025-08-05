package ru.kon.generator.dto;

public record PlayerStatsResponse(
    String playerNickname,
    String heroName,
    int kills,
    int deaths,
    int assists
) {
}
