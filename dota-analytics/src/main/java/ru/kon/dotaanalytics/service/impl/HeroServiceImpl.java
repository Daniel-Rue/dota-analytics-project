package ru.kon.dotaanalytics.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kon.dotaanalytics.dto.request.HeroRequest;
import ru.kon.dotaanalytics.dto.response.HeroResponse;
import ru.kon.dotaanalytics.entity.Hero;
import ru.kon.dotaanalytics.exception.HeroAlreadyExistsException;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;
import ru.kon.dotaanalytics.repository.HeroRepository;
import ru.kon.dotaanalytics.service.HeroService;
import ru.kon.dotaanalytics.util.ErrorMessages;

@Service
@AllArgsConstructor
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HeroResponse> getAllHeroes() {
        return heroRepository.findAll().stream()
            .map(this::mapToHeroResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HeroResponse getHeroById(Long heroId) {
        Hero hero = findHeroById(heroId);
        return mapToHeroResponse(hero);
    }

    @Override
    @Transactional
    public HeroResponse createHero(HeroRequest heroRequest) {
        if (heroRepository.existsByName(heroRequest.name())) {
            throw new HeroAlreadyExistsException(
                String.format(ErrorMessages.HERO_ALREADY_EXISTS, heroRequest.name()));
        }

        Hero hero = new Hero();
        hero.setName(heroRequest.name());
        hero.setPrimaryAttribute(heroRequest.primaryAttribute());

        Hero savedHero = heroRepository.save(hero);
        return mapToHeroResponse(savedHero);
    }

    @Override
    @Transactional
    public HeroResponse updateHero(Long heroId, HeroRequest heroRequest) {
        Hero heroToUpdate = findHeroById(heroId);

        heroRepository.findByName(heroRequest.name()).ifPresent(existingHero -> {
            if (!existingHero.getId().equals(heroId)) {
                throw new HeroAlreadyExistsException(
                    String.format(ErrorMessages.HERO_ALREADY_EXISTS, heroRequest.name()));
            }
        });

        heroToUpdate.setName(heroRequest.name());
        heroToUpdate.setPrimaryAttribute(heroRequest.primaryAttribute());

        Hero updatedHero = heroRepository.save(heroToUpdate);
        return mapToHeroResponse(updatedHero);
    }

    @Override
    @Transactional
    public void deleteHero(Long heroId) {
        if (!heroRepository.existsById(heroId)) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.HERO_NOT_FOUND_BY_ID, heroId));
        }
        heroRepository.deleteById(heroId);
    }

    private HeroResponse mapToHeroResponse(Hero hero) {
        return new HeroResponse(hero.getId(), hero.getName(), hero.getPrimaryAttribute());
    }

    private Hero findHeroById(Long heroId) {
        return heroRepository.findById(heroId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessages.HERO_NOT_FOUND_BY_ID, heroId)));
    }
}
