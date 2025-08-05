package ru.kon.dotaanalytics.dto.request;

import ru.kon.dotaanalytics.entity.enums.Team;

public record MatchCreateRequest(
    int durationInSeconds,
    Team winner
) {
}
