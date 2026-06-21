package com.guty.siemlite.controller;

import com.guty.siemlite.dto.LogRequest;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.service.DetectionService;
import org.springframework.web.bind.annotation.*;
import com.guty.siemlite.service.LogParserService;

/*
 * Main log ingestion controller.
 *
 * Receives raw log messages from clients,
 * parses them into SecurityEvent objects,
 * stores them in the database,
 * and sends them to the DetectionService
 * for correlation analysis.
 *
 * Endpoint:
 *
 * POST /api/logs
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    /*
     * Responsible for converting raw log lines
     * into structured SecurityEvent objects.
     */
    private final LogParserService logParserService;

    /*
     * Repository used to store SecurityEvents.
     */
    private final SecurityEventRepository securityEventRepository;

    /*
     * Responsible for correlation rules and
     * generating alerts.
     */
    private final DetectionService detectionService;

    /*
     * Constructor injection.
     *
     * Spring automatically provides these dependencies.
     */
    public LogController(LogParserService logParserService,
                         SecurityEventRepository securityEventRepository,
                         DetectionService detectionService) {

        this.logParserService = logParserService;
        this.securityEventRepository = securityEventRepository;
        this.detectionService = detectionService;
    }

    /*
     * POST /api/logs
     *
     * Receives raw log messages from clients.
     *
     * Example request:
     *
     * {
     *     "logLine":
     *     "Failed password for root from 192.168.1.60 port 50000 ssh2"
     * }
     *
     * Processing pipeline:
     *
     * Raw log
     *    ↓
     * LogParserService
     *    ↓
     * SecurityEvent
     *    ↓
     * Save to database
     *    ↓
     * DetectionService
     *    ↓
     * Generate alerts
     */
    @PostMapping
    public String ingestLog(@RequestBody LogRequest request) {

        /*
         * Convert raw text into a SecurityEvent object.
         */
        SecurityEvent event =
                logParserService.parseLog(request.getLogLine());

        /*
         * If parsing succeeded:
         */
        if (event != null) {

            /*
             * Store event in the H2 database.
             */
            securityEventRepository.save(event);

            /*
             * Run detection rules against the event.
             */
            detectionService.analyze(event);

            /*
             * Return success message.
             */
            return "Log stored successfully";
        }

        /*
         * Parsing failed because the log format
         * was not recognized.
         */
        return "Unable to parse log";
    }
}