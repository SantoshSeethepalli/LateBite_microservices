package com.backend.order_services.utils.exceptions.exps;

import com.backend.order_services.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends CustomRuntimeException {
    public AccessDeniedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
