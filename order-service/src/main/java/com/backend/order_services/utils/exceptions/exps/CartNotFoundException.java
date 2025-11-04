package com.backend.order_services.utils.exceptions.exps;

import com.backend.order_services.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CustomRuntimeException {

    public CartNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
