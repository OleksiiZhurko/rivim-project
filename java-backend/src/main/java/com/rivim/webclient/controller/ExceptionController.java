package com.rivim.webclient.controller;

import com.rivim.webclient.exceptions.DaoException;
import com.rivim.webclient.exceptions.EmailNotUniqueException;
import com.rivim.webclient.exceptions.FieldValidationException;
import com.rivim.webclient.exceptions.RoleNotFoundException;
import com.rivim.webclient.exceptions.UserNotFoundException;
import com.rivim.webclient.exceptions.UsernameNotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rivim.webclient.exceptions.ErrorMessage.RESPONSE_ERROR;
import static com.rivim.webclient.exceptions.ErrorMessage.RESPONSE_REASON;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(DaoException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(DaoException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createBody(e));
  }

  @ExceptionHandler(EmailNotUniqueException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(EmailNotUniqueException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(createBody(e));
  }

  @ExceptionHandler(FieldValidationException.class)
  public ResponseEntity<Map<String, ?>> handleCustomException(FieldValidationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of(
            RESPONSE_ERROR, e.getClass().getSimpleName(),
            RESPONSE_REASON, e.validate.stream()
                .collect(Collectors.toMap(
                    ConstraintViolation::getPropertyPath,
                    ConstraintViolation::getMessage)
                )
        ));
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(RoleNotFoundException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createBody(e));
  }

  @ExceptionHandler(UsernameNotUniqueException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(UsernameNotUniqueException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(createBody(e));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCustomException(UserNotFoundException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createBody(e));
  }

  private Map<String, String> createBody(Exception e) {
    return Map.of(
        RESPONSE_ERROR, e.getClass().getSimpleName(),
        RESPONSE_REASON, e.getMessage()
    );
  }
}
