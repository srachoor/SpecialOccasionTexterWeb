package com.spoctexter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException e
    ) {

        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadInputException.class)
    public ResponseEntity<Object> handleBadInputException(
            BadInputException e
    ) {

        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NamingConflictException.class)
    public ResponseEntity<Object> handleNamingConflictException(
            NamingConflictException e
    ) {

        ApiException apiException = new ApiException(
                e.getMessage(),
                e,
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.CONFLICT);
    }


}
