package com.rivim.webclient.exceptions;

public class EmailNotUniqueException extends RuntimeException {

  public EmailNotUniqueException() {
  }

  public EmailNotUniqueException(String message) {
    super(message);
  }

  public EmailNotUniqueException(String message, Throwable cause) {
    super(message, cause);
  }
}
