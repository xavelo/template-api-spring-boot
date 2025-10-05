package com.xavelo.template.adapter.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.Event;
import com.xavelo.template.application.port.in.ProcessEventUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.xavelo.common.metrics.AdapterMetrics.Direction.IN;
import static com.xavelo.common.metrics.AdapterMetrics.Type.KAFKA;

@Adapter
@Component
public class EventConsumer {

    private static final Logger logger = LogManager.getLogger(EventConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProcessEventUseCase processEventUseCase;

    public EventConsumer(ObjectMapper objectMapper, ProcessEventUseCase processEventUseCase) {
        this.objectMapper = objectMapper;
        this.processEventUseCase = processEventUseCase;
    }

    @KafkaListener(
        topics = "${kafka.consumer.topic}",
        containerFactory = "testTopicEventKafkaListenerContainerFactory"
    )
    @CountAdapterInvocation(name = "item-events", direction = IN, type = KAFKA)
    public void consume(String payload) {
        try {
            Event event = objectMapper.readValue(payload, Event.class);
            processEventUseCase.process(event);
        } catch (JsonProcessingException e) {
            logger.error("Unable to deserialize event payload", e);
            throw new IllegalStateException("Failed to deserialize event", e);
        }
    }
}
