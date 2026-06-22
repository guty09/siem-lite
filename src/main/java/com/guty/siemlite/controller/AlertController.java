package com.guty.siemlite.controller;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/*
 * REST controller responsible for alert-related endpoints.
 *
 * Clients can retrieve and update generated alerts through:
 *
 * GET /api/alerts
 * PUT /api/alerts/{id}/assign?analyst=Guty
 * PUT /api/alerts/{id}/status?status=INVESTIGATING
 * PUT /api/alerts/{id}/notes?note=Confirmed investigation
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    /*
     * Repository used to access the Alert table.
     */
    private final AlertRepository alertRepository;

    /*
     * Constructor injection.
     * Spring automatically provides AlertRepository.
     */
    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /*
     * GET /api/alerts
     *
     * Returns every alert stored in the database.
     */
    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll();
    }

    /*
     * PUT /api/alerts/{id}/assign?analyst=Guty
     *
     * Assigns an analyst to an alert.
     */
    @PutMapping("/{id}/assign")
    public Alert assignAnalyst(@PathVariable Long id,
                               @RequestParam String analyst) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        alert.setAssignedAnalyst(analyst);
        alert.setUpdatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    /*
     * PUT /api/alerts/{id}/status?status=INVESTIGATING
     *
     * Updates the lifecycle status of an alert.
     *
     * Valid statuses:
     *
     * OPEN
     * INVESTIGATING
     * CLOSED
     */
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

    /*
     * PUT /api/alerts/{id}/notes?note=Confirmed brute force followed by privilege escalation
     *
     * Adds or replaces investigation notes on an alert.
     */
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
