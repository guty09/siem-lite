package com.guty.siemlite.controller;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.specification.AlertSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Alert management and analyst workflow")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /**
     * Retrieves alerts with optional filtering and pagination.
     *
     * <p>Supported filters:
     * status, assignedTo, and mitreTechnique.</p>
     *
     * @param status optional alert status
     * @param assignedTo optional assigned analyst
     * @param mitreTechnique optional MITRE ATT&CK technique
     * @param pageable pagination information
     * @return a page of matching alerts
     */
    @Operation(summary = "Retrieve alerts with optional filtering")
    @GetMapping
    public Page<Alert> getAlerts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String mitreTechnique,
            Pageable pageable) {

        Specification<Alert> specification = Specification
                .where(AlertSpecification.hasStatus(status))
                .and(AlertSpecification.hasAssignedTo(assignedTo))
                .and(AlertSpecification.hasMitreTechnique(mitreTechnique));

        return alertRepository.findAll(specification, pageable);
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
