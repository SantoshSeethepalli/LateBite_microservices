package com.backend.order_services.utils.exceptions;

import org.springframework.http.HttpStatus;

public class CustomRuntimeException extends RuntimeException{

    private final HttpStatus statusCode;

    public CustomRuntimeException(String message, HttpStatus statusCode) {

        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
