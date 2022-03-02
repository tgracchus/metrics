package com.tgracchus.metrics.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MetricEvent {
    private final String metric;
    private final Double value;
    private final Long timestamp;
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MetricEvent(
            @JsonProperty("metric") String metric,
            @JsonProperty("value") Double value,
            @JsonProperty("timestamp") Long timestamp
    ) {
        this.metric = metric;
        this.value = value;
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getMetric() {
        return metric;
    }
}
