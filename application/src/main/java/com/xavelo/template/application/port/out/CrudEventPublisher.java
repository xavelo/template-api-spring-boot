package com.xavelo.template.application.port.out;

import com.xavelo.template.application.port.out.CrudPort.CrudRecord;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Outbound port for publishing CrudObject domain events.
 */
public interface CrudEventPublisher {

    /**
     * Publishes an event indicating that a new CrudObject has been created.
     *
     * @param event payload describing the created CrudObject
     */
    void publishCrudCreated(CrudCreatedEvent event);

    /**
     * Event payload describing a created CrudObject.
     */
    record CrudCreatedEvent(String id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        public CrudCreatedEvent {
            Objects.requireNonNull(id, "id must not be null");
            Objects.requireNonNull(name, "name must not be null");
            Objects.requireNonNull(createdAt, "createdAt must not be null");
            Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        }

        public static CrudCreatedEvent fromRecord(CrudRecord record) {
            Objects.requireNonNull(record, "record must not be null");
            return new CrudCreatedEvent(record.id(), record.name(), record.description(), record.createdAt(), record.updatedAt());
        }
    }
}
