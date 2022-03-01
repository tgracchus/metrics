package com.tgracchus.metrics.endpoints.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IngestRequest {

    private final List<IngestMetric> metrics;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public IngestRequest(@JsonProperty("metrics") List<IngestMetric> metrics) {
        this.metrics = metrics;
    }

    public List<IngestMetric> getMetrics() {
        return metrics;
    }
}
