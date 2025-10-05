package com.xavelo.template.application.exception;

/**
 * Signals that an Item could not be located in the mocked data store.
 */
public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(String itemId) {
        super("Item with id '%s' was not found".formatted(itemId));
    }
}
