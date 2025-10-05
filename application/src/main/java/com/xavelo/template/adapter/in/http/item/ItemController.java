package com.xavelo.template.adapter.in.http.item;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.ItemApi;
import com.xavelo.template.api.contract.model.ItemCreateRequestDto;
import com.xavelo.template.api.contract.model.ItemDto;
import com.xavelo.template.api.contract.model.ItemPageDto;
import com.xavelo.template.application.port.in.ItemUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Adapter
@RestController
public class ItemController implements ItemApi {

    private static final Logger logger = LogManager.getLogger(ItemController.class);

    private final ItemUseCase itemUseCase;

    public ItemController(ItemUseCase itemUseCase) {
        this.itemUseCase = itemUseCase;
    }

    @Override
    @CountAdapterInvocation(name = "item-create", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<Void> createItem(ItemCreateRequestDto itemCreateRequestDto) {
        ItemDto created = itemUseCase.createItem(itemCreateRequestDto);
        logger.info("Created Item {}", created.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{itemId}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    @CountAdapterInvocation(name = "item-list", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<ItemPageDto> listItems(Integer page, Integer size, String sort) {
        ItemPageDto list = itemUseCase.listItems(page, size, sort);
        logger.info("Listing Items page={}, size={}, sort={}", page, size, sort);
        return ResponseEntity.ok(list);
    }

    @Override
    @CountAdapterInvocation(name = "item-get", direction = AdapterMetrics.Direction.IN, type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<ItemDto> getItem(String itemId) {
        ItemDto item = itemUseCase.getItem(itemId);
        logger.info("Retrieved Item {}", itemId);
        return ResponseEntity.ok(item);
    }



}
