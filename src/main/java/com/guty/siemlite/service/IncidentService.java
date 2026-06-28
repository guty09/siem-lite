package com.guty.siemlite.service;

import com.guty.siemlite.model.Incident;
import com.guty.siemlite.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public IncidentService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incident not found with id: " + id));
    }

    public Incident assignIncident(Long id, String assignedAnalyst) {
        Incident incident = getIncidentById(id);
        incident.setAssignedAnalyst(assignedAnalyst);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    public Incident updateIncidentStatus(Long id, String status) {
        Incident incident = getIncidentById(id);
        incident.setStatus(status);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }

    public Incident updateIncidentNotes(Long id, String notes) {
        Incident incident = getIncidentById(id);
        incident.setNotes(notes);
        incident.setUpdatedAt(LocalDateTime.now());
        return incidentRepository.save(incident);
    }
}
