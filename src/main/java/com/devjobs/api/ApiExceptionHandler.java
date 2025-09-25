package com.devjobs.api;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", "NOT_FOUND", "message", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (var err : ex.getBindingResult().getAllErrors()) {
      String field = err instanceof FieldError fe ? fe.getField() : err.getObjectName();
      errors.put(field, err.getDefaultMessage());
    }
    return ResponseEntity.badRequest().body(Map.of("error", "VALIDATION_ERROR", "fields", errors));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraint(ConstraintViolationException ex) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "CONSTRAINT_VIOLATION", "message", ex.getMessage()));
  }
}
