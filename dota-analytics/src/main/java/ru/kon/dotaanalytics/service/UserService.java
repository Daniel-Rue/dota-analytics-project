package ru.kon.dotaanalytics.service;

import ru.kon.dotaanalytics.dto.request.LoginRequest;
import ru.kon.dotaanalytics.dto.request.RegisterRequest;
import ru.kon.dotaanalytics.dto.response.JwtResponse;

public interface UserService {
    /**
     * Регистрация нового пользователя.
     *
     * @param registerRequest DTO с данными для регистрации.
     */
    void registerUser(RegisterRequest registerRequest);

    /**
     * Аутентификация пользователя и генерация JWT.
     *
     * @param loginRequest DTO с данными для входа.
     * @return JwtResponse DTO с токеном и информацией о пользователе.
     */
    JwtResponse loginUser(LoginRequest loginRequest);
}
