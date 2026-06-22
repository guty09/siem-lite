package com.guty.siemlite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * SIEM-Lite Main Application
 *
 * Entry point of the application.
 *
 * Spring Boot automatically performs:
 *
 * - Component scanning
 * - Dependency injection
 * - H2 database configuration
 * - REST controller discovery
 * - Service discovery
 * - Repository discovery
 * - Embedded web server startup
 *
 * Default port:
 *
 * http://localhost:8081
 *
 * Main architecture:
 *
 * POST /api/logs
 *         ↓
 * LogController
 *         ↓
 * LogParserService
 *         ↓
 * SecurityEvent
 *         ↓
 * SecurityEventRepository
 *         ↓
 * H2 Database
 *         ↓
 * DetectionService
 *         ↓
 * Alert
 *         ↓
 * AlertRepository
 *         ↓
 * H2 Database
 *         ↓
 * GET /api/events
 * GET /api/alerts
 */
@SpringBootApplication
public class SiemLiteApplication {

    /*
     * Application entry point.
     *
     * Spring Boot initializes:
     *
     * - Controllers
     * - Services
     * - Repositories
     * - H2 Database
     * - Embedded Tomcat server
     */
    public static void main(String[] args) {

        /*
         * Start the SIEM-Lite application.
         */
        SpringApplication.run(SiemLiteApplication.class, args);
    }
}