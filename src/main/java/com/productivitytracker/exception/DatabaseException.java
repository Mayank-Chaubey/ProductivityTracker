package com.productivitytracker.exception;

/**
 * Wraps persistence-layer failures with application context.
 */
public class DatabaseException extends AppException {

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
