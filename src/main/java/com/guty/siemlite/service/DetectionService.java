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

@Service
public class DetectionService {

    private final SecurityEventRepository securityEventRepository;
    private final AlertRepository alertRepository;
    private final ThreatIntelligenceService threatIntelligenceService;

    public DetectionService(SecurityEventRepository securityEventRepository,
                            AlertRepository alertRepository,
                            ThreatIntelligenceService threatIntelligenceService) {

        this.securityEventRepository = securityEventRepository;
        this.alertRepository = alertRepository;
        this.threatIntelligenceService = threatIntelligenceService;
    }

    public void analyze(SecurityEvent event) {

        detectIocMatch(event);

        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);

        if ("FAILED_LOGIN".equals(event.getEventType())) {
            detectAggressiveBruteForce(event);
            detectBruteForce(event, fiveMinutesAgo);
            detectPasswordSpray(event, fiveMinutesAgo);
        }

        // rest of your existing code...


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
                        70,
                        "Possible brute force attack detected from " + event.getSourceIp(),
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }

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
                        95,
                        "Possible account compromise: successful login after multiple failed attempts from "
                                + event.getSourceIp(),
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }

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
                        75,
                        "Possible password spray attack detected from "
                                + event.getSourceIp()
                                + " against multiple usernames",
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }

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
                        90,
                        "Aggressive brute force attack detected from " + event.getSourceIp(),
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }

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
                        60,
                        "Possible port scan detected from " + event.getSourceIp()
                                + " targeting multiple destination ports",
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }

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
                        100,
                        "Possible privilege escalation after account compromise for user "
                                + event.getUsername()
                                + " from "
                                + event.getSourceIp(),
                        event.getSourceIp()
                );

                saveAlertWithMitre(alert);
            }
        }
    }
    private void detectIocMatch(SecurityEvent event) {

        boolean knownMaliciousIp =
                threatIntelligenceService.isKnownMaliciousIp(event.getSourceIp());

        if (!knownMaliciousIp) {
            return;
        }

        boolean iocMatchExists =
                alertRepository.existsBySourceIpAndAlertType(
                        event.getSourceIp(),
                        "IOC_MATCH"
                );

        if (iocMatchExists) {
            return;
        }

        Alert alert = new Alert(
                LocalDateTime.now(),
                "IOC_MATCH",
                "CRITICAL",
                95,
                "Threat intelligence match detected for known malicious IP "
                        + event.getSourceIp(),
                event.getSourceIp()
        );

        saveAlertWithMitre(alert);
    }

    private void saveAlertWithMitre(Alert alert) {
        addMitreMetadata(alert);
        alertRepository.save(alert);
    }

    private void addMitreMetadata(Alert alert) {

        switch (alert.getAlertType()) {

            case "BRUTE_FORCE_ATTEMPT" -> {
                alert.setMitreTechnique("T1110");
                alert.setMitreDescription("Brute Force");
            }

            case "ACCOUNT_COMPROMISE" -> {
                alert.setMitreTechnique("T1078");
                alert.setMitreDescription("Valid Accounts");
            }

            case "PASSWORD_SPRAY" -> {
                alert.setMitreTechnique("T1110.003");
                alert.setMitreDescription("Password Spraying");
            }

            case "BRUTE_FORCE_AGGRESSIVE" -> {
                alert.setMitreTechnique("T1110");
                alert.setMitreDescription("Brute Force");
            }

            case "PORT_SCAN" -> {
                alert.setMitreTechnique("T1046");
                alert.setMitreDescription("Network Service Discovery");
            }

            case "PRIVILEGE_ESCALATION" -> {
                alert.setMitreTechnique("T1068");
                alert.setMitreDescription("Exploitation for Privilege Escalation");
            }
            case "IOC_MATCH" -> {
                alert.setMitreTechnique("T1105");
                alert.setMitreDescription("Ingress Tool Transfer");
            }

            default -> {
                alert.setMitreTechnique("UNKNOWN");
                alert.setMitreDescription("Unknown Technique");
            }
        }
    }
}
