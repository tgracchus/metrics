package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.Metric;
import com.tgracchus.metrics.endpoints.dto.TimeRange;

import java.util.List;

public interface MetricsService {

    List<Metric> getMetricsByKeyAndTimeRange(String key, TimeRange timeRange, Long timestamp);
}
