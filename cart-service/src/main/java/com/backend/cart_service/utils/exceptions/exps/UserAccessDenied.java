package com.backend.cart_service.utils.exceptions.exps;

import com.backend.cart_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UserAccessDenied extends CustomRuntimeException {
    public UserAccessDenied(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
