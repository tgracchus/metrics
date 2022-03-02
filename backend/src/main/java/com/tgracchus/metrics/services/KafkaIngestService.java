package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        ingestRequest.getMetrics().forEach(metric -> {
            metric.getPoints().forEach(ingestPoint -> {
                MetricEvent event = new MetricEvent(metric.getMetric(), ingestPoint.getValue(), ingestPoint.getTimestamp());
                kafkaTemplate.send(topic, null, ingestPoint.getTimestamp(), event.getMetric(), event);
            });

        });
    }
}
