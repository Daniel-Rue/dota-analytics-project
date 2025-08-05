package ru.kon.dotaanalytics.dto;

import java.io.Serializable;

public record RecentMatchDto(
    String heroName,
    int kills,
    int deaths,
    int assists,
    String result
) implements Serializable {
}
