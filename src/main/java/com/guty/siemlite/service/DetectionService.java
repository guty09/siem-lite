package com.guty.siemlite.service;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 * DetectionService contains all SIEM correlation rules.
 *
 * Incoming SecurityEvents are analyzed here to determine
 * whether alerts should be generated.
 */
@Service
public class DetectionService {

    /*
     * Used to query historical events.
     */
    private final SecurityEventRepository securityEventRepository;

    /*
     * Used to store and query alerts.
     */
    private final AlertRepository alertRepository;

    public DetectionService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    /*
     * Main entry point.
     * Called whenever a new event is received.
     */
    public void analyze(SecurityEvent event) {

        // Define correlation window
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        /*
         * Failed logins may indicate brute force activity.
         */
        if ("FAILED_LOGIN".equals(event.getEventType())) {
            detectBruteForce(event, fiveMinutesAgo);
        }

        /*
         * Successful logins may indicate compromise if
         * multiple failures occurred beforehand.
         */
        if ("SUCCESSFUL_LOGIN".equals(event.getEventType())) {
            detectAccountCompromise(event, fiveMinutesAgo);
        }
    }

    /*
     * Rule:
     *
     * 5 FAILED_LOGIN
     * same IP
     * within 5 minutes
     *
     * =
     *
     * BRUTE_FORCE_ATTEMPT
     */
    private void detectBruteForce(SecurityEvent event, LocalDateTime fiveMinutesAgo) {

        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        fiveMinutesAgo
                );

        if (failedLogins.size() >= 5) {

            // Prevent duplicate alerts
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

    /*
     * Rule:
     *
     * 5 FAILED_LOGIN
     * same IP
     * same username
     * within 5 minutes
     *
     * +
     *
     * 1 SUCCESSFUL_LOGIN
     * same IP
     * same username
     *
     * =
     *
     * ACCOUNT_COMPROMISE
     */
    private void detectAccountCompromise(SecurityEvent event,
                                         LocalDateTime fiveMinutesAgo) {

        /*
         * Look for failed logins involving
         * the same IP and same username.
         */
        List<SecurityEvent> failedLogins =
                securityEventRepository
                        .findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
                                event.getSourceIp(),
                                event.getUsername(),
                                "FAILED_LOGIN",
                                fiveMinutesAgo
                        );

        if (failedLogins.size() >= 5) {

            // Prevent duplicate ACCOUNT_COMPROMISE alerts
            List<Alert> existingAlerts =
                    alertRepository.findBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "ACCOUNT_COMPROMISE"
                    );

            if (existingAlerts.isEmpty()) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "ACCOUNT_COMPROMISE",
                        "CRITICAL",
                        "Possible account compromise: successful login after multiple failed attempts from "
                                + event.getSourceIp(),
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }
}
