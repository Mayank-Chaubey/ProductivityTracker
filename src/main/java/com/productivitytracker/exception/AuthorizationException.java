package com.productivitytracker.exception;

/**
 * Thrown when an authenticated user is not allowed to access a resource.
 */
public class AuthorizationException extends AppException {

    public AuthorizationException(String message) {
        super(message);
    }
}
