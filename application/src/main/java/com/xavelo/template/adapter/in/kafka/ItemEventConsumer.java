package com.xavelo.template.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.Item;
import com.xavelo.template.application.port.in.ItemEventListenerUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.xavelo.common.metrics.AdapterMetrics.Direction.IN;
import static com.xavelo.common.metrics.AdapterMetrics.Type.KAFKA;

@Adapter
@Component
public class ItemEventConsumer {

    private static final Logger logger = LogManager.getLogger(ItemEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ItemEventListenerUseCase itemEventListenerUseCase;

    public ItemEventConsumer(ObjectMapper objectMapper, ItemEventListenerUseCase itemEventListenerUseCase) {
        this.objectMapper = objectMapper;
        this.itemEventListenerUseCase = itemEventListenerUseCase;
    }

    @KafkaListener(
        topics = "${template.kafka.item-events.topic}",
        containerFactory = "itemEventKafkaListenerContainerFactory"
    )
    @CountAdapterInvocation(name = "item-event-consume", direction = IN, type = KAFKA)
    public void consume(String payload) {
        try {
            Item item = objectMapper.readValue(payload, Item.class);
            logger.info("Consumed item event for id {}", item.id());
            itemEventListenerUseCase.onItemCreated(item);
        } catch (JsonProcessingException e) {
            logger.error("Unable to deserialize item event payload", e);
            throw new IllegalStateException("Failed to deserialize item event", e);
        }
    }
}
