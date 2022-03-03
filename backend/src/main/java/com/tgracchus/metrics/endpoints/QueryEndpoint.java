package com.tgracchus.metrics.endpoints;

import com.tgracchus.metrics.endpoints.dto.Metric;
import com.tgracchus.metrics.endpoints.dto.MetricPoints;
import com.tgracchus.metrics.endpoints.dto.TimeRange;
import com.tgracchus.metrics.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@RestController
public class QueryEndpoint {

    private final MetricsService metricsService;

    @Autowired
    public QueryEndpoint(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping(path = "/timeseries", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public MetricPoints timeseries(@RequestParam("metric") @NotEmpty String metric, @RequestParam("timeRange")TimeRange timeRange, @RequestParam(value = "timestamp", required = false) Long timestamp) {
        if (timestamp == null){
            timestamp = Instant.now().toEpochMilli();
        }
        return metricsService.getMetricsByKeyAndTimeRange(metric, timeRange, timestamp);
    }
}
