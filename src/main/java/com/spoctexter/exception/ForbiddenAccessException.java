package com.spoctexter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenAccessException extends RuntimeException{
    public ForbiddenAccessException(String message) {super(message);}
}
