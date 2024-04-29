package dev.realtimetransactionservice.controller;

import dev.realtimetransactionservice.model.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        Error error = new Error(errorMessage, "400");
        return ResponseEntity.badRequest().body(error);
    }
}