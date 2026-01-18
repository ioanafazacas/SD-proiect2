package com.example.demo.consumers;


import com.example.demo.dtos.DeviceMeasurementDTO;
import com.example.demo.services.MonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataConsumer.class);

    private final MonitoringService monitoringService;

    @Autowired
    public DeviceDataConsumer(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
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
}