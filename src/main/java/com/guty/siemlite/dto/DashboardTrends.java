package com.guty.siemlite.dto;

/**
 * Data Transfer Object (DTO) representing dashboard trend analytics.
 *
 * <p>Provides aggregated alert and security event statistics across
 * common SOC reporting periods (24 hours, 7 days, and 30 days).
 * This object is returned by the dashboard trends REST endpoint
 * to support operational and security analytics.</p>
 */
public class DashboardTrends {

    /** Number of alerts generated during the last 24 hours. */
    private long alertsLast24Hours;

    /** Number of alerts generated during the last 7 days. */
    private long alertsLast7Days;

    /** Number of alerts generated during the last 30 days. */
    private long alertsLast30Days;

    /** Number of security events received during the last 24 hours. */
    private long eventsLast24Hours;

    /** Number of security events received during the last 7 days. */
    private long eventsLast7Days;

    /** Number of security events received during the last 30 days. */
    private long eventsLast30Days;

    /**
     * Default constructor required for serialization frameworks.
     */
    public DashboardTrends() {
    }

    /**
     * Creates a dashboard trends summary.
     *
     * @param alertsLast24Hours alerts generated during the last 24 hours
     * @param alertsLast7Days alerts generated during the last 7 days
     * @param alertsLast30Days alerts generated during the last 30 days
     * @param eventsLast24Hours security events received during the last 24 hours
     * @param eventsLast7Days security events received during the last 7 days
     * @param eventsLast30Days security events received during the last 30 days
     */
    public DashboardTrends(long alertsLast24Hours,
                           long alertsLast7Days,
                           long alertsLast30Days,
                           long eventsLast24Hours,
                           long eventsLast7Days,
                           long eventsLast30Days) {

        this.alertsLast24Hours = alertsLast24Hours;
        this.alertsLast7Days = alertsLast7Days;
        this.alertsLast30Days = alertsLast30Days;
        this.eventsLast24Hours = eventsLast24Hours;
        this.eventsLast7Days = eventsLast7Days;
        this.eventsLast30Days = eventsLast30Days;
    }

    /**
     * Returns the number of alerts generated during the last 24 hours.
     *
     * @return alert count
     */
    public long getAlertsLast24Hours() {
        return alertsLast24Hours;
    }

    /**
     * Sets the number of alerts generated during the last 24 hours.
     *
     * @param alertsLast24Hours alert count
     */
    public void setAlertsLast24Hours(long alertsLast24Hours) {
        this.alertsLast24Hours = alertsLast24Hours;
    }

    /**
     * Returns the number of alerts generated during the last 7 days.
     *
     * @return alert count
     */
    public long getAlertsLast7Days() {
        return alertsLast7Days;
    }

    /**
     * Sets the number of alerts generated during the last 7 days.
     *
     * @param alertsLast7Days alert count
     */
    public void setAlertsLast7Days(long alertsLast7Days) {
        this.alertsLast7Days = alertsLast7Days;
    }

    /**
     * Returns the number of alerts generated during the last 30 days.
     *
     * @return alert count
     */
    public long getAlertsLast30Days() {
        return alertsLast30Days;
    }

    /**
     * Sets the number of alerts generated during the last 30 days.
     *
     * @param alertsLast30Days alert count
     */
    public void setAlertsLast30Days(long alertsLast30Days) {
        this.alertsLast30Days = alertsLast30Days;
    }

    /**
     * Returns the number of security events received during the last 24 hours.
     *
     * @return event count
     */
    public long getEventsLast24Hours() {
        return eventsLast24Hours;
    }

    /**
     * Sets the number of security events received during the last 24 hours.
     *
     * @param eventsLast24Hours event count
     */
    public void setEventsLast24Hours(long eventsLast24Hours) {
        this.eventsLast24Hours = eventsLast24Hours;
    }

    /**
     * Returns the number of security events received during the last 7 days.
     *
     * @return event count
     */
    public long getEventsLast7Days() {
        return eventsLast7Days;
    }

    /**
     * Sets the number of security events received during the last 7 days.
     *
     * @param eventsLast7Days event count
     */
    public void setEventsLast7Days(long eventsLast7Days) {
        this.eventsLast7Days = eventsLast7Days;
    }

    /**
     * Returns the number of security events received during the last 30 days.
     *
     * @return event count
     */
    public long getEventsLast30Days() {
        return eventsLast30Days;
    }

    /**
     * Sets the number of security events received during the last 30 days.
     *
     * @param eventsLast30Days event count
     */
    public void setEventsLast30Days(long eventsLast30Days) {
        this.eventsLast30Days = eventsLast30Days;
    }
}
