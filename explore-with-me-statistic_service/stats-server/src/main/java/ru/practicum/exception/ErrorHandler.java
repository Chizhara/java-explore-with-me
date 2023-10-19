package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            ValidationException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final RuntimeException e,
                                              HttpStatus status) {
        log.error("ERROR Validation 400! {}", e.getMessage());
        return handleException(status, e, "Error: validation error");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e,
                                                              HttpStatus status) {
        log.error("ERROR MethodArgumentTypeMismatchException 400! {}", e.getName());
        return handleException(status, e, "Unknown " + e.getName() + ": " + e.getValue());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final SQLException e, HttpStatus status) {
        log.error("ERROR SQLException 409! {}", e.getMessage());
        return handleException(status, e, "Error: database error");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUntrackedException(final Exception e, HttpStatus status) {
        log.error("ERROR Unhandled Error 500! {}", e.getClass());
        return handleException(status, e, "Error: untracked error");
    }

    private static ApiError handleException(HttpStatus status, Exception e, final String message) {
        StringWriter sw = new StringWriter();
        PrintWriter pr = new PrintWriter(sw);
        e.printStackTrace(pr);
        String stackTrace = sw.toString();
        return new ApiError(status, message, e.getMessage(), stackTrace);
    }

}
