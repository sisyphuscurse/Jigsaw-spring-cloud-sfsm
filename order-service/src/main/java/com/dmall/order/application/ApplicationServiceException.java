package com.dmall.order.application;

public class ApplicationServiceException extends RuntimeException{
  public ApplicationServiceException() {
  }

  public ApplicationServiceException(String message) {
    super(message);
  }

  public ApplicationServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApplicationServiceException(Throwable cause) {
    super(cause);
  }

  public ApplicationServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
