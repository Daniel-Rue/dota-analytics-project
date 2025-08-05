package ru.kon.dotaanalytics.util;

public final class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String USER_NOT_FOUND_BY_EMAIL = "Пользователь с email '%s' не найден.";
    public static final String ROLE_NOT_FOUND = "Роль '%s' не найдена в системе.";
    public static final String NICKNAME_ALREADY_EXISTS = "Пользователь с никнеймом '%s' уже существует.";
    public static final String EMAIL_ALREADY_EXISTS = "Пользователь с email '%s' уже существует.";
    public static final String UNEXPECTED_ERROR = "Произошла непредвиденная ошибка.";
    public static final String HERO_NOT_FOUND_BY_ID = "Герой с ID '%d' не найден.";
    public static final String HERO_ALREADY_EXISTS = "Герой с именем '%s' уже существует.";
    public static final String MATCH_NOT_FOUND_BY_ID = "Матч с ID '%d' не найден.";
    public static final String STATS_ALREADY_SUBMITTED =
        "Статистика для пользователя '%s' в матче с ID '%d' уже была добавлена.";
    public static final String USER_NOT_FOUND_BY_ID = "Пользователь с ID '%d' не найден.";
}
