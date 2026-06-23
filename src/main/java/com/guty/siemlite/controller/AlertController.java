package com.guty.siemlite.controller;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Alert management and analyst workflow")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Operation(summary = "Retrieve all alerts")
    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }

    @Operation(summary = "Assign analyst to an alert")
    @PutMapping("/{id}/assign")
    public Alert assignAnalyst(@PathVariable Long id,
                               @RequestParam String analyst) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        alert.setAssignedAnalyst(analyst);
        alert.setUpdatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    @Operation(summary = "Change alert status")
    @PutMapping("/{id}/status")
    public Alert updateStatus(@PathVariable Long id,
                              @RequestParam String status) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        if (!status.equals("OPEN")
                && !status.equals("INVESTIGATING")
                && !status.equals("CLOSED")) {
            throw new RuntimeException("Invalid status. Use OPEN, INVESTIGATING, or CLOSED.");
        }

        alert.setStatus(status);
        alert.setUpdatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    @Operation(summary = "Add investigation notes to an alert")
    @PutMapping("/{id}/notes")
    public Alert updateNotes(@PathVariable Long id,
                             @RequestParam String note) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        alert.setNotes(note);
        alert.setUpdatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }
}
