package com.xavelo.template.adapter.in.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.template.application.domain.Item;
import com.xavelo.template.application.port.in.ItemEventListenerUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class ItemEventConsumerTest {

    private ItemEventListenerUseCase itemEventListenerUseCase;
    private ObjectMapper objectMapper;
    private ItemEventConsumer itemEventConsumer;

    @BeforeEach
    void setUp() {
        itemEventListenerUseCase = Mockito.mock(ItemEventListenerUseCase.class);
        objectMapper = new ObjectMapper();
        itemEventConsumer = new ItemEventConsumer(objectMapper, itemEventListenerUseCase);
    }

    @Test
    void shouldInvokeInboundPortWhenPayloadIsConsumed() throws Exception {
        Item item = new Item("item-id", "Test Item");
        String payload = objectMapper.writeValueAsString(item);

        itemEventConsumer.consume(payload);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemEventListenerUseCase).onItemCreated(itemCaptor.capture());
        assertThat(itemCaptor.getValue()).isEqualTo(item);
    }
}
