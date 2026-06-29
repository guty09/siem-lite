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

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "Log ingestion and parsing")
public class LogController {

    private final LogParserService logParserService;
    private final SecurityEventRepository securityEventRepository;
    private final DetectionService detectionService;

    public LogController(LogParserService logParserService,
                         SecurityEventRepository securityEventRepository,
                         DetectionService detectionService) {
        this.logParserService = logParserService;
        this.securityEventRepository = securityEventRepository;
        this.detectionService = detectionService;
    }

    @Operation(summary = "Submit a single log")
    @PostMapping
    public SecurityEvent ingestLog(@RequestBody LogRequest request) {

        validateLogRequest(request);

        SecurityEvent event = logParserService.parseLog(request.getLogLine());

        if (event == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported log format: " + request.getLogLine()
            );
        }

        SecurityEvent savedEvent = securityEventRepository.save(event);

        detectionService.analyze(savedEvent);

        return savedEvent;
    }

    @Operation(summary = "Submit multiple logs")
    @PostMapping("/bulk")
    public List<SecurityEvent> ingestBulkLogs(@RequestBody List<LogRequest> requests) {

        if (requests == null || requests.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one log entry is required"
            );
        }

        return requests.stream()
                .map(request -> {
                    validateLogRequest(request);

                    SecurityEvent event = logParserService.parseLog(request.getLogLine());

                    if (event == null) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Unsupported log format: " + request.getLogLine()
                        );
                    }

                    SecurityEvent savedEvent = securityEventRepository.save(event);

                    detectionService.analyze(savedEvent);

                    return savedEvent;
                })
                .toList();
    }

    private void validateLogRequest(LogRequest request) {
        if (request == null || request.getLogLine() == null || request.getLogLine().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "logLine is required"
            );
        }
    }
}