package com.guty.siemlite.controller;

import com.guty.siemlite.dto.LogRequest;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.service.DetectionService;
import com.guty.siemlite.service.LogParserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
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

    /*
     * Receives a single log line.
     *
     * POST /api/logs
     */
    @PostMapping
    public SecurityEvent ingestLog(@RequestBody LogRequest request) {

        SecurityEvent event = logParserService.parseLog(request.getLogLine());

        SecurityEvent savedEvent = securityEventRepository.save(event);

        detectionService.analyze(savedEvent);

        return savedEvent;
    }

    /*
     * Receives multiple log lines in one request.
     *
     * POST /api/logs/bulk
     */
    @PostMapping("/bulk")
    public List<SecurityEvent> ingestBulkLogs(@RequestBody List<LogRequest> requests) {

        return requests.stream()
                .map(request -> {

                    SecurityEvent event =
                            logParserService.parseLog(request.getLogLine());

                    SecurityEvent savedEvent =
                            securityEventRepository.save(event);

                    detectionService.analyze(savedEvent);

                    return savedEvent;

                })
                .toList();
    }
}