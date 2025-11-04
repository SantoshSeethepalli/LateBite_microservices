package com.backend.order_services.utils.exceptions.exps;

public class UnauthorizedOrderUpdateException extends RuntimeException {
  public UnauthorizedOrderUpdateException(String message) {
    super(message);
  }
}
