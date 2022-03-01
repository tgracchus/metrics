package com.tgracchus.metrics.service;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaIngestService implements IngestService {

    private final KafkaTemplate<String, MetricEvent> kafkaTemplate;
    private final String topic;

    @Autowired
    public KafkaIngestService(KafkaTemplate<String, MetricEvent> kafkaTemplate, NewTopic topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic.name();
    }


    @Override
    public void ingest(IngestRequest ingestRequest) {
        ingestRequest.getMetrics().forEach(metricDto -> {
            long timestamp = Instant.now().toEpochMilli();
            kafkaTemplate.send(topic, null, timestamp, metricDto.getKey(), new MetricEvent(metricDto.getValue(), timestamp));
        });

    }
}
