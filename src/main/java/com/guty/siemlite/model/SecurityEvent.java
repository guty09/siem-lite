package com.guty.siemlite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SecurityEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String sourceIp;
    private String username;
    private String eventType;
    private String rawLog;

    public SecurityEvent() {
    }

    public SecurityEvent(LocalDateTime timestamp, String sourceIp, String username, String eventType, String rawLog) {
        this.timestamp = timestamp;
        this.sourceIp = sourceIp;
        this.username = username;
        this.eventType = eventType;
        this.rawLog = rawLog;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getUsername() {
        return username;
    }

    public String getEventType() {
        return eventType;
    }

    public String getRawLog() {
        return rawLog;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setRawLog(String rawLog) {
        this.rawLog = rawLog;
    }
}
