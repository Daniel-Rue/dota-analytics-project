package ru.kon.dotaanalytics.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kon.dotaanalytics.dto.request.HeroRequest;
import ru.kon.dotaanalytics.dto.response.HeroResponse;
import ru.kon.dotaanalytics.service.HeroService;

@RestController
@RequestMapping("/api/heroes")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<List<HeroResponse>> getAllHeroes() {
        List<HeroResponse> heroes = heroService.getAllHeroes();
        return ResponseEntity.ok(heroes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeroResponse> getHeroById(@PathVariable Long id) {
        HeroResponse hero = heroService.getHeroById(id);
        return ResponseEntity.ok(hero);
    }

    @PostMapping
    public ResponseEntity<HeroResponse> createHero(@RequestBody HeroRequest heroRequest) {
        HeroResponse createdHero = heroService.createHero(heroRequest);
        return new ResponseEntity<>(createdHero, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeroResponse> updateHero(@PathVariable Long id, @RequestBody HeroRequest heroRequest) {
        HeroResponse updatedHero = heroService.updateHero(id, heroRequest);
        return ResponseEntity.ok(updatedHero);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHero(@PathVariable Long id) {
        heroService.deleteHero(id);
        return ResponseEntity.noContent().build();
    }
}
