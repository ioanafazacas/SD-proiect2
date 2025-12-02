package com.example.demo.dtos;

import java.io.Serializable;
import java.util.UUID;

import java.io.Serializable;
import java.util.UUID;

public class UserSyncDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    private String operation; // CREATE, UPDATE, DELETE

    public UserSyncDTO() {
    }

    public UserSyncDTO(UUID userId, String operation) {

        this.userId = userId;
        this.operation = operation;
    }

    // Getters and Setters


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "DeviceSyncDTO{" +
                "userID=" + userId +'\'' +
                ", operation='" + operation +
                '}';
    }
}