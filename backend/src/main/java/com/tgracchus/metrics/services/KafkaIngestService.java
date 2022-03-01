package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        long timestamp = Instant.now().toEpochMilli();
        ingestRequest.getMetrics().forEach(
                metricDto -> kafkaTemplate.send(topic, null, timestamp, metricDto.getKey(), new MetricEvent(metricDto.getValue(), timestamp)));

    }
}
