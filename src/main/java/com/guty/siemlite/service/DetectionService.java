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

    private final SecurityEventRepository securityEventRepository;
    private final AlertRepository alertRepository;

    public DetectionService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository) {
        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
    }

    /*
     * Main analysis method.
     *
     * Called every time a new SecurityEvent is created.
     */
    public void analyze(SecurityEvent event) {

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        if ("FAILED_LOGIN".equals(event.getEventType())) {
            detectAggressiveBruteForce(event);
            detectBruteForce(event, fiveMinutesAgo);
            detectPasswordSpray(event, fiveMinutesAgo);
        }

        if ("SUCCESSFUL_LOGIN".equals(event.getEventType())) {
            detectAccountCompromise(event, fiveMinutesAgo);
        }

        if ("ADMIN_COMMAND_EXECUTED".equals(event.getEventType())) {
            detectPrivilegeEscalation(event, fiveMinutesAgo);
        }

        if ("CONNECTION_ATTEMPT".equals(event.getEventType())) {
            detectPortScan(event);
        }
    }

    // ============================================================
    // RULE 1: BRUTE FORCE DETECTION
    // ============================================================

    private void detectBruteForce(SecurityEvent event,
                                  LocalDateTime fiveMinutesAgo) {

        boolean aggressiveExists =
                alertRepository.existsBySourceIpAndAlertType(
                        event.getSourceIp(),
                        "BRUTE_FORCE_AGGRESSIVE"
                );

        if (aggressiveExists) {
            return;
        }

        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        fiveMinutesAgo
                );

        if (failedLogins.size() >= 5) {

            boolean bruteForceExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "BRUTE_FORCE_ATTEMPT"
                    );

            if (!bruteForceExists) {

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

    // ============================================================
    // RULE 2: ACCOUNT COMPROMISE DETECTION
    // ============================================================

    private void detectAccountCompromise(SecurityEvent event,
                                         LocalDateTime fiveMinutesAgo) {

        List<SecurityEvent> failedLogins =
                securityEventRepository
                        .findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
                                event.getSourceIp(),
                                event.getUsername(),
                                "FAILED_LOGIN",
                                fiveMinutesAgo
                        );

        if (failedLogins.size() >= 5) {

            boolean compromiseExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "ACCOUNT_COMPROMISE"
                    );

            if (!compromiseExists) {

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

    // ============================================================
    // RULE 3: PASSWORD SPRAY DETECTION
    // ============================================================

    private void detectPasswordSpray(SecurityEvent event,
                                     LocalDateTime fiveMinutesAgo) {

        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        fiveMinutesAgo
                );

        Set<String> uniqueUsernames =
                failedLogins.stream()
                        .map(SecurityEvent::getUsername)
                        .collect(Collectors.toSet());

        if (uniqueUsernames.size() >= 5) {

            boolean passwordSprayExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "PASSWORD_SPRAY"
                    );

            if (!passwordSprayExists) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "PASSWORD_SPRAY",
                        "HIGH",
                        "Possible password spray attack detected from "
                                + event.getSourceIp()
                                + " against multiple usernames",
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }

    // ============================================================
    // RULE 4: AGGRESSIVE BRUTE FORCE DETECTION
    // ============================================================

    private void detectAggressiveBruteForce(SecurityEvent event) {

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        List<SecurityEvent> failedLogins =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "FAILED_LOGIN",
                        oneMinuteAgo
                );

        if (failedLogins.size() >= 20) {

            boolean aggressiveExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "BRUTE_FORCE_AGGRESSIVE"
                    );

            if (!aggressiveExists) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "BRUTE_FORCE_AGGRESSIVE",
                        "CRITICAL",
                        "Aggressive brute force attack detected from " + event.getSourceIp(),
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }

    // ============================================================
    // RULE 5: PORT SCAN DETECTION
    // ============================================================

    private void detectPortScan(SecurityEvent event) {

        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);

        List<SecurityEvent> connectionAttempts =
                securityEventRepository.findBySourceIpAndEventTypeAndTimestampAfter(
                        event.getSourceIp(),
                        "CONNECTION_ATTEMPT",
                        thirtySecondsAgo
                );

        Set<Integer> uniquePorts =
                connectionAttempts.stream()
                        .map(SecurityEvent::getDestinationPort)
                        .collect(Collectors.toSet());

        if (uniquePorts.size() >= 10) {

            boolean portScanExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "PORT_SCAN"
                    );

            if (!portScanExists) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "PORT_SCAN",
                        "HIGH",
                        "Possible port scan detected from " + event.getSourceIp()
                                + " targeting multiple destination ports",
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }

    // ============================================================
    // RULE 6: PRIVILEGE ESCALATION DETECTION
    // ============================================================

    /*
     * Rule:
     * 5 FAILED_LOGIN events from same IP and username within 5 minutes
     * followed by 1 SUCCESSFUL_LOGIN from same IP and username
     * followed by 1 ADMIN_COMMAND_EXECUTED from same IP and username
     * =
     * PRIVILEGE_ESCALATION
     * Severity: CRITICAL
     *
     * MITRE ATT&CK:
     * T1068 - Exploitation for Privilege Escalation
     */
    private void detectPrivilegeEscalation(SecurityEvent event,
                                           LocalDateTime fiveMinutesAgo) {

        List<SecurityEvent> failedLogins =
                securityEventRepository
                        .findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
                                event.getSourceIp(),
                                event.getUsername(),
                                "FAILED_LOGIN",
                                fiveMinutesAgo
                        );

        List<SecurityEvent> successfulLogins =
                securityEventRepository
                        .findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
                                event.getSourceIp(),
                                event.getUsername(),
                                "SUCCESSFUL_LOGIN",
                                fiveMinutesAgo
                        );

        if (failedLogins.size() >= 5 && !successfulLogins.isEmpty()) {

            boolean privilegeEscalationExists =
                    alertRepository.existsBySourceIpAndAlertType(
                            event.getSourceIp(),
                            "PRIVILEGE_ESCALATION"
                    );

            if (!privilegeEscalationExists) {

                Alert alert = new Alert(
                        LocalDateTime.now(),
                        "PRIVILEGE_ESCALATION",
                        "CRITICAL",
                        "Possible privilege escalation after account compromise for user "
                                + event.getUsername()
                                + " from "
                                + event.getSourceIp()
                                + " - MITRE ATT&CK T1068",
                        event.getSourceIp()
                );

                alertRepository.save(alert);
            }
        }
    }
}
