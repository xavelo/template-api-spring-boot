package com.xavelo.template.application.port.in;

import com.xavelo.template.application.domain.Event;

/**
 * Inbound application port describing how events consumed from external systems should be handled
 * by the core domain.
 */
public interface ProcessEventUseCase {

    /**
     * Processes the supplied {@link Event}.
     *
     * @param event the event carried by the consumed message
     */
    void process(Event event);
}
