package com.tgracchus.metrics.aggregator;

import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        /*
           .map((key, value) -> {
                    long timestampPerMinute = Instant.ofEpochMilli(value.getTimestamp()).truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
                    String newKeyPerMinute = value.getMetric() + timestampPerMinute;
                    return new KeyValue<>(key, new MovingAverage(newKeyPerMinute, timestampPerMinute, value.getMetric(), 0, value.getValue()));
                })

                    BigDecimal val1 = new BigDecimal(value1.getValue());
                        BigDecimal val2 = new BigDecimal(value2.getValue());
                        BigDecimal sum = val1.add(val2);
                        return new MovingAverage(
                                value2.getKey(), value2.getTimestamp(), value2.getMetric(),
                                value1.getCount() + 1,
                                sum.doubleValue());
                    }
         */

        KTable<Windowed<String>, MovingAverage> averages_1m = messageStream
                .map((key, value) -> {
                    long timestampPerMinute = Instant.ofEpochMilli(value.getTimestamp()).truncatedTo(ChronoUnit.MINUTES).toEpochMilli();
                    String newKeyPerMinute = value.getMetric() + timestampPerMinute;
                    return new KeyValue<>(newKeyPerMinute, value);
                })
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(timeDifference))
                .aggregate(new Initializer<MovingAverage>() {
                    @Override
                    public MovingAverage apply() {
                        return new MovingAverage("", 0L, "", 0, 0.0);
                    }
                }, new Aggregator<String, MetricEvent, MovingAverage>() {
                    @Override
                    public MovingAverage apply(String key, MetricEvent value, MovingAverage aggregate) {
                        BigDecimal val1 = BigDecimal.valueOf(value.getValue());
                        BigDecimal val2 = BigDecimal.valueOf(aggregate.getValue());
                        BigDecimal sum = val1.add(val2);
                        return new MovingAverage(key, value.getTimestamp(), value.getMetric(), aggregate.getCount() + 1, sum.doubleValue());
                    }
                }, Materialized.with(stringSerde, movingAverageSerde));

        averages_1m
                .mapValues(new ValueMapper<MovingAverage, MetricEvent>() {
                    @Override
                    public MetricEvent apply(MovingAverage value) {
                        BigDecimal val = BigDecimal.valueOf(value.getValue());
                        BigDecimal division = val.divide(new BigDecimal(value.getCount()), RoundingMode.HALF_DOWN);
                        return new MetricEvent(value.getMetric(), division.doubleValue(), value.getTimestamp());
                    }
                })
                .toStream()
                .foreach(metricEventSink::insert);
    }

}