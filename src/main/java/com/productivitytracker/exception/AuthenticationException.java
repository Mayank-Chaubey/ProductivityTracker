package com.productivitytracker.exception;

/**
 * Thrown when a user cannot be authenticated.
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(String message) {
        super(message);
    }
}
