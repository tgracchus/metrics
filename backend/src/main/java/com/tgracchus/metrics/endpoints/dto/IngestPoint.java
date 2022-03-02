package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IngestPoint {
    private final Long timestamp;
    private final Double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IngestPoint(@JsonProperty("timestamp") Long timestamp,
                       @JsonProperty("value") Double value
    ) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Double getValue() {
        return value;
    }
}
