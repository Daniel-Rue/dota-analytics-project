package ru.kon.dotaanalytics.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kon.dotaanalytics.entity.Hero;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    boolean existsByName(String name);

    Optional<Hero> findByName(String name);
}
