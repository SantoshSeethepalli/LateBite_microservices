package com.backend.cart_service.utils.exceptions.exps;

import com.backend.cart_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CustomRuntimeException {
    public CartNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
