package com.xavelo.template.application.service;

import com.xavelo.template.api.contract.model.CrudObjectCreateRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectDto;
import com.xavelo.template.api.contract.model.CrudObjectPageDto;
import com.xavelo.template.api.contract.model.CrudObjectPatchRequestDto;
import com.xavelo.template.api.contract.model.CrudObjectUpdateRequestDto;
import com.xavelo.template.application.exception.CrudObjectNotFoundException;
import com.xavelo.template.application.port.in.CrudUseCase;
import com.xavelo.template.application.port.out.CrudObjectEntity;
import com.xavelo.template.application.port.out.CrudPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * Application service that orchestrates CRUD operations and delegates persistence to {@link CrudPort}.
 */
@Service
public class CrudService implements CrudUseCase {

    private static final Logger logger = LogManager.getLogger(CrudService.class);

    private final CrudPort crudPort;

    public CrudService(CrudPort crudPort) {
        this.crudPort = crudPort;
    }

    @Override
    public CrudObjectPageDto listCrudObjects(Integer page, Integer size, String sort) {
        int resolvedPage = page == null || page < 0 ? 0 : page;
        int resolvedSize = size == null || size < 1 ? 20 : size;

        List<CrudObjectEntity> ordered = crudPort.findAll().stream()
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
        CrudObjectEntity entity = findRequired(crudObjectId);
        return toDto(entity);
    }

    @Override
    public CrudObjectDto createCrudObject(CrudObjectCreateRequestDto request) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        String id = UUID.randomUUID().toString();
        CrudObjectEntity entity = new CrudObjectEntity(id, request.getName(), extractDescription(request.getDescription()), now, now);
        CrudObjectEntity persisted = crudPort.insert(entity);
        logger.info("Created CrudObject with id {}", id);
        return toDto(persisted);
    }

    @Override
    public CrudObjectDto replaceCrudObject(String crudObjectId, CrudObjectUpdateRequestDto request) {
        CrudObjectEntity existing = findRequired(crudObjectId);
        existing.setName(request.getName());
        existing.setDescription(extractDescription(request.getDescription()));
        existing.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        if (!crudPort.update(existing)) {
            throw new CrudObjectNotFoundException(crudObjectId);
        }
        logger.info("Replaced CrudObject with id {}", crudObjectId);
        return toDto(existing);
    }

    @Override
    public CrudObjectDto updateCrudObject(String crudObjectId, CrudObjectPatchRequestDto request) {
        CrudObjectEntity existing = findRequired(crudObjectId);
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        JsonNullable<String> description = request.getDescription();
        if (description != null && description.isPresent()) {
            existing.setDescription(description.get());
        }
        existing.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        if (!crudPort.update(existing)) {
            throw new CrudObjectNotFoundException(crudObjectId);
        }
        logger.info("Updated CrudObject with id {}", crudObjectId);
        return toDto(existing);
    }

    @Override
    public void deleteCrudObject(String crudObjectId) {
        if (!crudPort.deleteById(crudObjectId)) {
            throw new CrudObjectNotFoundException(crudObjectId);
        }
        logger.info("Deleted CrudObject with id {}", crudObjectId);
    }

    private CrudObjectEntity findRequired(String crudObjectId) {
        return crudPort.findById(crudObjectId)
            .orElseThrow(() -> new CrudObjectNotFoundException(crudObjectId));
    }

    private CrudObjectDto toDto(CrudObjectEntity entity) {
        CrudObjectDto dto = new CrudObjectDto()
            .id(entity.getId())
            .name(entity.getName())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt());
        if (entity.getDescription() != null) {
            dto.description(entity.getDescription());
        }
        return dto;
    }

    private Comparator<CrudObjectEntity> resolveComparator(String sort) {
        if (sort == null || sort.isBlank()) {
            return Comparator.comparing(CrudObjectEntity::getName, String.CASE_INSENSITIVE_ORDER);
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        boolean descending = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim());

        Comparator<CrudObjectEntity> comparator = switch (property) {
            case "id" -> Comparator.comparing(CrudObjectEntity::getId);
            case "createdAt" -> Comparator.comparing(CrudObjectEntity::getCreatedAt);
            case "updatedAt" -> Comparator.comparing(CrudObjectEntity::getUpdatedAt);
            case "name" -> Comparator.comparing(CrudObjectEntity::getName, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(CrudObjectEntity::getName, String.CASE_INSENSITIVE_ORDER);
        };
        return descending ? comparator.reversed() : comparator;
    }

    private String extractDescription(JsonNullable<String> description) {
        if (description != null && description.isPresent()) {
            return description.get();
        }
        return null;
    }
}
