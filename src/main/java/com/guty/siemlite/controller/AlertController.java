package com.guty.siemlite.controller;

import com.guty.siemlite.audit.model.AuditAction;
import com.guty.siemlite.audit.service.AuditLogService;
import com.guty.siemlite.exception.AlertNotFoundException;
import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.specification.AlertSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Alert management and analyst workflow")
public class AlertController {

    private final AlertRepository alertRepository;
    private final AuditLogService auditLogService;

    public AlertController(AlertRepository alertRepository,
                           AuditLogService auditLogService) {
        this.alertRepository = alertRepository;
        this.auditLogService = auditLogService;
    }

    @Operation(summary = "Retrieve alerts with optional filtering")
    @GetMapping
    public Page<Alert> getAlerts(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignedTo,
            @RequestParam(required = false) String mitreTechnique,
            @RequestParam(required = false) Integer minRiskScore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Specification<Alert> specification = Specification
                .where(AlertSpecification.hasStatus(status))
                .and(AlertSpecification.hasAssignedTo(assignedTo))
                .and(AlertSpecification.hasMitreTechnique(mitreTechnique))
                .and(AlertSpecification.hasMinimumRiskScore(minRiskScore));

        return alertRepository.findAll(
                specification,
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "riskScore")
                )
        );
    }

    @Operation(summary = "Assign analyst to an alert")
    @PutMapping("/{id}/assign")
    public Alert assignAnalyst(@PathVariable Long id,
                               @RequestParam String analyst,
                               Authentication authentication) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));

        alert.setAssignedAnalyst(analyst);
        alert.setUpdatedAt(LocalDateTime.now());

        Alert savedAlert = alertRepository.save(alert);

        auditLogService.log(
                AuditAction.ALERT_ASSIGNED,
                getCurrentUsername(authentication),
                "Alert " + id + " assigned to analyst: " + analyst
        );

        return savedAlert;
    }

    @Operation(summary = "Change alert status")
    @PutMapping("/{id}/status")
    public Alert updateStatus(@PathVariable Long id,
                              @RequestParam String status,
                              Authentication authentication) {

        if (!status.equals("OPEN")
                && !status.equals("INVESTIGATING")
                && !status.equals("CLOSED")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid status. Use OPEN, INVESTIGATING, or CLOSED."
            );
        }

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));

        alert.setStatus(status);
        alert.setUpdatedAt(LocalDateTime.now());

        Alert savedAlert = alertRepository.save(alert);

        auditLogService.log(
                AuditAction.ALERT_STATUS_CHANGED,
                getCurrentUsername(authentication),
                "Alert " + id + " status changed to: " + status
        );

        return savedAlert;
    }

    @Operation(summary = "Add investigation notes to an alert")
    @PutMapping("/{id}/notes")
    public Alert updateNotes(@PathVariable Long id,
                             @RequestParam String note,
                             Authentication authentication) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));

        alert.setNotes(note);
        alert.setUpdatedAt(LocalDateTime.now());

        Alert savedAlert = alertRepository.save(alert);

        auditLogService.log(
                AuditAction.ALERT_NOTES_UPDATED,
                getCurrentUsername(authentication),
                "Alert " + id + " notes updated"
        );

        return savedAlert;
    }

    private String getCurrentUsername(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return "SYSTEM";
        }

        return authentication.getName();
    }
}