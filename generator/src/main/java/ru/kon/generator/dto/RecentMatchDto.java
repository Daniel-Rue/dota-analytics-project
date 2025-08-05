package ru.kon.generator.dto;

import java.io.Serializable;

public record RecentMatchDto(
    String heroName,
    int kills,
    int deaths,
    int assists,
    String result
) implements Serializable {
}
