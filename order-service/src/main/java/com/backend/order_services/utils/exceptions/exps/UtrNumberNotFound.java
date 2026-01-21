package com.backend.order_services.utils.exceptions.exps;

import com.backend.order_services.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UtrNumberNotFound extends CustomRuntimeException {
    public UtrNumberNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
