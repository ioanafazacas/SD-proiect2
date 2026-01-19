package com.example.demo.consumers;


import com.example.demo.dtos.DeviceMeasurementDTO;
import com.example.demo.services.MonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeviceDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataConsumer.class);

    private final MonitoringService monitoringService;
    private final RestTemplate restTemplate;

    @Autowired
    public DeviceDataConsumer(MonitoringService monitoringService, RestTemplate restTemplate) {
        this.monitoringService = monitoringService;
        this.restTemplate = restTemplate;
    }

    @RabbitListener(
            queues = "#{monitoringIngestQueue.name}"
    )
    public void consumeDeviceData(DeviceMeasurementDTO measurement) {
        try {
            LOGGER.info("📊 Received measurement: device={}, value={}, timestamp={}",
                    measurement.getDeviceId(),
                    measurement.getMeasurementValue(),
                    measurement.getTimestamp());
            System.out.println(
                    "[MONITORING] Consumed message from " +
                            System.getenv("INSTANCE_ID")
            );
            monitoringService.processMeasurement(measurement);

        } catch (Exception e) {
            LOGGER.error("❌ Error processing device measurement: {}", e.getMessage(), e);
        }
    }

    //am nevoie de o alta coada aici si vceva nu e bine la logica
    @RabbitListener(queues = "device-sync-queue")
    public void consumeSync(DeviceMeasurementDTO dto) {

        // 1️⃣ Salvează în DB
        monitoringService.processMeasurement(dto);

        // 2️⃣ Trimite către WebSocketService
        restTemplate.postForObject(
                "http://websocket-service:8085/push/measurement",
                dto,
                Void.class
        );
    }
}