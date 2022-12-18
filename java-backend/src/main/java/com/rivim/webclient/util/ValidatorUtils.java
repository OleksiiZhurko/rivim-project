package com.rivim.webclient.util;

import com.rivim.webclient.exceptions.FieldValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Set;

@Component
public class ValidatorUtils {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public void checkForBindingError(Object user) {
    Set<ConstraintViolation<Object>> errors = validator.validate(user);

    if (!errors.isEmpty()) {
      throw new FieldValidationException(Collections.unmodifiableSet(errors));
    }
  }
}
