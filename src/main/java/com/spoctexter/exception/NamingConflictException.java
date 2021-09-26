package com.spoctexter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NamingConflictException extends RuntimeException {
    public NamingConflictException(String message) {super(message);}
}
