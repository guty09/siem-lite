package com.guty.siemlite.controller;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }


    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }
}
