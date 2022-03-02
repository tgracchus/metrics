package com.tgracchus.metrics.aggregator;

import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
public class MetricsAggregator {

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder,
                       Serde<String> stringSerde,
                       Serde<MetricEvent> metricEventSerde,
                       Serde<MovingAverage> movingAverageSerde,
                       Sink<Windowed<String>, MetricEvent> metricEventSink) {

        Duration timeDifference = Duration.ofMinutes(15);
        KStream<String, MetricEvent> messageStream = streamsBuilder
                .stream("ingest", Consumed.with(stringSerde, metricEventSerde));

        KTable<Windowed<String>, MovingAverage> averages_1m = messageStream
                .map((key, value) -> {
                    long timestampPerMinute = Instant.ofEpochMilli(value.getTimestamp()).truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
                    String newKeyPerMinute = value.getMetric() + timestampPerMinute;
                    return new KeyValue<>(key, new MovingAverage(newKeyPerMinute, timestampPerMinute, value.getMetric(), 1, new BigDecimal(value.getValue())));
                })
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(timeDifference))
                .reduce(
                        new Reducer<MovingAverage>() {
                            @Override
                            public MovingAverage apply(MovingAverage value1, MovingAverage value2) {
                                return new MovingAverage(
                                        value2.getKey(), value2.getTimestamp(), value2.getMetric(),
                                        value1.getCount() + value2.getCount(),
                                        value1.getValue().add(value2.getValue()));
                            }
                        }, Materialized.with(stringSerde, movingAverageSerde));

        averages_1m
                .mapValues(new ValueMapper<MovingAverage, MetricEvent>() {
                    @Override
                    public MetricEvent apply(MovingAverage value) {
                        return new MetricEvent(value.getMetric(), value.getValue().divide(new BigDecimal(value.getCount())).doubleValue(), value.getTimestamp());
                    }
                })
                .toStream()
                .foreach(metricEventSink::insert);
    }

}