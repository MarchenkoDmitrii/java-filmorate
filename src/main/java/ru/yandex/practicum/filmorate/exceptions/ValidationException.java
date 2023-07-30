package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends IllegalArgumentException {
    public ValidationException(String cause, HttpStatus status) {
        super(cause);
    }
}
