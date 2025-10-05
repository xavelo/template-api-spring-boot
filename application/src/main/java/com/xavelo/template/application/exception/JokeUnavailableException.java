package com.xavelo.template.application.exception;

/**
 * Exception thrown when the application cannot retrieve a new joke and no cached data is available.
 */
public class JokeUnavailableException extends RuntimeException {

    public JokeUnavailableException(String message) {
        super(message);
    }

    public JokeUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
