package com.guty.siemlite.dto;

import com.guty.siemlite.model.IocType;

/**
 * Request DTO for creating or updating Indicators of Compromise.
 */
public class IndicatorOfCompromiseRequest {

    /**
     * IOC value, such as an IP address, domain, URL, or file hash.
     */
    private String value;

    /**
     * IOC type.
     */
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
     * Optional malware family associated with the IOC.
     */
    private String malwareFamily;

    /**
     * Optional threat campaign associated with the IOC.
     */
    private String campaign;

    /**
     * Human-readable IOC description.
     */
    private String description;

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
}
