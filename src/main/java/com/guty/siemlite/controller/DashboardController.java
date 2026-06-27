package com.guty.siemlite.controller;

import com.guty.siemlite.dto.DashboardSummary;
import com.guty.siemlite.dto.DashboardTrends;
import com.guty.siemlite.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guty.siemlite.dto.TopAlertType;
import com.guty.siemlite.dto.TopSourceIp;
import com.guty.siemlite.dto.TopUsername;
import com.guty.siemlite.dto.RiskDistribution;
import com.guty.siemlite.dto.MitreStatistic;
import com.guty.siemlite.dto.DashboardTimelinePoint;
import org.springframework.web.bind.annotation.RequestParam;
import com.guty.siemlite.dto.IocStatistics;

import java.util.List;

/**
 * REST controller that exposes SOC dashboard metrics and analytics.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Creates a dashboard controller with the dashboard service dependency.
     *
     * @param dashboardService service used to generate dashboard analytics
     */
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Returns high-level SIEM dashboard statistics.
     *
     * @return dashboard summary metrics
     */
    @GetMapping
    @Operation(summary = "Get dashboard summary metrics")
    public DashboardSummary getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }

    /**
     * Returns trend analytics for alerts and security events.
     *
     * <p>Includes alert and event counts for the last 24 hours,
     * 7 days, and 30 days.</p>
     *
     * @return dashboard trend analytics
     */
    @GetMapping("/trends")
    @Operation(summary = "Get dashboard trend analytics")
    public DashboardTrends getDashboardTrends() {
        return dashboardService.getDashboardTrends();
    }
    /**
     * Returns the most frequently generated alert types.
     *
     * <p>Provides alert frequency analytics to help SOC analysts
     * identify the most common detections in the environment.</p>
     *
     * @return alert types ordered by descending occurrence
     */
    @GetMapping("/top-alert-types")
    @Operation(summary = "Get top alert types")
    public List<TopAlertType> getTopAlertTypes() {
        return dashboardService.getTopAlertTypes();

    }
    /**
     * Returns the source IP addresses generating the highest number
     * of security events.
     *
     * <p>Provides visibility into the most active hosts observed by
     * the SIEM for SOC dashboard analytics.</p>
     *
     * @return source IP addresses ordered by descending event count
     */
    @GetMapping("/top-source-ips")
    @Operation(summary = "Get top source IP addresses")
    public List<TopSourceIp> getTopSourceIps() {
        return dashboardService.getTopSourceIps();
    }
    /**
     * Returns the usernames generating the highest number of
     * security events.
     *
     * <p>Provides visibility into the most active user accounts
     * observed by the SIEM for SOC dashboard analytics.</p>
     *
     * @return usernames ordered by descending event count
     */
    @GetMapping("/top-usernames")
    @Operation(summary = "Get top usernames")
    public List<TopUsername> getTopUsernames() {
        return dashboardService.getTopUsernames();
    }
    /**
     * Returns alert counts grouped by risk band.
     *
     * <p>Provides SOC dashboard visibility into how alerts are distributed
     * across low, medium, high, and critical risk levels.</p>
     *
     * @return alert counts grouped by risk band
     */
    @GetMapping("/risk-distribution")
    @Operation(summary = "Get alert risk distribution")
    public RiskDistribution getRiskDistribution() {
        return dashboardService.getRiskDistribution();
    }
    /**
     * Returns alert counts grouped by MITRE ATT&CK technique.
     *
     * <p>Provides SOC dashboard analytics showing which adversary
     * techniques are most frequently represented by generated alerts.</p>
     *
     * @return MITRE ATT&CK techniques ordered by descending alert count
     */
    @GetMapping("/mitre-statistics")
    @Operation(summary = "Get MITRE ATT&CK statistics")
    public List<MitreStatistic> getMitreStatistics() {
        return dashboardService.getMitreStatistics();
    }
    /**
     * Returns daily dashboard timeline analytics.
     *
     * <p>Provides event and alert counts grouped by day for SOC dashboard
     * charting and time-series analysis.</p>
     *
     * @param days number of days to include in the timeline
     * @return daily timeline points ordered from oldest to newest
     */
    @GetMapping("/timeline")
    @Operation(summary = "Get dashboard timeline analytics")
    public List<DashboardTimelinePoint> getDashboardTimeline(
            @RequestParam(defaultValue = "7") int days) {
        return dashboardService.getDashboardTimeline(days);
    }
    /**
     * Returns IOC alert statistics for threat intelligence dashboarding.
     *
     * <p>Provides visibility into alerts generated from known malicious
     * indicators of compromise.</p>
     *
     * @return IOC alert statistics
     */
    @GetMapping("/ioc-statistics")
    @Operation(summary = "Get IOC alert statistics")
    public IocStatistics getIocStatistics() {
        return dashboardService.getIocStatistics();
    }
}
