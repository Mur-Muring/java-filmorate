package ru.yandex.practicum.filmorate.exceptions;

public class InternalServerException extends RuntimeException {

    public InternalServerException(final String message) {
        super(message);
    }
}
