package com.tgracchus.metrics.config;

import com.tgracchus.metrics.Sink;
import com.tgracchus.metrics.TimescaleDBSink;
import com.tgracchus.metrics.events.MetricEvent;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public Sink<Windowed<String>,MetricEvent> metricEventSink(JdbcTemplate jdbcTemplate){
        return new TimescaleDBSink(jdbcTemplate);
    }
}
