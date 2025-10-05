package com.xavelo.template.application.service;

import com.xavelo.template.api.contract.model.ItemCreateRequestDto;
import com.xavelo.template.api.contract.model.ItemDto;
import com.xavelo.template.api.contract.model.ItemPageDto;
import com.xavelo.template.application.domain.Item;
import com.xavelo.template.application.exception.ItemNotFoundException;
import com.xavelo.template.application.port.in.ItemUseCase;
import com.xavelo.template.application.port.out.ItemCreatedPort;
import com.xavelo.template.application.port.out.ItemPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Application service for Item use cases backed by a persistence adapter.
 */
@Service
public class ItemService implements ItemUseCase {

    private static final Logger logger = LogManager.getLogger(ItemService.class);

    private final ItemPort itemPort;
    private final ItemCreatedPort itemCreatedPort;

    public ItemService(ItemPort itemPort, ItemCreatedPort itemCreatedPort) {
        this.itemPort = itemPort;
        this.itemCreatedPort = itemCreatedPort;
    }

    @Override
    public ItemPageDto listItems(Integer page, Integer size, String sort) {
        int resolvedPage = page == null || page < 0 ? 0 : page;
        int resolvedSize = size == null || size < 1 ? 20 : size;

        ItemPort.Sort sortDefinition = resolveSort(sort);
        ItemPort.PageResult pageResult = itemPort.fetchPage(resolvedPage, resolvedSize, sortDefinition);

        List<ItemDto> content = pageResult.content().stream()
            .map(this::toDto)
            .toList();
        int totalElements = Math.toIntExact(pageResult.totalElements());
        int totalPages = resolvedSize == 0 ? 0 : (int) Math.ceil((double) totalElements / resolvedSize);

        return new ItemPageDto()
            .content(new ArrayList<>(content))
            .page(resolvedPage)
            .size(resolvedSize)
            .totalElements(totalElements)
            .totalPages(totalPages);
    }

    @Override
    public ItemDto getItem(String itemId) {
        ItemPort.ItemRecord record = itemPort.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException(itemId));
        return toDto(record);
    }

    @Override
    public ItemDto createItem(ItemCreateRequestDto request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String id = UUID.randomUUID().toString();
        ItemPort.ItemRecord toPersist = new ItemPort.ItemRecord(
            id,
            request.getName(),
            extractDescription(request.getDescription()),
            now,
            now
        );
        ItemPort.ItemRecord stored = itemPort.save(toPersist);
        itemCreatedPort.publishItemCreated(new Item(stored.id(), stored.name()));
        logger.info("Created Item with id {}", stored.id());
        return toDto(stored);
    }

    private ItemPort.Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return ItemPort.Sort.by("name", ItemPort.SortDirection.ASC);
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        boolean descending = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim());
        return ItemPort.Sort.by(property, descending ? ItemPort.SortDirection.DESC : ItemPort.SortDirection.ASC);
    }

    private ItemDto toDto(ItemPort.ItemRecord record) {
        ItemDto dto = new ItemDto()
            .id(record.id())
            .name(record.name())
            .createdAt(record.createdAt())
            .updatedAt(record.updatedAt());
        if (record.description() != null) {
            dto.description(record.description());
        }
        return dto;
    }

    private String extractDescription(JsonNullable<String> description) {
        if (description != null && description.isPresent()) {
            return description.get();
        }
        return null;
    }

}
