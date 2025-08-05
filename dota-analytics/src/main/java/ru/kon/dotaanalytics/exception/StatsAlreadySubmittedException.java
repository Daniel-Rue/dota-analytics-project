package ru.kon.dotaanalytics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StatsAlreadySubmittedException extends RuntimeException {
    public StatsAlreadySubmittedException(String message) {
        super(message);
    }
}
