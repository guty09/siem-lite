package com.guty.siemlite.controller;

import com.guty.siemlite.dto.DashboardSummary;
import com.guty.siemlite.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes dashboard summary metrics.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Constructor-based dependency injection.
     */
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Returns high-level SIEM dashboard statistics.
     *
     * Endpoint:
     * GET /api/dashboard
     */
    @GetMapping
    public DashboardSummary getDashboardSummary() {
        return dashboardService.getDashboardSummary();
    }
}
