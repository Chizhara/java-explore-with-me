package ru.practicum.exception;

public class ForbiddenAccessException extends InvalidActionException {
    public ForbiddenAccessException(long userId, long eventId) {
        super(String.format("User with id = %d have not access to event with id = %d", userId, eventId));
    }
}
