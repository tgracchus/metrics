package com.tgracchus.metrics.endpoints.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class IngestRequest {

    @NotEmpty
    @Valid
    private List<IngestMetric> metrics;
}
