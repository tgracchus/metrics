package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.Metric;
import com.tgracchus.metrics.endpoints.dto.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimescaleDBMetricsService implements MetricsService {

    private final JdbcTemplate jdbcTemplate;
    private final MetricsRowMapper rowMapper;

    @Autowired
    public TimescaleDBMetricsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new MetricsRowMapper();
    }

    @Override
    public List<Metric> getMetricsByKeyAndTimeRange(String key, TimeRange timeRange, Long timestamp) {
        Instant timestampInstant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime localTime = OffsetDateTime.ofInstant(timestampInstant, ZoneId.of("UTC"));
        switch (timeRange){
            case LAST_15M: {
                String query = "SELECT time as timestamp,key,value FROM metrics WHERE key=? AND time>? at time zone 'utc' - INTERVAL '15 minutes' ORDER BY timestamp DESC";
                return jdbcTemplate.query(query, this.rowMapper, key, localTime);
            }
            case LAST_HOUR: {
                String query = "SELECT time as timestamp,key,value FROM metrics WHERE key=? AND time>? at time zone 'utc' - INTERVAL '1 hour' ORDER BY timestamp DESC";
                return jdbcTemplate.query(query, this.rowMapper, key, localTime);
            }
            case LAST_DAY: {
                String query = "SELECT time_bucket('1 hour', time) as timestamp, key, avg(value) as value FROM metrics " +
                        "WHERE key = ? AND time > ? - interval '1' day GROUP BY key, timestamp ORDER BY key, timestamp DESC;";
                return jdbcTemplate.query(query, this.rowMapper, key, localTime);
            }
        }
        return new ArrayList<>();
    }

    private static class MetricsRowMapper implements RowMapper<Metric>{

        @Override
        public Metric mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp time = rs.getTimestamp("timestamp");
            String key = rs.getString("key");
            Double value = rs.getDouble("value");
            return new Metric(key, time.getTime(), value);
        }
    }
}
