package com.guty.siemlite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

/**
 * Represents a correlated security incident created from related alerts.
 *
 * <p>An incident is a higher-level SOC object than an alert. Alerts represent
 * individual detections, while incidents group related activity into a single
 * investigation unit.</p>
 */
@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String incidentType;

    private String severity;

    private Integer riskScore;

    @Column(length = 2000)
    private String summary;

    private String sourceIp;

    private String status;

    private String assignedAnalyst;

    @Column(length = 2000)
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Incident() {
    }

    public Incident(LocalDateTime timestamp,
                    String incidentType,
                    String severity,
                    Integer riskScore,
                    String summary,
                    String sourceIp) {

        this.timestamp = timestamp;
        this.incidentType = incidentType;
        this.severity = severity;
        this.riskScore = riskScore;
        this.summary = summary;
        this.sourceIp = sourceIp;

        this.status = "OPEN";
        this.assignedAnalyst = null;
        this.notes = null;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getSeverity() {
        return severity;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public String getSummary() {
        return summary;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedAnalyst() {
        return assignedAnalyst;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssignedAnalyst(String assignedAnalyst) {
        this.assignedAnalyst = assignedAnalyst;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
