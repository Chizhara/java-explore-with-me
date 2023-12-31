package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String exceptionMessage;
    private final String stackTrace;
    private final LocalDateTime timestamp;
}
