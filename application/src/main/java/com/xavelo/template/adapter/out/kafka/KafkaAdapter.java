package com.xavelo.template.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.CrudObject;
import com.xavelo.template.application.port.out.CrudObjectCreatedPort;
import org.springframework.kafka.core.KafkaTemplate;

import static com.xavelo.common.metrics.AdapterMetrics.Direction.OUT;
import static com.xavelo.common.metrics.AdapterMetrics.Type.KAFKA;

@Adapter
public class KafkaAdapter implements CrudObjectCreatedPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TEST_TOPIC = "test-topic";

    public KafkaAdapter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @CountAdapterInvocation(name = "crud-object-created", direction = OUT, type = KAFKA)
    @Override
    public void publishCrudObectCreated(CrudObject crud) {
        try {
            String payload = objectMapper.writeValueAsString(crud);
            kafkaTemplate.send(TEST_TOPIC, payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize quote", e);
        }
    }
}
