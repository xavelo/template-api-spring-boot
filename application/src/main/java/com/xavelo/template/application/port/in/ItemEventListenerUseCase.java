package com.xavelo.template.application.port.in;

import com.xavelo.template.application.domain.Item;

/**
 * Inbound application port describing how item-related events should be handled by the core domain.
 */
public interface ItemEventListenerUseCase {

    /**
     * Reacts to an event that indicates a new {@link Item} has been created upstream.
     *
     * @param item the item carried by the consumed message
     */
    void onItemCreated(Item item);
}
