package com.guty.siemlite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an Indicator of Compromise used for threat intelligence matching.
 */
@Entity
public class IndicatorOfCompromise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The IOC value, such as an IP address, domain, URL, or file hash.
     */
    @Column(name = "ioc_value", nullable = false, unique = true)
    private String value;

    /**
     * The type of indicator.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IocType type;

    /**
     * Source of the threat intelligence.
     */
    private String threatSource;

    /**
     * Confidence score from 0 to 100.
     */
    private Integer confidence;

    /**
     * Optional malware family associated with this IOC.
     */
    private String malwareFamily;

    /**
     * Optional campaign name associated with this IOC.
     */
    private String campaign;

    /**
     * Human-readable IOC description.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Whether this IOC is currently active for matching.
     */
    private boolean active = true;

    /**
     * Timestamp when this IOC was created.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    public IndicatorOfCompromise() {
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public IocType getType() {
        return type;
    }

    public String getThreatSource() {
        return threatSource;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public String getMalwareFamily() {
        return malwareFamily;
    }

    public String getCampaign() {
        return campaign;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(IocType type) {
        this.type = type;
    }

    public void setThreatSource(String threatSource) {
        this.threatSource = threatSource;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public void setMalwareFamily(String malwareFamily) {
        this.malwareFamily = malwareFamily;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
