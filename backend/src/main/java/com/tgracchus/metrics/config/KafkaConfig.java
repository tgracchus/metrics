package com.tgracchus.metrics.config;

import com.tgracchus.metrics.aggregator.MovingAverage;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
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
public class KafkaConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs(KafkaProperties kafkaProperties) {
        return new KafkaStreamsConfiguration(properties(kafkaProperties));
    }


    @Bean
    public Serde<MetricEvent> metricEventSerde(KafkaProperties kafkaProperties) {
        Serde<MetricEvent> metricEventSerde = new JsonSerde<>(MetricEvent.class);
        metricEventSerde.configure(properties(kafkaProperties), false);
        return metricEventSerde;
    }

    @Bean
    public Serde<String> stringSerde(KafkaProperties kafkaProperties) {
        Serde<String> stringSerde = Serdes.String();
        stringSerde.configure(properties(kafkaProperties), true);
        return stringSerde;
    }


    @Bean
    public Serde<Double> doubleSerde(KafkaProperties kafkaProperties) {
        Serde<Double> doubleSerde = Serdes.Double();
        doubleSerde.configure(properties(kafkaProperties), false);
        return doubleSerde;
    }


    @Bean
    public Serde<MovingAverage> movingAverageSerde(KafkaProperties kafkaProperties) {
        Serde<MovingAverage> movingAverageSerde = new JsonSerde<>(MovingAverage.class);
        movingAverageSerde.configure(properties(kafkaProperties), false);
        return movingAverageSerde;
    }

    private Map<String, Object> properties(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "ingest_to_1min");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, MetricEvent.class.getPackageName() + "," + MovingAverage.class.getPackageName());
        return props;
    }

    @Bean
    public ProducerFactory<String, MetricEvent> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // See https://kafka.apache.org/documentation/#producerconfigs for more properties
        return new DefaultKafkaProducerFactory<>(props);
    }


    @Bean
    public KafkaTemplate<String, MetricEvent> kafkaTemplate(ProducerFactory<String, MetricEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties kafkaProperties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        KafkaAdmin admin = new KafkaAdmin(configs);
        return admin;
    }

    @Bean
    public NewTopic ingestTopic() {
        return new NewTopic("ingest", 1, (short) 1);
    }
}
