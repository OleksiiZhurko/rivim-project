package com.rivim.webclient.exceptions;

public class UsernameNotUniqueException extends RuntimeException {

  public UsernameNotUniqueException() {
  }

  public UsernameNotUniqueException(String message) {
    super(message);
  }

  public UsernameNotUniqueException(String message, Throwable cause) {
    super(message, cause);
  }
}
