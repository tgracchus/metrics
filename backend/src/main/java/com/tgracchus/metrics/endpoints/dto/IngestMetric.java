package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IngestMetric {
    private final String key;
    private final Double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IngestMetric(@JsonProperty("key") String key,
                        @JsonProperty("value") Double value
    ) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Double getValue() {
        return value;
    }
}
