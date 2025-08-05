package ru.kon.dotaanalytics.dto.request;

public record MatchPlayerStatsRequest(
    Long heroId,
    int kills,
    int deaths,
    int assists
) {
}
