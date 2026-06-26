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

    private final SecurityEventRepository securityEventRepository;
    private final AlertRepository alertRepository;

    public DashboardService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    public DashboardSummary getDashboardSummary() {

        DashboardSummary summary = new DashboardSummary();

        long totalAlerts = alertRepository.count();

        summary.setTotalEvents(securityEventRepository.count());
        summary.setTotalAlerts(totalAlerts);

        summary.setCriticalAlerts(
                alertRepository.countBySeverity("CRITICAL"));

        summary.setHighAlerts(
                alertRepository.countBySeverity("HIGH"));

        summary.setFailedLogins(
                securityEventRepository.countByEventType("FAILED_LOGIN"));

        summary.setPortScans(
                securityEventRepository.countByEventType("PORT_SCAN"));

        summary.setPrivilegeEscalations(
                securityEventRepository.countByEventType("PRIV_ESC"));

        Integer totalRisk = alertRepository.sumRiskScore();
        Integer highestRisk = alertRepository.maxRiskScore();

        int totalRiskScore = totalRisk != null ? totalRisk : 0;
        int highestRiskScore = highestRisk != null ? highestRisk : 0;

        double averageRiskScore = totalAlerts > 0
                ? (double) totalRiskScore / totalAlerts
                : 0.0;

        summary.setTotalRiskScore(totalRiskScore);
        summary.setAverageRiskScore(averageRiskScore);
        summary.setHighestRiskScore(highestRiskScore);

        summary.setCriticalRiskAlertCount(
                alertRepository.countByRiskScoreGreaterThanEqual(90));

        summary.setHighRiskAlertCount(
                alertRepository.countByRiskScoreGreaterThanEqual(70));

        return summary;
    }
}
