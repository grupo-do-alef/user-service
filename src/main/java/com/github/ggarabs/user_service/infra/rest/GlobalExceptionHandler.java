package com.github.ggarabs.user_service.infra.rest;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.ggarabs.user_service.application.exception.InvalidCredentialsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(RuntimeException ex) {
        ErrorResponse err = new ErrorResponse(
                LocalDateTime.now(),
                "E-mail ou senha inválidos");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            String message) {
    }
}
