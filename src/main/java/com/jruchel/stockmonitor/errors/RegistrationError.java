package com.jruchel.stockmonitor.errors;

public class RegistrationError extends RuntimeException {
    public RegistrationError(String message) {
        super(message);
    }
}
