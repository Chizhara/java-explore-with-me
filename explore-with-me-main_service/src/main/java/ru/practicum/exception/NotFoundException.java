package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Long idValue) {
        super(String.format("%s with id=%d was not found", entityName, idValue));
    }

    public NotFoundException(String entityName, Long... params) {
        super(String.format("%s  was not found by parameters: %s", entityName, String.join(", ", Arrays.toString(params))));
    }

}
