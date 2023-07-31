package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalParametersException extends RuntimeException {
    public IllegalParametersException(String message) {
        super(message);
        log.warn(message);
    }
}
