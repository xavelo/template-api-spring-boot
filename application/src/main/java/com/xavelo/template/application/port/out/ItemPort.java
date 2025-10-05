package com.xavelo.template.application.port.out;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Outbound port that defines persistence operations for Items.
 */
public interface ItemPort {

    /**
     * Retrieves a page of Items applying the requested sort order.
     *
     * @param page zero-based page index
     * @param size number of elements per page
     * @param sort sort definition to apply
     * @return page result containing the requested slice of records and the total number of elements
     */
    PageResult fetchPage(int page, int size, Sort sort);

    /**
     * Retrieves an Item by its identifier.
     *
     * @param id unique identifier of the Item
     * @return optional containing the record when found
     */
    Optional<ItemRecord> findById(String id);

    /**
     * Persists an Item record.
     *
     * @param record record to persist
     * @return the stored record as retrieved from the persistence layer
     */
    ItemRecord save(ItemRecord record);

    enum SortDirection {
        ASC,
        DESC
    }

    record Sort(String property, SortDirection direction) {
        public Sort {
            Objects.requireNonNull(direction, "direction must not be null");
            property = property == null ? "" : property;
        }

        public static Sort by(String property, SortDirection direction) {
            return new Sort(property, direction);
        }
    }

    record PageResult(List<ItemRecord> content, long totalElements) {
        public PageResult {
            content = List.copyOf(Objects.requireNonNull(content, "content must not be null"));
        }
    }

    record ItemRecord(String id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        public ItemRecord {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(createdAt, "createdAt must not be null");
            Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        }
    }
}
