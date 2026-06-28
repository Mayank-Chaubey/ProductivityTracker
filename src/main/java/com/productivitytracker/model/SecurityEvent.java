package com.productivitytracker.model;

import java.sql.Timestamp;

public class SecurityEvent {

    private final String eventType;
    private final String details;
    private final String ipAddress;
    private final Timestamp createdAt;

    public SecurityEvent(String eventType, String details, String ipAddress, Timestamp createdAt) {
        this.eventType = eventType;
        this.details = details;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
