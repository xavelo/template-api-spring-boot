package com.xavelo.template.application.port.out;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Outbound port that defines persistence operations for CrudObjects.
 */
public interface CrudPort {

    /**
     * Retrieves a page of CrudObjects applying the requested sort order.
     *
     * @param page zero-based page index
     * @param size number of elements per page
     * @param sort sort definition to apply
     * @return page result containing the requested slice of records and the total number of elements
     */
    PageResult fetchPage(int page, int size, Sort sort);

    /**
     * Retrieves a CrudObject by its identifier.
     *
     * @param id unique identifier of the CrudObject
     * @return optional containing the record when found
     */
    Optional<CrudRecord> findById(String id);

    /**
     * Persists a CrudObject record.
     *
     * @param record record to persist
     * @return the stored record as retrieved from the persistence layer
     */
    CrudRecord save(CrudRecord record);

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

    record PageResult(List<CrudRecord> content, long totalElements) {
        public PageResult {
            content = List.copyOf(Objects.requireNonNull(content, "content must not be null"));
        }
    }

    record CrudRecord(String id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        public CrudRecord {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(createdAt, "createdAt must not be null");
            Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        }
    }
}
