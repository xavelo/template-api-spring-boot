package com.xavelo.template.application.service;

import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.application.exception.CrudObjectNotFoundException;
import com.xavelo.template.application.port.in.CrudUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock implementation of {@link CrudUseCase} that keeps entities in memory.
 */
@Service
public class CrudService implements CrudUseCase {

    private static final Logger logger = LogManager.getLogger(CrudService.class);

    private final Map<String, CrudRecord> dataStore = new ConcurrentHashMap<>();

    public CrudService() {
        seedData();
    }

    @Override
    public CrudObjectPageDto listCrudObjects(Integer page, Integer size, String sort) {
        int resolvedPage = page == null || page < 0 ? 0 : page;
        int resolvedSize = size == null || size < 1 ? 20 : size;

        List<CrudRecord> ordered = dataStore.values().stream()
            .sorted(resolveComparator(sort))
            .toList();

        int totalElements = ordered.size();
        int fromIndex = Math.min(resolvedPage * resolvedSize, totalElements);
        int toIndex = Math.min(fromIndex + resolvedSize, totalElements);
        List<CrudObjectDto> content = ordered.subList(fromIndex, toIndex).stream()
            .map(this::toDto)
            .toList();
        int totalPages = resolvedSize == 0 ? 0 : (int) Math.ceil((double) totalElements / resolvedSize);

        return new CrudObjectPageDto()
            .content(new ArrayList<>(content))
            .page(resolvedPage)
            .size(resolvedSize)
            .totalElements(totalElements)
            .totalPages(totalPages);
    }

    @Override
    public CrudObjectDto getCrudObject(String crudObjectId) {
        CrudRecord record = findRequired(crudObjectId);
        return toDto(record);
    }

    @Override
    public CrudObjectDto createCrudObject(CrudObjectCreateRequestDto request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String id = UUID.randomUUID().toString();
        CrudRecord record = new CrudRecord(id, request.getName(), extractDescription(request.getDescription()), now, now);
        dataStore.put(id, record);
        logger.info("Created CrudObject with id {}", id);
        return toDto(record);
    }

    private CrudRecord findRequired(String crudObjectId) {
        CrudRecord record = dataStore.get(crudObjectId);
        if (record == null) {
            throw new CrudObjectNotFoundException(crudObjectId);
        }
        return record;
    }

    private CrudObjectDto toDto(CrudRecord record) {
        CrudObjectDto dto = new CrudObjectDto()
            .id(record.getId())
            .name(record.getName())
            .createdAt(record.getCreatedAt())
            .updatedAt(record.getUpdatedAt());
        if (record.getDescription() != null) {
            dto.description(record.getDescription());
        }
        return dto;
    }

    private Comparator<CrudRecord> resolveComparator(String sort) {
        if (sort == null || sort.isBlank()) {
            return Comparator.comparing(CrudRecord::getName, String.CASE_INSENSITIVE_ORDER);
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        boolean descending = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim());

        Comparator<CrudRecord> comparator = switch (property) {
            case "id" -> Comparator.comparing(CrudRecord::getId);
            case "createdAt" -> Comparator.comparing(CrudRecord::getCreatedAt);
            case "updatedAt" -> Comparator.comparing(CrudRecord::getUpdatedAt);
            case "name" -> Comparator.comparing(CrudRecord::getName, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(CrudRecord::getName, String.CASE_INSENSITIVE_ORDER);
        };
        return descending ? comparator.reversed() : comparator;
    }

    private String extractDescription(JsonNullable<String> description) {
        if (description != null && description.isPresent()) {
            return description.get();
        }
        return null;
    }

    private void seedData() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        store(new CrudRecord(UUID.randomUUID().toString(), "Alpha Object", "Mock object seeded at startup", now.minusDays(3), now.minusDays(1)));
        store(new CrudRecord(UUID.randomUUID().toString(), "Beta Object", null, now.minusDays(2), now.minusHours(10)));
        store(new CrudRecord(UUID.randomUUID().toString(), "Gamma Object", "Illustrative sample", now.minusDays(1), now.minusHours(2)));
    }

    private void store(CrudRecord record) {
        dataStore.put(record.getId(), record);
    }

    private static final class CrudRecord {
        private final String id;
        private String name;
        private String description;
        private final OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private CrudRecord(String id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        private String getId() {
            return id;
        }

        private String getName() {
            return name;
        }

        private String getDescription() {
            return description;
        }

        private OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        private OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

    }
}
