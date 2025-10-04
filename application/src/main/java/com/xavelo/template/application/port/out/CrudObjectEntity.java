package com.xavelo.template.application.port.out;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Persistence representation of a CRUD object.
 */
public class CrudObjectEntity {

    private final String id;
    private String name;
    private String description;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public CrudObjectEntity(String id, String name, String description, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.description = description;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }
}
