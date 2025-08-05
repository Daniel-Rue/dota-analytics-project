package ru.kon.dotaanalytics.dto.response;

import ru.kon.dotaanalytics.entity.enums.PrimaryAttribute;

public record HeroResponse(
    Long id,
    String name,
    PrimaryAttribute primaryAttribute
) {
}
