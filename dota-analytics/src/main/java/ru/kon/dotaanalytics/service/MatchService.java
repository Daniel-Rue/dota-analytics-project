package ru.kon.dotaanalytics.service;

import ru.kon.dotaanalytics.dto.request.MatchCreateRequest;
import ru.kon.dotaanalytics.dto.request.MatchPlayerStatsRequest;
import ru.kon.dotaanalytics.dto.response.MatchDetailsResponse;
import ru.kon.dotaanalytics.security.service.UserDetailsImpl;

public interface MatchService {

    /**
     * Создает (регистрирует) новый завершенный матч.
     *
     * @param request DTO с результатами матча.
     * @return ID созданного матча.
     */
    Long createMatch(MatchCreateRequest request);

    /**
     * Добавляет статистику аутентифицированного пользователя к существующему матчу.
     *
     * @param matchId     ID матча, к которому добавляется статистика.
     * @param request     DTO со статистикой игрока (герой, KDA).
     * @param currentUser Аутентифицированный пользователь, добавляющий статистику.
     */
    void addPlayerStats(Long matchId, MatchPlayerStatsRequest request, UserDetailsImpl currentUser);

    /**
     * Получает полную информацию о матче, включая статистику всех игроков.
     *
     * @param matchId ID матча.
     * @return DTO с детальной информацией о матче.
     */
    MatchDetailsResponse getMatchDetails(Long matchId);
}
