package com.backend.order_services.utils.exceptions.exps;

import com.backend.order_services.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class IllegalAccessException extends CustomRuntimeException {
    public IllegalAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
