package ru.kon.dotaanalytics.service;

import ru.kon.dotaanalytics.dto.PlayerAnalyticsDto;

public interface PlayerAnalyticsService {
    /**
     * Рассчитывает сводную аналитику по играм для указанного пользователя.
     *
     * @param userId ID пользователя.
     * @return DTO с рассчитанными метриками (общее кол-во матчей, Win Rate, средний KDA).
     */
    PlayerAnalyticsDto getPlayerAnalytics(Long userId);
}
