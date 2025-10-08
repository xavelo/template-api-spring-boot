package com.xavelo.template.application.exception;

public class ExternalApiUnavailableException extends RuntimeException {

    private final int requestedStatus;

    public ExternalApiUnavailableException(int requestedStatus, Throwable cause) {
        super(String.format("External API is unavailable for status %d", requestedStatus), cause);
        this.requestedStatus = requestedStatus;
    }

    public int getRequestedStatus() {
        return requestedStatus;
    }
}
