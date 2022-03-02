package com.tgracchus.metrics.endpoints.dto;

import lombok.Data;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class IngestPoint {

    @NotNull
    private Long timestamp;

    @NotNull
    private Double value;


}
