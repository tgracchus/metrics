package com.tgracchus.metrics.aggregator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class MovingAverage {
    private final String key;
    private final Long timestamp;
    private final String metric;
    private final Integer count;
    private final BigDecimal value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovingAverage(@JsonProperty("key")String key,
                         @JsonProperty("timestamp") Long timestamp,
                         @JsonProperty("metric") String metric,
                         @JsonProperty("count") Integer count,
                         @JsonProperty("value") BigDecimal value) {
        this.key = key;
        this.timestamp = timestamp;
        this.metric = metric;
        this.count = count;
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getMetric() {
        return metric;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }
}
