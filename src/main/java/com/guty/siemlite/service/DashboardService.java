package com.guty.siemlite.service;

import com.guty.siemlite.dto.DashboardSummary;
import com.guty.siemlite.dto.DashboardTrends;
import com.guty.siemlite.dto.MitreStatistic;
import com.guty.siemlite.dto.RiskDistribution;
import com.guty.siemlite.dto.TopAlertType;
import com.guty.siemlite.dto.TopSourceIp;
import com.guty.siemlite.dto.TopUsername;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * Returns the most frequently generated alert types.
     *
     * <p>Used by the SOC dashboard to identify the most common detection
     * categories currently appearing in the environment.</p>
     *
     * @return alert types ordered by descending count
     */
    public List<TopAlertType> getTopAlertTypes() {
        return alertRepository.findTopAlertTypes();
    }

    /**
     * Returns the most active source IP addresses.
     *
     * <p>Used by the SOC dashboard to identify which source IP addresses
     * are generating the highest number of security events.</p>
     *
     * @return source IP addresses ordered by descending event count
     */
    public List<TopSourceIp> getTopSourceIps() {
        return securityEventRepository.findTopSourceIps();
    }

    /**
     * Returns the most active usernames.
     *
     * <p>Used by the SOC dashboard to identify which user accounts
     * appear most frequently in security events.</p>
     *
     * @return usernames ordered by descending event count
     */
    public List<TopUsername> getTopUsernames() {
        return securityEventRepository.findTopUsernames();
    }

    /**
     * Builds alert risk distribution analytics.
     *
     * <p>Groups alerts into enterprise SOC risk bands so the dashboard
     * can show how many alerts fall into low, medium, high, and critical
     * priority ranges.</p>
     *
     * @return alert counts grouped by risk band
     */
    public RiskDistribution getRiskDistribution() {
        return new RiskDistribution(
                alertRepository.countByRiskScoreLessThan(40),
                alertRepository.countByRiskScoreBetween(40, 69),
                alertRepository.countByRiskScoreBetween(70, 89),
                alertRepository.countByRiskScoreGreaterThanEqual(90)
        );
    }

    /**
     * Returns MITRE ATT&CK technique statistics for generated alerts.
     *
     * <p>Used by the SOC dashboard to show which adversary techniques
     * are most frequently represented across alert activity.</p>
     *
     * @return MITRE ATT&CK techniques ordered by descending alert count
     */
    public List<MitreStatistic> getMitreStatistics() {
        return alertRepository.findMitreStatistics();
    }
}