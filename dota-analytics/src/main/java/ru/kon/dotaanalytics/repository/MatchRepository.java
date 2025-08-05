package ru.kon.dotaanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kon.dotaanalytics.entity.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
