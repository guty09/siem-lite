package com.guty.siemlite.controller;

import com.guty.siemlite.model.Incident;
import com.guty.siemlite.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Incident> assignIncident(
            @PathVariable Long id,
            @RequestParam String assignedAnalyst) {
        return ResponseEntity.ok(incidentService.assignIncident(id, assignedAnalyst));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Incident> updateIncidentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(incidentService.updateIncidentStatus(id, status));
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<Incident> updateIncidentNotes(
            @PathVariable Long id,
            @RequestParam String notes) {
        return ResponseEntity.ok(incidentService.updateIncidentNotes(id, notes));
    }
}
