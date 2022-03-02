package com.tgracchus.metrics.endpoints;

import com.tgracchus.metrics.endpoints.dto.IngestMetric;
import com.tgracchus.metrics.endpoints.dto.Metric;
import com.tgracchus.metrics.endpoints.dto.TimeRange;
import com.tgracchus.metrics.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryEndpoint {

    private final MetricsService metricsService;

    @Autowired
    public QueryEndpoint(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping(path = "/timeseries", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public List<Metric> timeseries(@RequestParam("metric") String key, @RequestParam("timeRange")TimeRange timeRange) {
        return metricsService.getMetricsByKeyAndTimeRange(key,timeRange);
    }
}
