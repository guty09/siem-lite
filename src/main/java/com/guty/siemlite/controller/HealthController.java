package com.guty.siemlite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/*
 * Health controller.
 *
 * Used to verify that the application is running
 * and responding to HTTP requests.
 *
 * Endpoint:
 *
 * GET /api/health
 */
@RestController
@RequestMapping("/api/health")
@Tag(
        name = "Health",
        description = "Application health and status"
)
public class HealthController {

    /*
     * GET /api/health
     *
     * Returns information confirming that
     * the SIEM-Lite application is running.
     *
     * Example response:
     *
     * {
     *   "application":"SIEM-Lite",
     *   "status":"UP",
     *   "version":"1.0",
     *   "timestamp":"..."
     * }
     */
    @Operation(summary = "Retrieve application health status")
    @GetMapping
    public Map<String, Object> health() {

        /*
         * Return health-check information.
         */
        return Map.of(
                "application", "SIEM-Lite",
                "status", "UP",
                "version", "1.0",
                "timestamp", LocalDateTime.now()
        );
    }
}
