package com.productivitytracker.exception;

/**
 * Thrown when CSRF validation fails.
 */
public class CsrfException extends AppException {

    public CsrfException(String message) {
        super(message);
    }
}
