package com.spoctexter.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Objects;

public class ApiException {

    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime zonedDateTime;

    ApiException(String message,
                 Throwable throwable,
                 HttpStatus httpStatus,
                 ZonedDateTime zonedDateTime) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }

    public String message() {
        return message;
    }

    public Throwable throwable() {
        return throwable;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public ZonedDateTime zonedDateTime() {
        return zonedDateTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ApiException) obj;
        return Objects.equals(this.message, that.message) &&
                Objects.equals(this.throwable, that.throwable) &&
                Objects.equals(this.httpStatus, that.httpStatus) &&
                Objects.equals(this.zonedDateTime, that.zonedDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, throwable, httpStatus, zonedDateTime);
    }

    @Override
    public String toString() {
        return "ApiException[" +
                "message=" + message + ", " +
                "throwable=" + throwable + ", " +
                "httpStatus=" + httpStatus + ", " +
                "zonedDateTime=" + zonedDateTime + ']';
    }

}
