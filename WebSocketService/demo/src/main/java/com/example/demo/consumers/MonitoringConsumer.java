package com.example.demo.consumers;

import com.example.demo.dtos.DeviceMeasurementDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MonitoringConsumer {
    private final SimpMessagingTemplate template;

    public MonitoringConsumer(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RabbitListener(queues = "#{monitoringIngestQueue.name}")
    public void consume(DeviceMeasurementDTO measurement) {
        template.convertAndSend("/topic/monitoring", measurement);
    }
}
