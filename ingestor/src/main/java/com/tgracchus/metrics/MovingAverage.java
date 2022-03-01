package com.tgracchus.metrics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MovingAverage {
    private final Long timestamp;
    private final String metric;
    private final Integer count;
    private final Double value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovingAverage(@JsonProperty("timestamp") Long timestamp,
                         @JsonProperty("metric") String metric,
                         @JsonProperty("count") Integer count,
                         @JsonProperty("value") Double value) {
        this.timestamp = timestamp;
        this.metric = metric;
        this.count = count;
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public Double getValue() {
        return value;
    }

    public String getMetric() {
        return metric;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
