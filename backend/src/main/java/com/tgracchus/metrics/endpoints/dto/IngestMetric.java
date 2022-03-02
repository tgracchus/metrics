package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IngestMetric {
    private final String metric;
    private final List<IngestPoint> points;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IngestMetric(@JsonProperty("key") String metric,
                        @JsonProperty("points") List<IngestPoint> points
    ) {
        this.metric = metric;
        this.points = points;
    }

    public String getMetric() {
        return metric;
    }

    public List<IngestPoint> getPoints() {
        return points;
    }
}
