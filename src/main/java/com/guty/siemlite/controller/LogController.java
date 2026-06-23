package com.guty.siemlite.controller;

import com.guty.siemlite.dto.LogRequest;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.service.DetectionService;
import com.guty.siemlite.service.LogParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
 * LogController is responsible for receiving log lines
 * through the REST API.
 */
@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "Log ingestion and parsing")
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
     */
    @Operation(summary = "Submit a single log")
    @PostMapping
    public SecurityEvent ingestLog(@RequestBody LogRequest request) {

        /*
         * Parse raw log into SecurityEvent.
         */
        SecurityEvent event =
                logParserService.parseLog(request.getLogLine());

        /*
         * Reject unsupported log formats.
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
    @Operation(summary = "Submit multiple logs")
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