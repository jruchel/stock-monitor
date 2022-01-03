package com.jruchel.stockmonitor.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<ErrorResponse> resourceNotFound(NullPointerException exception) {
        return getErrorResponse(exception.getMessage(), "Not found", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<ErrorResponse> httpException(ResponseStatusException exception) {
        return getErrorResponse(exception.getMessage(), exception.getMessage(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException exception) {
        return getErrorResponse(exception.getMessage(), "Validation error", HttpStatus.CONFLICT);
    }

    private ResponseEntity<ErrorResponse> getErrorResponse(String message, String value, HttpStatus status) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .value(value)
                .message(message)
                .httpStatus(status)
                .timestamp(new Date().toString())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

}
