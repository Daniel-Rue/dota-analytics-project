package ru.kon.dotaanalytics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HeroAlreadyExistsException extends RuntimeException {
    public HeroAlreadyExistsException(String message) {
        super(message);
    }
}
