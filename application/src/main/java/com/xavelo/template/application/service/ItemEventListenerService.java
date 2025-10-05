package com.xavelo.template.application.service;

import com.xavelo.template.api.contract.model.ItemDto;
import com.xavelo.template.application.domain.Item;
import com.xavelo.template.application.exception.ItemNotFoundException;
import com.xavelo.template.application.port.in.ItemEventListenerUseCase;
import com.xavelo.template.application.port.in.ItemUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Application service that reacts to incoming item-related domain events.
 */
@Service
public class ItemEventListenerService implements ItemEventListenerUseCase {

    private static final Logger logger = LogManager.getLogger(ItemEventListenerService.class);

    private final ItemUseCase itemUseCase;

    public ItemEventListenerService(ItemUseCase itemUseCase) {
        this.itemUseCase = itemUseCase;
    }

    @Override
    public void onItemCreated(Item item) {
        logger.info("Received item created event for id {}", item.id());
        try {
            ItemDto itemDto = itemUseCase.getItem(item.id());
            logger.info("Validated item {} exists with name {}", itemDto.getId(), itemDto.getName());
        } catch (ItemNotFoundException ex) {
            logger.warn("Item {} was not found when processing the created event", item.id());
        }
    }
}
