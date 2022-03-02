package com.tgracchus.metrics.config;

import com.tgracchus.metrics.aggregator.MovingAverage;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
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
        String bootstrapAddress = buildKafkaUrl();
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
    public Serde<MovingAverage> movingAverageSerde() {
        Serde<MovingAverage> movingAverageSerde = new JsonSerde<>(MovingAverage.class);
        movingAverageSerde.configure(properties(), false);
        return movingAverageSerde;
    }

    @Value(value = "${kafka.host}")
    private String host;

    @Bean
    public ProducerFactory<String, MetricEvent> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        String bootstrapAddress = buildKafkaUrl();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return new DefaultKafkaProducerFactory<>(props);
    }

    private String buildKafkaUrl() {
        return String.format("%s:29092", host);
    }

    @Bean
    public KafkaTemplate<String, MetricEvent> kafkaTemplate(ProducerFactory<String, MetricEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        String bootstrapAddress = buildKafkaUrl();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic ingestTopic() {
        return new NewTopic("ingest", 1, (short) 1);
    }
}
