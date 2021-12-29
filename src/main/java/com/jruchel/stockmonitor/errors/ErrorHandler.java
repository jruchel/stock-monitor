package com.jruchel.stockmonitor.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<ErrorResponse> resourceNotFound(NullPointerException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .value("Not found")
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND)
                .timestamp(new Date().toString())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
