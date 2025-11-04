package com.backend.order_services.utils.exceptions.exps;

import com.backend.order_services.utils.exceptions.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UnauthorizedOrderUpdateException extends CustomRuntimeException {
  public UnauthorizedOrderUpdateException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
