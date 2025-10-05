package com.xavelo.template.adapter.out.mysql;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.template.application.port.out.ItemPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Adapter
@Repository
public class MySqlAdapter implements ItemPort {

    private static final Logger logger = LogManager.getLogger(MySqlAdapter.class);
    private static final String SYSTEM_USER = "system";

    private final ItemJpaRepository repository;

    public MySqlAdapter(ItemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult fetchPage(int page, int size, ItemPort.Sort sort) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.max(size, 1);
        org.springframework.data.domain.Sort resolvedSort = resolveSort(sort);

        Page<ItemEntity> pageResult = repository.findAll(PageRequest.of(resolvedPage, resolvedSize, resolvedSort));
        List<ItemRecord> content = pageResult.getContent().stream()
            .map(this::mapRecord)
            .toList();
        logger.debug("Fetched {} Items from MySQL (page={}, size={})", content.size(), resolvedPage, resolvedSize);
        return new PageResult(content, pageResult.getTotalElements());
    }

    @Override
    public Optional<ItemRecord> findById(String id) {
        return repository.findById(id)
            .map(this::mapRecord);
    }

    @Override
    @Transactional
    public ItemRecord save(ItemRecord record) {
        ItemEntity entity = toEntity(record);
        ItemEntity saved = repository.save(entity);
        logger.debug("Inserted Item {} into MySQL", record.id());
        return mapRecord(saved);
    }

    private ItemRecord mapRecord(ItemEntity entity) {
        OffsetDateTime createdAt = entity.getCreatedOn();
        OffsetDateTime updatedAt = entity.getModifiedOn() == null ? createdAt : entity.getModifiedOn();
        return new ItemRecord(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            createdAt,
            updatedAt
        );
    }

    private ItemEntity toEntity(ItemRecord record) {
        ItemEntity entity = new ItemEntity();
        entity.setId(record.id());
        entity.setName(record.name());
        entity.setDescription(record.description());
        entity.setCreatedBy(SYSTEM_USER);
        entity.setCreatedOn(record.createdAt());
        entity.setModifiedBy(SYSTEM_USER);
        entity.setModifiedOn(record.updatedAt());
        return entity;
    }

    private org.springframework.data.domain.Sort resolveSort(ItemPort.Sort sort) {
        String property = (sort == null || sort.property() == null || sort.property().isBlank())
            ? "name"
            : sort.property();
        String entityProperty = switch (property) {
            case "id" -> "id";
            case "createdAt" -> "createdOn";
            case "updatedAt" -> "modifiedOn";
            case "name" -> "name";
            default -> "name";
        };
        org.springframework.data.domain.Sort.Direction direction = sort != null && sort.direction() == ItemPort.SortDirection.DESC
            ? org.springframework.data.domain.Sort.Direction.DESC
            : org.springframework.data.domain.Sort.Direction.ASC;
        return org.springframework.data.domain.Sort.by(direction, entityProperty);
    }
}
