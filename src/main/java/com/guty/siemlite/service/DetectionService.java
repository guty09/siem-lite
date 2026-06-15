package com.guty.siemlite.service;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DetectionService {

    private final SecurityEventRepository securityEventRepository;
    private final AlertRepository alertRepository;

    public DetectionService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    public void analyze(SecurityEvent event) {
        if (!"FAILED_LOGIN".equals(event.getEventType())) {
            return;
        }

        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventType(
                        event.getSourceIp(),
                        "FAILED_LOGIN"
                );

        if (failedLogins.size() >= 5) {

            List<Alert> existingAlerts =
                    alertRepository.findBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "BRUTE_FORCE_ATTEMPT"
                    );

            if (existingAlerts.isEmpty()) {
                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "BRUTE_FORCE_ATTEMPT",
                        "HIGH",
                        "Possible brute force attack detected from " + event.getSourceIp(),
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }
}
