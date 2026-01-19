package com.example.demo.controllers;

import com.example.demo.dtos.DeviceMeasurementDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/push")
public class MonitoringPushController {

    private final SimpMessagingTemplate messagingTemplate;

    public MonitoringPushController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/measurement")
    public void pushMeasurement(@RequestBody DeviceMeasurementDTO measurement) {
        messagingTemplate.convertAndSend(
                "/topic/monitoring",
                measurement
        );
    }
}
