package ru.kon.dotaanalytics.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kon.dotaanalytics.entity.Match;
import ru.kon.dotaanalytics.entity.MatchPlayerStats;
import ru.kon.dotaanalytics.entity.User;

@Repository
public interface MatchPlayerStatsRepository extends JpaRepository<MatchPlayerStats, Long> {

    boolean existsByMatchAndUser(Match match, User user);

    @Query("SELECT mps FROM MatchPlayerStats mps JOIN FETCH mps.user JOIN FETCH mps.hero WHERE mps.match = :match")
    List<MatchPlayerStats> findAllByMatchWithDetails(Match match);

    List<MatchPlayerStats> findAllByUser(User user);

    @Query("SELECT mps FROM MatchPlayerStats mps JOIN FETCH mps.hero JOIN FETCH mps.match " +
        "WHERE mps.user = :user ORDER BY mps.match.startTime DESC LIMIT 5")
    List<MatchPlayerStats> findLast5MatchesForUser(User user);
}
