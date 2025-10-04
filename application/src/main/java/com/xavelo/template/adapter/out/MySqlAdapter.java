package com.xavelo.template.adapter.out;

import com.xavelo.template.application.port.out.CrudObjectEntity;
import com.xavelo.template.application.port.out.CrudPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

/**
 * MySQL backed implementation of {@link CrudPort}.
 */
@Repository
public class MySqlAdapter implements CrudPort {

    private static final String DEFAULT_ACTOR = "system";

    private static final RowMapper<CrudObjectEntity> ROW_MAPPER = (rs, rowNum) -> {
        OffsetDateTime createdAt = toOffsetDateTime(rs.getTimestamp("created_on"));
        OffsetDateTime updatedAt = toOffsetDateTime(rs.getTimestamp("modified_on"));
        return new CrudObjectEntity(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("description"),
            createdAt,
            updatedAt != null ? updatedAt : createdAt
        );
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MySqlAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CrudObjectEntity> findAll() {
        String sql = "SELECT id, name, description, created_on, modified_on FROM crud";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<CrudObjectEntity> findById(String id) {
        String sql = "SELECT id, name, description, created_on, modified_on FROM crud WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            CrudObjectEntity entity = jdbcTemplate.queryForObject(sql, params, ROW_MAPPER);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public CrudObjectEntity insert(CrudObjectEntity entity) {
        String sql = "INSERT INTO crud (id, name, description, created_by, created_on, modified_by, modified_on) " +
            "VALUES (:id, :name, :description, :createdBy, :createdOn, :modifiedBy, :modifiedOn)";
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("id", entity.getId())
            .addValue("name", entity.getName())
            .addValue("description", entity.getDescription())
            .addValue("createdBy", DEFAULT_ACTOR)
            .addValue("createdOn", toTimestamp(entity.getCreatedAt()))
            .addValue("modifiedBy", DEFAULT_ACTOR)
            .addValue("modifiedOn", toTimestamp(entity.getUpdatedAt()));
        jdbcTemplate.update(sql, params);
        return entity;
    }

    private static Timestamp toTimestamp(OffsetDateTime dateTime) {
        return Timestamp.from(dateTime.toInstant());
    }

    private static OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant().atOffset(ZoneOffset.UTC);
    }
}
