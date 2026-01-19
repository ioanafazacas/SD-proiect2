package com.example.demo.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RabbitMQConfig {
    @Bean
    public Queue monitoringIngestQueue(
            @Value("${INSTANCE_ID:websocket}") String id) {
        return new Queue("monitoring_ingest_" + id, true);
    }

}

