package com.guty.siemlite.controller;

import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final SecurityEventRepository securityEventRepository;

    public EventController(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @GetMapping
    public List<SecurityEvent> getEvents() {
        return securityEventRepository.findAll();
    }
}
