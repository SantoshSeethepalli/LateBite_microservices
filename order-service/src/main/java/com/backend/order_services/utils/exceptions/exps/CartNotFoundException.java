package com.backend.order_services.utils.exceptions.exps;

public class CartNotFoundException extends RuntimeException{

    public CartNotFoundException(String message) {
        super(message);
    }
}
