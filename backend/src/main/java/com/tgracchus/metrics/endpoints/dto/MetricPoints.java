package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MetricPoints {
    private final String metric;
    private final List<Metric> points;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MetricPoints(@JsonProperty("metric") String metric,
                        @JsonProperty("points") List<Metric> points
    ) {
        this.metric = metric;
        this.points = points;
    }

    public String getMetric() {
        return metric;
    }

    public List<Metric> getPoints() {
        return points;
    }
}
