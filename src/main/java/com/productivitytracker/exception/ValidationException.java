package com.productivitytracker.exception;

/**
 * Thrown when input fails business validation.
 */
public class ValidationException extends AppException {

    public ValidationException(String message) {
        super(message);
    }
}
