package ru.kon.dotaanalytics.controller;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kon.dotaanalytics.dto.PlayerAnalyticsDto;
import ru.kon.dotaanalytics.service.PlayerAnalyticsService;
import ru.kon.dotaanalytics.service.ReportService;

@RestController
@RequestMapping("/api/players")
@AllArgsConstructor
public class PlayerController {

    private final ReportService reportService;
    private final PlayerAnalyticsService playerAnalyticsService;

    @GetMapping("/{userId}/stats")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<PlayerAnalyticsDto> getPlayerStats(@PathVariable Long userId) {
        PlayerAnalyticsDto analytics = playerAnalyticsService.getPlayerAnalytics(userId);
        return ResponseEntity.ok(analytics);
    }

    @PostMapping("/{userId}/report")
    @PreAuthorize("#userId == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> requestReport(@PathVariable Long userId) {
        reportService.requestReportGeneration(userId);
        return ResponseEntity.accepted()
            .body(Map.of("message", "Запрос принят. Готовый отчет будет отправлен на вашу электронную почту."));
    }
}
