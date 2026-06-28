package com.productivitytracker.exception;

/**
 * Thrown when a requested user-scoped resource cannot be found.
 */
public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message);
    }
}
