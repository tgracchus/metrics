package com.tgracchus.metrics.config;


import com.tgracchus.metrics.MovingAverage;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafkaStreams
@EnableKafka
public class KafkaConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        return new KafkaStreamsConfiguration(properties());
    }

    private Map<String, Object> properties() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ingest_to_1min");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, MetricEvent.class.getPackageName() + "," + MovingAverage.class.getPackageName());
        return props;
    }


    @Bean
    public Serde<MetricEvent> metricEventSerde() {
        Serde<MetricEvent> metricEventSerde = new JsonSerde<>(MetricEvent.class);
        metricEventSerde.configure(properties(), false);
        return metricEventSerde;
    }

    @Bean
    public Serde<String> stringSerde() {
        Serde<String> stringSerde = Serdes.String();
        stringSerde.configure(properties(), true);
        return stringSerde;
    }


    @Bean
    public Serde<Double> doubleSerde() {
        Serde<Double> doubleSerde = Serdes.Double();
        doubleSerde.configure(properties(), false);
        return doubleSerde;
    }

    @Bean
    public WindowedSerdes.TimeWindowedSerde<String> timeWindowedSerde(Serde<String> stringSerde) {
        WindowedSerdes.TimeWindowedSerde<String> timeWindowedSerde = new WindowedSerdes.TimeWindowedSerde<>(stringSerde, 10000);
        timeWindowedSerde.configure(properties(), true);
        return timeWindowedSerde;
    }


    @Bean
    public Serde<MovingAverage> movingAverageSerde() {
        Serde<MovingAverage> movingAverageSerde = new JsonSerde<>(MovingAverage.class);
        movingAverageSerde.configure(properties(), false);
        return movingAverageSerde;
    }

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

}
