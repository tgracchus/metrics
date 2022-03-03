package com.tgracchus.metrics.services.timescaledb;

public class MetricRecord {
    private final String key;
    private final Long timestamp;
    private final Double value;

    public MetricRecord(String key, Long timestamp, Double value) {
        this.key = key;
        this.timestamp = timestamp;
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
