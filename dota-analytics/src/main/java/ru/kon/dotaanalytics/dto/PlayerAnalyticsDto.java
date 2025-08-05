package ru.kon.dotaanalytics.dto;

import java.io.Serializable;
import java.util.List;

public record PlayerAnalyticsDto(
    String nickname,
    long totalMatches,
    double winRate,
    double averageKda,
    List<RecentMatchDto> recentMatches
) implements Serializable {
}
