package ru.kon.dotaanalytics.dto.response;

import java.util.List;

public record JwtResponse(
    String token,
    Long id,
    String nickname,
    String email,
    List<String> roles
) {
}
