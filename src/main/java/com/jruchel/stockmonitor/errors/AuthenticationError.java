package com.jruchel.stockmonitor.errors;

public class AuthenticationError extends RuntimeException {

    public AuthenticationError(String username, String message) {
        super("Could not authenticate user '%s': %s".formatted(username, message));
    }

    public AuthenticationError(String message) {
        super("Could not authenticate: %s".formatted(message));
    }

}
