package ru.kon.dotaanalytics.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.dto.request.MatchCreateRequest;
import ru.kon.dotaanalytics.dto.request.MatchPlayerStatsRequest;
import ru.kon.dotaanalytics.dto.response.MatchDetailsResponse;
import ru.kon.dotaanalytics.dto.response.PlayerStatsResponse;
import ru.kon.dotaanalytics.entity.Hero;
import ru.kon.dotaanalytics.entity.Match;
import ru.kon.dotaanalytics.entity.MatchPlayerStats;
import ru.kon.dotaanalytics.entity.User;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;
import ru.kon.dotaanalytics.exception.StatsAlreadySubmittedException;
import ru.kon.dotaanalytics.repository.HeroRepository;
import ru.kon.dotaanalytics.repository.MatchPlayerStatsRepository;
import ru.kon.dotaanalytics.repository.MatchRepository;
import ru.kon.dotaanalytics.repository.UserRepository;
import ru.kon.dotaanalytics.security.service.UserDetailsImpl;
import ru.kon.dotaanalytics.service.MatchService;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@Slf4j
@AllArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchPlayerStatsRepository matchPlayerStatsRepository;
    private final UserRepository userRepository;
    private final HeroRepository heroRepository;

    @Override
    @Transactional
    public Long createMatch(MatchCreateRequest request) {
        Match match = new Match();
        match.setStartTime(LocalDateTime.now());
        match.setDurationInSeconds(request.durationInSeconds());
        match.setWinner(request.winner());

        Match savedMatch = matchRepository.save(match);
        log.info("Создан новый матч с ID: {}", savedMatch.getId());
        return savedMatch.getId();
    }

    @Override
    @Transactional
    public void addPlayerStats(Long matchId, MatchPlayerStatsRequest request, UserDetailsImpl currentUserDetails) {
        Match match = findMatchById(matchId);

        User user = userRepository.findById(currentUserDetails.getId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, currentUserDetails.getId())));

        if (matchPlayerStatsRepository.existsByMatchAndUser(match, user)) {
            throw new StatsAlreadySubmittedException(String.format(
                ErrorMessages.STATS_ALREADY_SUBMITTED, user.getNickname(), matchId));
        }

        Hero hero = heroRepository.findById(request.heroId())
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.HERO_NOT_FOUND_BY_ID, request.heroId())));

        MatchPlayerStats stats = new MatchPlayerStats();
        stats.setMatch(match);
        stats.setUser(user);
        stats.setHero(hero);
        stats.setKills(request.kills());
        stats.setDeaths(request.deaths());
        stats.setAssists(request.assists());

        matchPlayerStatsRepository.save(stats);
        log.info("Пользователь '{}' добавил статистику к матчу ID {}", user.getNickname(), matchId);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchDetailsResponse getMatchDetails(Long matchId) {
        Match match = findMatchById(matchId);

        List<MatchPlayerStats> statsList = matchPlayerStatsRepository.findAllByMatchWithDetails(match);

        List<PlayerStatsResponse> playerStatsResponses = statsList.stream()
            .map(this::mapToPlayerStatsResponse)
            .collect(Collectors.toList());

        return new MatchDetailsResponse(
            match.getId(),
            match.getStartTime(),
            match.getDurationInSeconds(),
            match.getWinner(),
            playerStatsResponses
        );
    }

    private Match findMatchById(Long matchId) {
        return matchRepository.findById(matchId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.MATCH_NOT_FOUND_BY_ID, matchId)));
    }

    private PlayerStatsResponse mapToPlayerStatsResponse(MatchPlayerStats stats) {
        return new PlayerStatsResponse(
            stats.getUser().getNickname(),
            stats.getHero().getName(),
            stats.getKills(),
            stats.getDeaths(),
            stats.getAssists()
        );
    }
}
