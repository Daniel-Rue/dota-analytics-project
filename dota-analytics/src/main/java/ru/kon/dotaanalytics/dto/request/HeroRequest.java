package ru.kon.dotaanalytics.dto.request;

import ru.kon.dotaanalytics.entity.enums.PrimaryAttribute;

public record HeroRequest(
    String name,
    PrimaryAttribute primaryAttribute
) {
}
