package com.example.demo.producers;

import com.example.demo.dtos.DeviceMeasurementDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class MonitoringProducer {

    private final RabbitTemplate rabbitTemplate;

    public MonitoringProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String queueName, DeviceMeasurementDTO message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

}
