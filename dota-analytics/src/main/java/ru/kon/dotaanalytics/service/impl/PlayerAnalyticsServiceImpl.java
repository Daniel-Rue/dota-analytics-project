package ru.kon.dotaanalytics.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.dto.PlayerAnalyticsDto;
import ru.kon.dotaanalytics.dto.RecentMatchDto;
import ru.kon.dotaanalytics.entity.MatchPlayerStats;
import ru.kon.dotaanalytics.entity.User;
import ru.kon.dotaanalytics.entity.enums.Team;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;
import ru.kon.dotaanalytics.repository.MatchPlayerStatsRepository;
import ru.kon.dotaanalytics.repository.UserRepository;
import ru.kon.dotaanalytics.service.PlayerAnalyticsService;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@AllArgsConstructor
public class PlayerAnalyticsServiceImpl implements PlayerAnalyticsService {

    private final MatchPlayerStatsRepository matchPlayerStatsRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public PlayerAnalyticsDto getPlayerAnalytics(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, userId)));

        List<MatchPlayerStats> allStats = matchPlayerStatsRepository.findAllByUser(user);

        List<RecentMatchDto> recentMatches;
        if (allStats.isEmpty()) {
            recentMatches = Collections.emptyList();
        } else {
            List<MatchPlayerStats> last5Stats = matchPlayerStatsRepository.findLast5MatchesForUser(user);
            recentMatches = last5Stats.stream()
                .map(this::mapToRecentMatchDto)
                .collect(Collectors.toList());
        }

        if (allStats.isEmpty()) {
            return new PlayerAnalyticsDto(user.getNickname(), 0, 0.0, 0.0, recentMatches);
        }

        long totalMatches = allStats.size();

        long wins = allStats.stream()
            .filter(stat -> stat.getMatch().getWinner() == Team.RADIANT)
            .count();
        double winRate = (double) wins / totalMatches * 100;

        long totalKills = allStats.stream().mapToLong(MatchPlayerStats::getKills).sum();
        long totalAssists = allStats.stream().mapToLong(MatchPlayerStats::getAssists).sum();
        long totalDeaths = allStats.stream().mapToLong(MatchPlayerStats::getDeaths).sum();

        double averageKda = (double) (totalKills + totalAssists) / Math.max(1, totalDeaths);

        return new PlayerAnalyticsDto(user.getNickname(), totalMatches, winRate, averageKda, recentMatches);
    }

    private RecentMatchDto mapToRecentMatchDto(MatchPlayerStats stat) {
        String result;
        if (stat.getMatch().getWinner() == Team.RADIANT) {
            result = "Победа";
        } else {
            result = "Поражение";
        }

        return new RecentMatchDto(
            stat.getHero().getName(),
            stat.getKills(),
            stat.getDeaths(),
            stat.getAssists(),
            result
        );
    }
}
