package com.backend.cart_service.utils.exceptions.exps;

import com.backend.cart_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class RestaurantMisMatchException extends CustomRuntimeException {
    public RestaurantMisMatchException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
