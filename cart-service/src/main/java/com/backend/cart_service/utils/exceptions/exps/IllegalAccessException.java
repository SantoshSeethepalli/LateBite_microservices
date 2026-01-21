package com.backend.cart_service.utils.exceptions.exps;

import com.backend.cart_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class IllegalAccessException extends CustomRuntimeException {
    public IllegalAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
