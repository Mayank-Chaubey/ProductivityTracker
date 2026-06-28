package com.productivitytracker.exception;

/**
 * Thrown when required application configuration is missing or invalid.
 */
public class ConfigurationException extends AppException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
