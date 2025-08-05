package ru.kon.dotaanalytics.service;

import java.util.List;
import ru.kon.dotaanalytics.dto.request.HeroRequest;
import ru.kon.dotaanalytics.dto.response.HeroResponse;
import ru.kon.dotaanalytics.exception.HeroAlreadyExistsException;
import ru.kon.dotaanalytics.exception.ResourceNotFoundException;

public interface HeroService {
    /**
     * Получает список всех героев, существующих в системе.
     *
     * @return Список DTO с информацией о каждом герое.
     */
    List<HeroResponse> getAllHeroes();

    /**
     * Находит героя по его уникальному идентификатору (ID).
     *
     * @param heroId Уникальный идентификатор героя.
     * @return DTO с информацией о найденном герое.
     * @throws ResourceNotFoundException если герой с таким ID не найден.
     */
    HeroResponse getHeroById(Long heroId);

    /**
     * Создает нового героя на основе предоставленных данных.
     *
     * @param heroRequest DTO с данными для создания нового героя (имя, основной атрибут).
     * @return DTO с информацией о созданном герое, включая его новый ID.
     * @throws HeroAlreadyExistsException если герой с таким именем уже существует.
     */
    HeroResponse createHero(HeroRequest heroRequest);

    /**
     * Обновляет данные существующего героя по его ID.
     *
     * @param heroId      Уникальный идентификатор обновляемого героя.
     * @param heroRequest DTO с новыми данными для героя.
     * @return DTO с обновленной информацией о герое.
     * @throws ResourceNotFoundException  если герой с таким ID не найден.
     * @throws HeroAlreadyExistsException если новое имя уже занято другим героем.
     */
    HeroResponse updateHero(Long heroId, HeroRequest heroRequest);

    /**
     * Удаляет героя из системы по его ID.
     *
     * @param heroId Уникальный идентификатор удаляемого героя.
     * @throws ResourceNotFoundException если герой с таким ID не найден.
     */
    void deleteHero(Long heroId);
}
