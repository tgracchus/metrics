package com.tgracchus.metrics.aggregator;

public interface Sink<K, V> {
    void insert(K key, V metricEvent);
}
