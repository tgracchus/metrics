package com.tgracchus.metrics.aggregator;

import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class TimescaleDBSink implements Sink<Windowed<String>, MetricEvent> {

    private final JdbcTemplate jdbcTemplate;

    public TimescaleDBSink(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public InsertResult insert(Windowed<String> key, MetricEvent metricEvent) {
        Instant timestamp = Instant.ofEpochMilli(metricEvent.getTimestamp());
        OffsetDateTime localTime = OffsetDateTime.ofInstant(timestamp, ZoneId.of("UTC"));
        String insertQuery = "insert into metrics values(?,?,?) ON CONFLICT (key,time) DO UPDATE SET value=?";
        int updated = jdbcTemplate.update(insertQuery, localTime, metricEvent.getMetric(), metricEvent.getValue(), metricEvent.getValue());
        return new InsertResult(updated);
    }

}
