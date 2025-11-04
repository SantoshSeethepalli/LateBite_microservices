package com.backend.order_services.utils.exceptions.exps;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
