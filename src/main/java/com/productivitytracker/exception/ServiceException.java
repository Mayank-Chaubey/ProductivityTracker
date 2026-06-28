package com.productivitytracker.exception;

/**
 * Thrown when service execution fails unexpectedly.
 */
public class ServiceException extends AppException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
