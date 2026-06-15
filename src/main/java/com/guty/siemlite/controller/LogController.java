package com.guty.siemlite.controller;

import com.guty.siemlite.dto.LogRequest;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.service.DetectionService;
import org.springframework.web.bind.annotation.*;
import com.guty.siemlite.service.LogParserService;

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

    @PostMapping
    public String ingestLog(@RequestBody LogRequest request) {

        SecurityEvent event =
                logParserService.parseLog(request.getLogLine());

        if (event != null) {
            securityEventRepository.save(event);
            detectionService.analyze(event);
            return "Log stored successfully";
        }

        return "Unable to parse log";
    }
}