package ru.practicum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class ErrorResponse {

    @Getter
    private final String error;

}
