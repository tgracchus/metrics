package com.tgracchus.metrics;

import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Reducer;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
public class MetricsAggregator {

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder,
                       Serde<String> stringSerde,
                       WindowedSerdes.TimeWindowedSerde<String> timeWindowedSerde,
                       Serde<MetricEvent> metricEventSerde,
                       Serde<MovingAverage> movingAverageSerde) {

        Duration timeDifference = Duration.ofMinutes(1);
        Duration gracePeriod = Duration.ofMillis(50);
        KStream<String, MetricEvent> messageStream = streamsBuilder
                .stream("ingest", Consumed.with(stringSerde, metricEventSerde));

        KTable<Windowed<String>, MovingAverage> averages_1m = messageStream
                .map((key, value) -> new KeyValue<>(key, new MovingAverage(value.getTimestamp(), key, 1, value.getValue())))
                .groupByKey()
                .windowedBy(SlidingWindows.ofTimeDifferenceAndGrace(timeDifference, gracePeriod))
                .reduce(
                        new Reducer<MovingAverage>() {
                            @Override
                            public MovingAverage apply(MovingAverage value1, MovingAverage value2) {
                                return new MovingAverage(
                                        value1.getTimestamp(), value1.getMetric(),
                                        value1.getCount() + value2.getCount(),
                                        value1.getValue() + value2.getValue());
                            }
                        }, Materialized.with(stringSerde, movingAverageSerde))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));


        averages_1m
                .mapValues(new ValueMapper<MovingAverage, MetricEvent>() {
                    @Override
                    public MetricEvent apply(MovingAverage value) {
                        return new MetricEvent(value.getValue() / value.getCount(), value.getTimestamp());
                    }
                })
                .toStream()
                .to("metrics_1min", Produced.with(timeWindowedSerde, metricEventSerde));
    }

}
