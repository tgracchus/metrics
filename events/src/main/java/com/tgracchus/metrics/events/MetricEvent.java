package com.tgracchus.metrics.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class MetricEvent {
    private final Double value;
    private final Long timestamp;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MetricEvent(@JsonProperty("value") Double value,@JsonProperty("timestamp") Long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
