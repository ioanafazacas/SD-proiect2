package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class SyncEventDTO {

    @JsonProperty("event_type")
    private String eventType; // "USER_CREATED", "DEVICE_CREATED", "USER_DELETED", "DEVICE_DELETED"

    @JsonProperty("entity_id")
    private UUID entityId;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("device_name")
    private String deviceName;

    @JsonProperty("max_consumption")
    private Double maxConsumption;

    public SyncEventDTO() {}

    public SyncEventDTO(String eventType, UUID entityId) {
        this.eventType = eventType;
        this.entityId = entityId;
    }

    // Getters and Setters
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Double getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(Double maxConsumption) {
        this.maxConsumption = maxConsumption;
    }
}