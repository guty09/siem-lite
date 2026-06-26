package com.guty.siemlite.dto;

/**
 * Data Transfer Object (DTO) used to return
 * high-level dashboard statistics to the client.
 *
 * This object aggregates key SIEM metrics such as:
 * - Total security events
 * - Total alerts
 * - Critical and high severity alerts
 * - Failed login attempts
 * - Port scan detections
 * - Privilege escalation detections
 *
 * The DashboardService populates this DTO, and the
 * DashboardController returns it as JSON.
 */
public class DashboardSummary {

    // Total number of security events stored
    private long totalEvents;

    // Total number of alerts generated
    private long totalAlerts;

    // Number of critical severity alerts
    private long criticalAlerts;

    // Number of high severity alerts
    private long highAlerts;

    // Total failed login detections
    private long failedLogins;

    // Total port scan detections
    private long portScans;

    // Total privilege escalation detections
    private long privilegeEscalations;
    // Sum of all alert risk scores
    private int totalRiskScore;

    // Average risk score across all alerts
    private double averageRiskScore;

    // Highest risk score currently present
    private int highestRiskScore;

    // Number of alerts with risk score >= 90
    private long criticalRiskAlertCount;

    // Number of alerts with risk score >= 70
    private long highRiskAlertCount;

    /**
     * Default constructor required by Spring Boot
     * and JSON serialization libraries.
     */
    public DashboardSummary() {
    }

    // Returns the total number of security events
    public long getTotalEvents() {
        return totalEvents;
    }

    // Sets the total number of security events
    public void setTotalEvents(long totalEvents) {
        this.totalEvents = totalEvents;
    }

    // Returns the total number of alerts
    public long getTotalAlerts() {
        return totalAlerts;
    }

    // Sets the total number of alerts
    public void setTotalAlerts(long totalAlerts) {
        this.totalAlerts = totalAlerts;
    }

    // Returns the number of critical alerts
    public long getCriticalAlerts() {
        return criticalAlerts;
    }

    // Sets the number of critical alerts
    public void setCriticalAlerts(long criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    // Returns the number of high severity alerts
    public long getHighAlerts() {
        return highAlerts;
    }

    // Sets the number of high severity alerts
    public void setHighAlerts(long highAlerts) {
        this.highAlerts = highAlerts;
    }

    // Returns the number of failed login detections
    public long getFailedLogins() {
        return failedLogins;
    }

    // Sets the number of failed login detections
    public void setFailedLogins(long failedLogins) {
        this.failedLogins = failedLogins;
    }

    // Returns the number of port scan detections
    public long getPortScans() {
        return portScans;
    }

    // Sets the number of port scan detections
    public void setPortScans(long portScans) {
        this.portScans = portScans;
    }

    // Returns the number of privilege escalation detections
    public long getPrivilegeEscalations() {
        return privilegeEscalations;
    }

    // Sets the number of privilege escalation detections
    public void setPrivilegeEscalations(long privilegeEscalations) {
        this.privilegeEscalations = privilegeEscalations;
    }


    // Returns the total risk score
    public int getTotalRiskScore() {
        return totalRiskScore;
    }

    // Sets the total risk score
    public void setTotalRiskScore(int totalRiskScore) {
        this.totalRiskScore = totalRiskScore;
    }

    // Returns the average risk score
    public double getAverageRiskScore() {
        return averageRiskScore;
    }

    // Sets the average risk score
    public void setAverageRiskScore(double averageRiskScore) {
        this.averageRiskScore = averageRiskScore;
    }

    // Returns the highest risk score
    public int getHighestRiskScore() {
        return highestRiskScore;
    }

    // Sets the highest risk score
    public void setHighestRiskScore(int highestRiskScore) {
        this.highestRiskScore = highestRiskScore;
    }

    // Returns the number of critical-risk alerts
    public long getCriticalRiskAlertCount() {
        return criticalRiskAlertCount;
    }

    // Sets the number of critical-risk alerts
    public void setCriticalRiskAlertCount(long criticalRiskAlertCount) {
        this.criticalRiskAlertCount = criticalRiskAlertCount;
    }

    // Returns the number of high-risk alerts
    public long getHighRiskAlertCount() {
        return highRiskAlertCount;
    }

    // Sets the number of high-risk alerts
    public void setHighRiskAlertCount(long highRiskAlertCount) {
        this.highRiskAlertCount = highRiskAlertCount;
    }


}
