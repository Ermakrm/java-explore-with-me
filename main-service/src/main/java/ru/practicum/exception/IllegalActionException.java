package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalActionException extends RuntimeException {
    public IllegalActionException(String message) {
        super(message);
        log.warn(message);
    }
}
