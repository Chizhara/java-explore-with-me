package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            ValidationException.class,
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final RuntimeException e) {
        log.error("ERROR Validation 400! {}", e.getMessage());
        return handleException(HttpStatus.BAD_REQUEST, e, "Incorrectly made request.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.error("ERROR MethodArgumentTypeMismatchException 400! {}", e.getName());
        return handleException(HttpStatus.BAD_REQUEST, e, "Unknown " + e.getName() + ": " + e.getValue());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("ERROR SQLException 409! {}", e.getMessage());
        return handleException(HttpStatus.CONFLICT, e, "Integrity constraint has been violated.");
    }

    @ExceptionHandler({InvalidValueException.class, EventIncorrectException.class, InvalidActionException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidValueException(final RuntimeException e) {
        log.error("ERROR invalid data 409! {}", e.getMessage());
        return handleException(HttpStatus.CONFLICT, e, "For the requested operation the conditions are not met.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoFoundException(final NotFoundException e) {
        log.error("ERROR SQLException 404! {}", e.getMessage());
        return handleException(HttpStatus.NOT_FOUND, e, "The required object not found.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUntrackedException(final Exception e) {
        log.error("ERROR Unhandled Error 500! {}", e.getClass());
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, e, "Error: untracked error");
    }

    private ApiError handleException(HttpStatus status, Exception e, final String reason) {
        Arrays.stream(e.getStackTrace()).forEach(System.out::println);
        return new ApiError(status, reason, e.getMessage(), Arrays.toString(e.getStackTrace()), LocalDateTime.now());
    }

}
