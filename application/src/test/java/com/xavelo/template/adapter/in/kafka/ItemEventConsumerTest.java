package com.xavelo.template.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.template.application.domain.Event;
import com.xavelo.template.application.port.in.ProcessEventUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class ItemEventConsumerTest {

    private ProcessEventUseCase processEventUseCase;
    private ObjectMapper objectMapper;
    private EventConsumer itemEventConsumer;

    @BeforeEach
    void setUp() {
        processEventUseCase = Mockito.mock(ProcessEventUseCase.class);
        objectMapper = new ObjectMapper();
        itemEventConsumer = new EventConsumer(objectMapper, processEventUseCase);
    }

    @Test
    void shouldInvokeInboundPortWhenPayloadIsConsumed() throws Exception {
        Event event = new Event("event-id", "Test event");
        String payload = objectMapper.writeValueAsString(event);

        itemEventConsumer.consume(payload);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(processEventUseCase).process(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isEqualTo(event);
    }
}
