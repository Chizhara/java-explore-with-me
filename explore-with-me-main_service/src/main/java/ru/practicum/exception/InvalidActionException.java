package ru.practicum.exception;

public class InvalidActionException extends RuntimeException {
    public InvalidActionException(String msg) {
        super(msg);
    }
}
