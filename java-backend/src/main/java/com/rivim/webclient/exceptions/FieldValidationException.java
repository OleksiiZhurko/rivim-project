package com.rivim.webclient.exceptions;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rivim.webclient.exceptions.ErrorMessage.RESPONSE_ERROR;
import static com.rivim.webclient.exceptions.ErrorMessage.RESPONSE_REASON;

public class FieldValidationException extends RuntimeException {

  public Set<ConstraintViolation<?>> validate;

  public FieldValidationException() {
  }

  public FieldValidationException(Set<ConstraintViolation<?>> validate) {
    this.validate = validate;
  }

  public FieldValidationException(String message, Set<ConstraintViolation<?>> validate) {
    super(message);
    this.validate = validate;
  }

  public FieldValidationException(String message, Throwable cause, Set<ConstraintViolation<?>> validate) {
    super(message, cause);
    this.validate = validate;
  }
}
