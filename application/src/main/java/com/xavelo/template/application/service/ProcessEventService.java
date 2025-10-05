package com.xavelo.template.application.service;

import com.xavelo.template.application.domain.Event;
import com.xavelo.template.application.port.in.ProcessEventUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Application service that logs consumed events.
 */
@Service
public class ProcessEventService implements ProcessEventUseCase {

    private static final Logger logger = LogManager.getLogger(ProcessEventService.class);

    @Override
    public void process(Event event) {
        logger.info("Processing event {} with text: {}", event.id(), event.text());
    }
}
