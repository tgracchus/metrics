package com.tgracchus.metrics.aggregator;

public interface Sink<K, V> {
    InsertResult insert(K key, V metricEvent);
}
