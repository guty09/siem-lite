package com.guty.siemlite.service;

import com.guty.siemlite.dto.DashboardSummary;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for generating dashboard statistics.
 *
 * This service gathers information from the application's
 * repositories and builds a DashboardSummary object that
 * can be returned by the REST API.
 */
@Service
public class DashboardService {

    // Repository used to retrieve security event data
    private final SecurityEventRepository securityEventRepository;

    // Repository used to retrieve alert data
    private final AlertRepository alertRepository;

    /**
     * Constructor-based dependency injection.
     *
     * Spring automatically injects the required repositories.
     */
    public DashboardService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    /**
     * Builds a dashboard summary containing
     * high-level SIEM statistics.
     *
     * @return populated DashboardSummary object
     */
    public DashboardSummary getDashboardSummary() {

        DashboardSummary summary = new DashboardSummary();

        // Total number of security events
        summary.setTotalEvents(securityEventRepository.count());

        // Total number of generated alerts
        summary.setTotalAlerts(alertRepository.count());

        // Number of critical severity alerts
        summary.setCriticalAlerts(
                alertRepository.countBySeverity("CRITICAL"));

        // Number of high severity alerts
        summary.setHighAlerts(
                alertRepository.countBySeverity("HIGH"));

        // Total failed login events
        summary.setFailedLogins(
                securityEventRepository.countByEventType("FAILED_LOGIN"));

        // Total port scan events
        summary.setPortScans(
                securityEventRepository.countByEventType("PORT_SCAN"));

        // Total privilege escalation events
        summary.setPrivilegeEscalations(
                securityEventRepository.countByEventType("PRIV_ESC"));

        return summary;
    }
}
