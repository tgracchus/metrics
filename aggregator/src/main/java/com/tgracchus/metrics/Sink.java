package com.tgracchus.metrics;

public interface Sink<K, V> {
    InsertResult insert(K key, V metricEvent);
}
