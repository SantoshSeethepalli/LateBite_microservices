package com.backend.restaurant_service.utils.exceptions.exps;

import com.backend.restaurant_service.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class IllegalRestaurantAccessException extends CustomRuntimeException {
  public IllegalRestaurantAccessException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}
