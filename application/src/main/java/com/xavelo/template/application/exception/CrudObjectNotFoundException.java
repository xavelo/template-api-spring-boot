package com.xavelo.template.application.exception;

/**
 * Signals that a CrudObject could not be located in the mocked data store.
 */
public class CrudObjectNotFoundException extends RuntimeException {

    public CrudObjectNotFoundException(String crudObjectId) {
        super("CrudObject with id '%s' was not found".formatted(crudObjectId));
    }
}
