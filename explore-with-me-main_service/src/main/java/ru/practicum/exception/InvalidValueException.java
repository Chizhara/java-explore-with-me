package ru.practicum.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String fieldName, Object fieldValue) {
        super(String.format("Field %s, Error: must contain a date that not yet occurred. Value: %s ",
                fieldName, fieldValue.toString()));
    }
}
