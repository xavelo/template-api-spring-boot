package com.xavelo.template.application.port.out;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port that defines persistence operations for CRUD objects.
 */
public interface CrudPort {

    /**
     * Retrieves all crud objects persisted in the underlying data store.
     *
     * @return the collection of stored crud objects
     */
    List<CrudObjectEntity> findAll();

    /**
     * Looks up a crud object by its identifier.
     *
     * @param id the identifier of the object
     * @return an optional containing the object when found
     */
    Optional<CrudObjectEntity> findById(String id);

    /**
     * Persists a new crud object.
     *
     * @param entity the object to persist
     * @return the persisted entity
     */
    CrudObjectEntity insert(CrudObjectEntity entity);
}
