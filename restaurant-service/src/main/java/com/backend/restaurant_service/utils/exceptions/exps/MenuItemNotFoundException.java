package com.backend.restaurant_service.utils.exceptions.exps;

import com.backend.restaurant_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class MenuItemNotFoundException extends CustomRuntimeException {
    public MenuItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
