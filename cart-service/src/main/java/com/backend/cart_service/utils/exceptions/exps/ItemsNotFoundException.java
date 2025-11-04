package com.backend.cart_service.utils.exceptions.exps;

import com.backend.cart_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class ItemsNotFoundException extends CustomRuntimeException {
    public ItemsNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
