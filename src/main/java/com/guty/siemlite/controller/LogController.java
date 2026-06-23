package com.guty.siemlite.controller;

import com.guty.siemlite.dto.LogRequest;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.service.DetectionService;
import com.guty.siemlite.service.LogParserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
 * LogController is responsible for receiving log lines
 * through the REST API.
 *
 * Flow:
 *
 * POST /api/logs
 *        ↓
 * LogParserService
 *        ↓
 * SecurityEvent
 *        ↓
 * SecurityEventRepository
 *        ↓
 * DetectionService
 *
 * If the parser cannot recognize a log format,
 * the controller returns HTTP 400 Bad Request
 * instead of crashing the application.
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    /*
     * Dependencies.
     */
    private final LogParserService logParserService;
    private final SecurityEventRepository securityEventRepository;
    private final DetectionService detectionService;

    /*
     * Constructor injection.
     */
    public LogController(LogParserService logParserService,
                         SecurityEventRepository securityEventRepository,
                         DetectionService detectionService) {

        this.logParserService = logParserService;
        this.securityEventRepository = securityEventRepository;
        this.detectionService = detectionService;
    }

    /*
     * Receives a single log line.
     *
     * POST /api/logs
     *
     * Example:
     *
     * {
     *   "logLine":"Failed password for root from 192.168.1.100 port 50000 ssh2"
     * }
     */
    @PostMapping
    public SecurityEvent ingestLog(@RequestBody LogRequest request) {

        /*
         * Parse raw log into SecurityEvent.
         */
        SecurityEvent event =
                logParserService.parseLog(request.getLogLine());

        /*
         * If parser returns null, the log format
         * is unsupported.
         *
         * Prevent saving a null entity.
         */
        if (event == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported log format: " + request.getLogLine()
            );
        }

        /*
         * Save event.
         */
        SecurityEvent savedEvent =
                securityEventRepository.save(event);

        /*
         * Send event to correlation engine.
         */
        detectionService.analyze(savedEvent);

        /*
         * Return stored event.
         */
        return savedEvent;
    }

    /*
     * Receives multiple logs at once.
     *
     * POST /api/logs/bulk
     */
    @PostMapping("/bulk")
    public List<SecurityEvent> ingestBulkLogs(
            @RequestBody List<LogRequest> requests) {

        return requests.stream()
                .map(request -> {

                    /*
                     * Parse log line.
                     */
                    SecurityEvent event =
                            logParserService.parseLog(request.getLogLine());

                    /*
                     * Reject unsupported log formats.
                     */
                    if (event == null) {

                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Unsupported log format: "
                                        + request.getLogLine()
                        );
                    }

                    /*
                     * Save event.
                     */
                    SecurityEvent savedEvent =
                            securityEventRepository.save(event);

                    /*
                     * Analyze event.
                     */
                    detectionService.analyze(savedEvent);

                    /*
                     * Return saved event.
                     */
                    return savedEvent;

                })
                .toList();
    }
}