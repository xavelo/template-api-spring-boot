package com.xavelo.template.adapter.out;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.template.application.port.out.CrudPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Adapter
@Repository
public class MySqlAdapter implements CrudPort {

    private static final Logger logger = LogManager.getLogger(MySqlAdapter.class);
    private static final String SYSTEM_USER = "system";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MySqlAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PageResult fetchPage(int page, int size, Sort sort) {
        int resolvedPage = Math.max(page, 0);
        int resolvedSize = Math.max(size, 1);
        String orderBy = resolveOrderBy(sort);

        String sql = """
            SELECT id, name, description, created_on, modified_on
            FROM crud
            ORDER BY %s
            LIMIT :limit OFFSET :offset
            """.formatted(orderBy);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("limit", resolvedSize)
            .addValue("offset", resolvedPage * (long) resolvedSize);

        List<CrudRecord> content = jdbcTemplate.query(sql, parameters, (rs, rowNum) -> mapRecord(rs));
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM crud", Map.of(), Long.class);
        long resolvedTotal = total == null ? 0L : total;
        logger.debug("Fetched {} CrudObjects from MySQL (page={}, size={})", content.size(), resolvedPage, resolvedSize);
        return new PageResult(content, resolvedTotal);
    }

    @Override
    public Optional<CrudRecord> findById(String id) {
        try {
            CrudRecord record = jdbcTemplate.queryForObject(
                """
                    SELECT id, name, description, created_on, modified_on
                    FROM crud
                    WHERE id = :id
                    """,
                new MapSqlParameterSource("id", id),
                (rs, rowNum) -> mapRecord(rs)
            );
            return Optional.ofNullable(record);
        } catch (EmptyResultDataAccessException ex) {
            logger.debug("CrudObject {} not found in MySQL", id);
            return Optional.empty();
        }
    }

    @Override
    public CrudRecord save(CrudRecord record) {
        String sql = """
            INSERT INTO crud (id, name, description, created_by, created_on, modified_by, modified_on)
            VALUES (:id, :name, :description, :createdBy, :createdOn, :modifiedBy, :modifiedOn)
            """;

        MapSqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("id", record.id())
            .addValue("name", record.name())
            .addValue("description", record.description())
            .addValue("createdBy", SYSTEM_USER)
            .addValue("createdOn", toTimestamp(record.createdAt()))
            .addValue("modifiedBy", SYSTEM_USER)
            .addValue("modifiedOn", toTimestamp(record.updatedAt()));

        jdbcTemplate.update(sql, parameters);
        logger.debug("Inserted CrudObject {} into MySQL", record.id());
        return findById(record.id())
            .orElseThrow(() -> new IllegalStateException("Failed to load CrudObject after insert: " + record.id()));
    }

    private CrudRecord mapRecord(ResultSet rs) throws SQLException {
        OffsetDateTime createdAt = toOffsetDateTime(rs.getTimestamp("created_on"));
        OffsetDateTime updatedAt = toOffsetDateTime(rs.getTimestamp("modified_on"));
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
        return new CrudRecord(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("description"),
            createdAt,
            updatedAt
        );
    }

    private OffsetDateTime toOffsetDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toInstant().atOffset(ZoneOffset.UTC);
    }

    private Timestamp toTimestamp(OffsetDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Timestamp.from(dateTime.toInstant());
    }

    private String resolveOrderBy(Sort sort) {
        String property = (sort == null || sort.property() == null || sort.property().isBlank())
            ? "name"
            : sort.property();
        String column = switch (property) {
            case "id" -> "id";
            case "createdAt" -> "created_on";
            case "updatedAt" -> "modified_on";
            case "name" -> "name";
            default -> "name";
        };
        String direction = sort != null && sort.direction() == SortDirection.DESC ? "DESC" : "ASC";
        return column + " " + direction;
    }
}
