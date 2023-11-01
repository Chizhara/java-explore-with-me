package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Long idValue) {
        super(String.format("%s with id=%d was not found", entityName, idValue));
    }

}
