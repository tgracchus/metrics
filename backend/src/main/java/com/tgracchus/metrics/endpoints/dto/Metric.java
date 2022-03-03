package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Metric {
    private final Long timestamp;
    private final Double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Metric(@JsonProperty("timestamp") Long timestamp, @JsonProperty("value") Double value
    ) {
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
