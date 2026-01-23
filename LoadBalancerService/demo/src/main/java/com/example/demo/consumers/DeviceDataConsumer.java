package com.example.demo.consumers;


import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dtos.DeviceMeasurementDTO;
import com.example.demo.producers.MonitoringProducer;
import com.example.demo.services.LoadBalancingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceDataConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceDataConsumer.class);

    private final LoadBalancingService loadBalancingService;
    private final MonitoringProducer monitoringProducer;


    @Autowired
    public DeviceDataConsumer(
            LoadBalancingService loadBalancingService,
            MonitoringProducer monitoringProducer) {
        this.loadBalancingService = loadBalancingService;
        this.monitoringProducer = monitoringProducer;
    }

    @RabbitListener(queues = "device-measurement-queue")
    //@RabbitListener(queues = RabbitMQConfig.DEVICE_DATA_QUEUE)
    public void consume(DeviceMeasurementDTO measurement) {
        try {
            LOGGER.info("📊 Received measurement: device={}, value={}, timestamp={}",
                    measurement.getDeviceId(),
                    measurement.getMeasurementValue(),
                    measurement.getTimestamp());

            loadBalancingService.processMeasurement(measurement);

        } catch (Exception e) {
            LOGGER.error("❌ Error processing device measurement: {}", e.getMessage(), e);
        }
        String targetQueue = loadBalancingService.selectQueue();
        monitoringProducer.send(targetQueue, measurement);

        System.out.println(
                "[LOAD BALANCER] Forwarded message to " + targetQueue
        );
    }


}