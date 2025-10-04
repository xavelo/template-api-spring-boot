package com.xavelo.template.adapter.out.kafka;

import com.xavelo.template.application.port.out.CrudEventPublisher;
import com.xavelo.template.application.port.out.CrudEventPublisher.CrudCreatedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaCrudEventPublisher implements CrudEventPublisher {

    private static final Logger logger = LogManager.getLogger(KafkaCrudEventPublisher.class);
    private static final String TOPIC_NAME = "test-topic";

    private final KafkaTemplate<String, CrudCreatedEvent> kafkaTemplate;

    public KafkaCrudEventPublisher(KafkaTemplate<String, CrudCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishCrudCreated(CrudCreatedEvent event) {
        kafkaTemplate.send(TOPIC_NAME, event.id(), event);
        logger.info("Published CrudObject created event for id {}", event.id());
    }
}
