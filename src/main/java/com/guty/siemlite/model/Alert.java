package com.guty.siemlite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String alertType;
    private String severity;
    private String message;
    private String sourceIp;

    public Alert() {
    }

    public Alert(LocalDateTime timestamp, String alertType, String severity, String message, String sourceIp) {
        this.timestamp = timestamp;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.sourceIp = sourceIp;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getAlertType() {
        return alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}