package com.backend.cart_service.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException{

    private final HttpStatus statusCode;

    public CustomRuntimeException(String message, HttpStatus statusCode) {

        super(message);
        this.statusCode = statusCode;
    }
}
