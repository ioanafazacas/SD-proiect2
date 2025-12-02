package com.example.demo.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class DeviceMeasurementDTO {

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("device_id")
    private UUID deviceId;

    @JsonProperty("measurement_value")
    private double measurementValue;

    public DeviceMeasurementDTO() {}

    public DeviceMeasurementDTO(long timestamp, UUID deviceId, double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    // Getters and Setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }
}