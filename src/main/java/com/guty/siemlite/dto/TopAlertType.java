package com.guty.siemlite.dto;

/**
 * Data Transfer Object (DTO) representing the number of alerts
 * generated for a specific alert type.
 *
 * <p>Used by the SOC dashboard to display the most frequently
 * generated alert types.</p>
 */
public class TopAlertType {

    /** Alert type (for example, BRUTE_FORCE_ATTEMPT or PORT_SCAN). */
    private String alertType;

    /** Number of alerts generated for this alert type. */
    private long count;

    /**
     * Default constructor required for serialization frameworks.
     */
    public TopAlertType() {
    }

    /**
     * Creates a top alert type summary.
     *
     * @param alertType alert type
     * @param count number of alerts
     */
    public TopAlertType(String alertType, long count) {
        this.alertType = alertType;
        this.count = count;
    }

    /**
     * Returns the alert type.
     *
     * @return alert type
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * Sets the alert type.
     *
     * @param alertType alert type
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * Returns the number of alerts generated for this alert type.
     *
     * @return alert count
     */
    public long getCount() {
        return count;
    }

    /**
     * Sets the number of alerts generated for this alert type.
     *
     * @param count alert count
     */
    public void setCount(long count) {
        this.count = count;
    }
}
