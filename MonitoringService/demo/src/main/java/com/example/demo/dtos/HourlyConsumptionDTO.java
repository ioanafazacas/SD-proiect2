package com.example.demo.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class HourlyConsumptionDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("device_id")
    private UUID deviceId;

    @JsonProperty("hour_timestamp")
    private LocalDateTime hourTimestamp;

    @JsonProperty("total_consumption")
    private double totalConsumption;

    @JsonProperty("max_consumption_threshold")
    private double maxConsumptionThreshold;

    @JsonProperty("exceeded")
    private boolean exceeded;

    public HourlyConsumptionDTO() {}

    public HourlyConsumptionDTO(UUID id, UUID deviceId, LocalDateTime hourTimestamp,
                                double totalConsumption, double maxConsumptionThreshold,
                                boolean exceeded) {
        this.id = id;
        this.deviceId = deviceId;
        this.hourTimestamp = hourTimestamp;
        this.totalConsumption = totalConsumption;
        this.maxConsumptionThreshold = maxConsumptionThreshold;
        this.exceeded = exceeded;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getHourTimestamp() {
        return hourTimestamp;
    }

    public void setHourTimestamp(LocalDateTime hourTimestamp) {
        this.hourTimestamp = hourTimestamp;
    }

    public double getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(double totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public double getMaxConsumptionThreshold() {
        return maxConsumptionThreshold;
    }

    public void setMaxConsumptionThreshold(double maxConsumptionThreshold) {
        this.maxConsumptionThreshold = maxConsumptionThreshold;
    }

    public boolean isExceeded() {
        return exceeded;
    }

    public void setExceeded(boolean exceeded) {
        this.exceeded = exceeded;
    }
}