package com.guty.siemlite.dto;

/**
 * DTO representing threat intelligence alert statistics for the SOC dashboard.
 *
 * <p>This object summarizes IOC-driven alert activity so analysts can quickly
 * understand how often threat intelligence matches are occurring.</p>
 */
public class IocStatistics {

    private long totalIocAlerts;
    private long criticalIocAlerts;
    private long uniqueMaliciousIps;

    public IocStatistics(long totalIocAlerts,
                         long criticalIocAlerts,
                         long uniqueMaliciousIps) {
        this.totalIocAlerts = totalIocAlerts;
        this.criticalIocAlerts = criticalIocAlerts;
        this.uniqueMaliciousIps = uniqueMaliciousIps;
    }

    public long getTotalIocAlerts() {
        return totalIocAlerts;
    }

    public void setTotalIocAlerts(long totalIocAlerts) {
        this.totalIocAlerts = totalIocAlerts;
    }

    public long getCriticalIocAlerts() {
        return criticalIocAlerts;
    }

    public void setCriticalIocAlerts(long criticalIocAlerts) {
        this.criticalIocAlerts = criticalIocAlerts;
    }

    public long getUniqueMaliciousIps() {
        return uniqueMaliciousIps;
    }

    public void setUniqueMaliciousIps(long uniqueMaliciousIps) {
        this.uniqueMaliciousIps = uniqueMaliciousIps;
    }
}
