package com.xavelo.template.application.service;

import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.application.domain.CrudObject;
import com.xavelo.template.application.exception.CrudObjectNotFoundException;
import com.xavelo.template.application.port.in.CrudUseCase;
import com.xavelo.template.application.port.out.CrudObjectCreatedPort;
import com.xavelo.template.application.port.out.CrudPort;
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
 * Application service for CRUD use cases backed by a persistence adapter.
 */
@Service
public class CrudService implements CrudUseCase {

    private static final Logger logger = LogManager.getLogger(CrudService.class);

    private final CrudPort crudPort;
    private final CrudObjectCreatedPort crudObjectCreatedPort;

    public CrudService(CrudPort crudPort, CrudObjectCreatedPort crudObjectCreatedPort) {
        this.crudPort = crudPort;
        this.crudObjectCreatedPort = crudObjectCreatedPort;
    }

    @Override
    public CrudObjectPageDto listCrudObjects(Integer page, Integer size, String sort) {
        int resolvedPage = page == null || page < 0 ? 0 : page;
        int resolvedSize = size == null || size < 1 ? 20 : size;

        CrudPort.Sort sortDefinition = resolveSort(sort);
        CrudPort.PageResult pageResult = crudPort.fetchPage(resolvedPage, resolvedSize, sortDefinition);

        List<CrudObjectDto> content = pageResult.content().stream()
            .map(this::toDto)
            .toList();
        int totalElements = Math.toIntExact(pageResult.totalElements());
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
        CrudPort.CrudRecord record = crudPort.findById(crudObjectId)
            .orElseThrow(() -> new CrudObjectNotFoundException(crudObjectId));
        return toDto(record);
    }

    @Override
    public CrudObjectDto createCrudObject(CrudObjectCreateRequestDto request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String id = UUID.randomUUID().toString();
        CrudPort.CrudRecord toPersist = new CrudPort.CrudRecord(
            id,
            request.getName(),
            extractDescription(request.getDescription()),
            now,
            now
        );
        CrudPort.CrudRecord stored = crudPort.save(toPersist);
        crudObjectCreatedPort.publishCrudObectCreated(new CrudObject(stored.id(), stored.name()));
        logger.info("Created CrudObject with id {}", stored.id());
        return toDto(stored);
    }

    private CrudPort.Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return CrudPort.Sort.by("name", CrudPort.SortDirection.ASC);
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        boolean descending = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim());
        return CrudPort.Sort.by(property, descending ? CrudPort.SortDirection.DESC : CrudPort.SortDirection.ASC);
    }

    private CrudObjectDto toDto(CrudPort.CrudRecord record) {
        CrudObjectDto dto = new CrudObjectDto()
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
