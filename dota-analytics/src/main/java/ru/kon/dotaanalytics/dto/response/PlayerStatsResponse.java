package ru.kon.dotaanalytics.dto.response;

public record PlayerStatsResponse(
    String playerNickname,
    String heroName,
    int kills,
    int deaths,
    int assists
) {
}
