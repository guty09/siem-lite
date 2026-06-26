package com.guty.siemlite.service;

import com.guty.siemlite.dto.DashboardSummary;
import com.guty.siemlite.dto.DashboardTrends;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service responsible for generating SOC dashboard statistics.
 *
 * <p>This service gathers event, alert, and risk data from the application's
 * repositories and builds dashboard DTOs that are returned by the REST API.</p>
 */
@Service
public class DashboardService {

    private final SecurityEventRepository securityEventRepository;
    private final AlertRepository alertRepository;

    /**
     * Creates a dashboard service with the repositories required for analytics.
     *
     * @param securityEventRepository repository for security event data
     * @param alertRepository repository for alert data
     */
    public DashboardService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    /**
     * Builds the main dashboard summary.
     *
     * <p>Includes operational metrics such as total events, alert counts,
     * severity counts, detection counts, and enterprise risk analytics.</p>
     *
     * @return dashboard summary metrics
     */
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

    /**
     * Builds dashboard trend analytics.
     *
     * <p>Calculates alert and security event volumes across common SOC
     * reporting windows: the last 24 hours, 7 days, and 30 days.</p>
     *
     * @return dashboard trend analytics
     */
    public DashboardTrends getDashboardTrends() {

        // Capture the current time once so every trend calculation
        // uses the same reference point.
        LocalDateTime now = LocalDateTime.now();

        return new DashboardTrends(
                alertRepository.countByCreatedAtAfter(now.minusHours(24)),
                alertRepository.countByCreatedAtAfter(now.minusDays(7)),
                alertRepository.countByCreatedAtAfter(now.minusDays(30)),
                securityEventRepository.countByTimestampAfter(now.minusHours(24)),
                securityEventRepository.countByTimestampAfter(now.minusDays(7)),
                securityEventRepository.countByTimestampAfter(now.minusDays(30))
        );
    }
}