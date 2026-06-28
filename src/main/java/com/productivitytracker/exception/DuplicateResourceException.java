package com.productivitytracker.exception;

/**
 * Thrown when a unique resource, such as an email address, already exists.
 */
public class DuplicateResourceException extends AppException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
