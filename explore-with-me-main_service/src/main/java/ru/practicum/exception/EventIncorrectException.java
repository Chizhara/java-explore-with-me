package ru.practicum.exception;

public class EventIncorrectException extends RuntimeException {
    public EventIncorrectException(String msg, Object fieldValue) {
        super(String.format("Cannot %s the event because it's not in the right state: %s ", msg, fieldValue));
    }
}
