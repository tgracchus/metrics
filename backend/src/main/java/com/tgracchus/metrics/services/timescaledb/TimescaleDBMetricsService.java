package com.tgracchus.metrics.services.timescaledb;

import com.tgracchus.metrics.endpoints.dto.Metric;
import com.tgracchus.metrics.endpoints.dto.MetricPoints;
import com.tgracchus.metrics.endpoints.dto.TimeRange;
import com.tgracchus.metrics.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimescaleDBMetricsService implements MetricsService {

    private final JdbcTemplate jdbcTemplate;
    private final MetricRecordRowMapper rowMapper;

    @Autowired
    public TimescaleDBMetricsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = new MetricRecordRowMapper();
    }

    @Override
    public MetricPoints getMetricsByKeyAndTimeRange(String key, TimeRange timeRange, Long timestamp) {
        Instant timestampInstant = Instant.ofEpochMilli(timestamp);
        OffsetDateTime localTime = OffsetDateTime.ofInstant(timestampInstant, ZoneId.of("UTC"));
        switch (timeRange){
            case LAST_15M: {
                String query = "SELECT time as timestamp,key,value FROM metrics WHERE key=? AND time>? at time zone 'utc' - INTERVAL '15 minutes' ORDER BY timestamp ASC";
                return query(key, localTime, query);
            }
            case LAST_HOUR: {
                String query = "SELECT time_bucket('1 min', time) as timestamp, key, avg(value) as value FROM metrics " +
                        "WHERE key = ? AND time > ? - interval '1' day GROUP BY key, timestamp ORDER BY key, timestamp ASC;";
                return query(key, localTime, query);
            }
            case LAST_DAY: {
                String query = "SELECT time_bucket('30 min', time) as timestamp, key, avg(value) as value FROM metrics " +
                        "WHERE key = ? AND time > ? - interval '1' day GROUP BY key, timestamp ORDER BY key, timestamp ASC;";
                return query(key, localTime, query);
            }
        }
        return new MetricPoints(key,new ArrayList<>());
    }

    private MetricPoints query(String key, OffsetDateTime localTime, String query) {
        List<MetricRecord> records = jdbcTemplate.query(query, this.rowMapper, key, localTime);
        return new MetricPoints(key,
                records.stream().map(metricRecord -> new Metric(metricRecord.getTimestamp(), metricRecord.getValue())).collect(Collectors.toList())
        );
    }

    private static class MetricRecordRowMapper implements RowMapper<MetricRecord>{

        @Override
        public MetricRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp time = rs.getTimestamp("timestamp");
            String key = rs.getString("key");
            Double value = rs.getDouble("value");
            return new MetricRecord(key, time.getTime(), value);
        }
    }
}
