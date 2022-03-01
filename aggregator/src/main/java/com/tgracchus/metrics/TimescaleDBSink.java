package com.tgracchus.metrics;

import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;


public class TimescaleDBSink implements Sink<Windowed<String>, MetricEvent> {

    private final JdbcTemplate jdbcTemplate;


    public TimescaleDBSink(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public InsertResult insert(Windowed<String> key, MetricEvent metricEvent) {
        Instant timestamp = Instant.ofEpochMilli(metricEvent.getTimestamp());
        LocalDateTime localTime = LocalDateTime.ofInstant(timestamp, ZoneId.of("UTC"));
        String insertQuery = "insert into metrics values(?,?,?)";
        int updated = jdbcTemplate.update(insertQuery, localTime, key.key(), metricEvent.getValue());
        return new InsertResult(updated);
    }

}
