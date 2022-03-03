package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.MetricPoints;
import com.tgracchus.metrics.endpoints.dto.TimeRange;

public interface MetricsService {

    MetricPoints getMetricsByKeyAndTimeRange(String key, TimeRange timeRange, Long timestamp);
}

