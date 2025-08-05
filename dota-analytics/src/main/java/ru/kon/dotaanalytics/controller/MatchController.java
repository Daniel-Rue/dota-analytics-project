package ru.kon.dotaanalytics.controller;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kon.dotaanalytics.dto.request.MatchCreateRequest;
import ru.kon.dotaanalytics.dto.request.MatchPlayerStatsRequest;
import ru.kon.dotaanalytics.dto.response.MatchDetailsResponse;
import ru.kon.dotaanalytics.security.service.UserDetailsImpl;
import ru.kon.dotaanalytics.service.MatchService;

@RestController
@RequestMapping("/api/matches")
@AllArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createMatch(@RequestBody MatchCreateRequest request) {
        Long matchId = matchService.createMatch(request);
        return new ResponseEntity<>(Map.of("id", matchId), HttpStatus.CREATED);
    }

    @PostMapping("/{matchId}/stats")
    public ResponseEntity<Void> addMyStatsToMatch(
        @PathVariable Long matchId,
        @RequestBody MatchPlayerStatsRequest request,
        @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        matchService.addPlayerStats(matchId, request, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDetailsResponse> getMatchDetails(@PathVariable Long matchId) {
        MatchDetailsResponse matchDetails = matchService.getMatchDetails(matchId);
        return ResponseEntity.ok(matchDetails);
    }
}
