package com.xavelo.template.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.Item;
import com.xavelo.template.application.port.out.ItemCreatedPort;
import org.springframework.kafka.core.KafkaTemplate;

import static com.xavelo.common.metrics.AdapterMetrics.Direction.OUT;
import static com.xavelo.common.metrics.AdapterMetrics.Type.KAFKA;

@Adapter
public class KafkaAdapter implements ItemCreatedPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TEST_TOPIC = "test-topic";

    public KafkaAdapter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @CountAdapterInvocation(name = "item-created", direction = OUT, type = KAFKA)
    @Override
    public void publishItemCreated(Item item) {
        try {
            String payload = objectMapper.writeValueAsString(item);
            kafkaTemplate.send(TEST_TOPIC, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize item", e);
        }
    }
}
