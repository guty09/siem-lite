package com.guty.siemlite.service;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.AlertRepository;
import com.guty.siemlite.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * DetectionService is the SIEM correlation engine.
 *
 * It receives parsed SecurityEvent objects and checks them
 * against detection rules.
 *
 * If suspicious behavior is detected, it creates Alert records.
 */
@Service
public class DetectionService {

    /*
     * Repository used to search stored security events.
     */
    private final SecurityEventRepository securityEventRepository;

    /*
     * Repository used to save and search generated alerts.
     */
    private final AlertRepository alertRepository;

    /*
     * Constructor injection.
     *
     * Spring automatically provides the repositories.
     */
    public DetectionService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    /*
     * Main analysis method.
     *
     * This method is called every time a new SecurityEvent
     * is created from an incoming log.
     */
    public void analyze(SecurityEvent event) {

        /*
         * Define the correlation window.
         *
         * Current rule window:
         * last 5 minutes
         */
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        /*
         * FAILED_LOGIN events can indicate:
         *
         * - Brute force attack
         * - Password spray attack
         */
        if ("FAILED_LOGIN".equals(event.getEventType())) {
            detectBruteForce(event, fiveMinutesAgo);
            detectPasswordSpray(event, fiveMinutesAgo);
        }

        /*
         * SUCCESSFUL_LOGIN events can indicate compromise
         * if multiple failed attempts happened before success.
         */
        if ("SUCCESSFUL_LOGIN".equals(event.getEventType())) {
            detectAccountCompromise(event, fiveMinutesAgo);
        }
    }

    // ============================================================
    // RULE 1: BRUTE FORCE DETECTION
    // ============================================================

    /*
     * Rule:
     *
     * 5 FAILED_LOGIN events
     * from the same source IP
     * within 5 minutes
     *
     * =
     *
     * BRUTE_FORCE_ATTEMPT
     * Severity: HIGH
     */
    private void detectBruteForce(SecurityEvent event,
                                  LocalDateTime fiveMinutesAgo) {

        /*
         * Find all recent failed login events
         * from the same source IP.
         */
        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        fiveMinutesAgo
                );

        /*
         * Trigger the rule if 5 or more failed logins
         * were found in the time window.
         */
        if (failedLogins.size() >= 5) {

            /*
             * Check if this alert already exists.
             *
             * This prevents duplicate alerts from being created
             * every time another failed login arrives.
             */
            List<Alert> existingAlerts =
                    alertRepository.findBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "BRUTE_FORCE_ATTEMPT"
                    );

            /*
             * Only create a new alert if one does not already exist.
             */
            if (existingAlerts.isEmpty()) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "BRUTE_FORCE_ATTEMPT",
                        "HIGH",
                        "Possible brute force attack detected from " + event.getSourceIp(),
                        event.getSourceIp()
                );

                /*
                 * Save the alert to the database.
                 */
                alertRepository.save(alert);
            }
        }
    }

    // ============================================================
    // RULE 2: ACCOUNT COMPROMISE DETECTION
    // ============================================================

    /*
     * Rule:
     *
     * 5 FAILED_LOGIN events
     * from the same source IP
     * against the same username
     * within 5 minutes
     *
     * followed by:
     *
     * 1 SUCCESSFUL_LOGIN
     * from the same source IP
     * against the same username
     *
     * =
     *
     * ACCOUNT_COMPROMISE
     * Severity: CRITICAL
     */
    private void detectAccountCompromise(SecurityEvent event,
                                         LocalDateTime fiveMinutesAgo) {

        /*
         * Find recent failed logins that match:
         *
         * - same source IP
         * - same username
         * - event type FAILED_LOGIN
         * - within the last 5 minutes
         */
        List<SecurityEvent> failedLogins =
                securityEventRepository
                        .findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
                                event.getSourceIp(),
                                event.getUsername(),
                                "FAILED_LOGIN",
                                fiveMinutesAgo
                        );

        /*
         * If there were 5 or more failed logins before
         * this successful login, treat it as possible compromise.
         */
        if (failedLogins.size() >= 5) {

            /*
             * Prevent duplicate ACCOUNT_COMPROMISE alerts
             * for the same source IP.
             */
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

                /*
                 * Save the alert to the database.
                 */
                alertRepository.save(alert);
            }
        }
    }

    // ============================================================
    // RULE 3: PASSWORD SPRAY DETECTION
    // ============================================================

    /*
     * Rule:
     *
     * 1 source IP
     * targets 5 different usernames
     * with FAILED_LOGIN events
     * within 5 minutes
     *
     * =
     *
     * PASSWORD_SPRAY
     * Severity: HIGH
     *
     * Example:
     *
     * 192.168.1.90 -> admin
     * 192.168.1.90 -> root
     * 192.168.1.90 -> john
     * 192.168.1.90 -> maria
     * 192.168.1.90 -> test
     */
    private void detectPasswordSpray(SecurityEvent event,
                                     LocalDateTime fiveMinutesAgo) {

        /*
         * Find recent failed login events
         * from the same source IP.
         */
        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        fiveMinutesAgo
                );

        /*
         * Extract usernames from the failed login events.
         *
         * A Set stores only unique values, so duplicate usernames
         * are counted once.
         */
        Set<String> uniqueUsernames =
                failedLogins.stream()
                        .map(SecurityEvent::getUsername)
                        .collect(Collectors.toSet());

        /*
         * Trigger the rule if the same IP has targeted
         * 5 or more different usernames.
         */
        if (uniqueUsernames.size() >= 5) {

            /*
             * Prevent duplicate PASSWORD_SPRAY alerts
             * for the same source IP.
             */
            List<Alert> existingAlerts =
                    alertRepository.findBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "PASSWORD_SPRAY"
                    );

            if (existingAlerts.isEmpty()) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "PASSWORD_SPRAY",
                        "HIGH",
                        "Possible password spray attack detected from "
                                + event.getSourceIp()
                                + " against multiple usernames",
                        event.getSourceIp()
                );

                /*
                 * Save the alert to the database.
                 */
                alertRepository.save(alert);
            }
        }
    }
}
