package ru.kon.dotaanalytics.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import ru.kon.dotaanalytics.entity.enums.Team;

public record MatchDetailsResponse(
    Long id,
    LocalDateTime startTime,
    int durationInSeconds,
    Team winner,
    List<PlayerStatsResponse> playersStats
) {
}
