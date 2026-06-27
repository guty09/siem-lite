package com.guty.siemlite.service;

import com.guty.siemlite.dto.ThreatMatch;
import com.guty.siemlite.model.IndicatorOfCompromise;
import com.guty.siemlite.model.IocType;
import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.IndicatorOfCompromiseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service responsible for matching security events against
 * active Indicators of Compromise.
 */
@Service
public class ThreatIntelligenceService {

    private final IndicatorOfCompromiseRepository repository;

    /**
     * Creates a new ThreatIntelligenceService.
     *
     * @param repository IOC repository
     */
    public ThreatIntelligenceService(IndicatorOfCompromiseRepository repository) {
        this.repository = repository;
    }

    /**
     * Analyzes a security event for active IOC matches.
     *
     * @param event security event
     * @return threat match result
     */
    public ThreatMatch analyze(SecurityEvent event) {
        if (event == null) {
            return ThreatMatch.noMatch();
        }

        Optional<IndicatorOfCompromise> ipMatch = findMatchingIp(event.getSourceIp());

        if (ipMatch.isPresent()) {
            return toThreatMatch(ipMatch.get());
        }

        return ThreatMatch.noMatch();
    }

    /**
     * Finds an active IP IOC.
     *
     * @param ip source IP
     * @return matching IOC
     */
    public Optional<IndicatorOfCompromise> findMatchingIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return Optional.empty();
        }

        return repository.findByValueAndTypeAndActiveTrue(ip.trim(), IocType.IP);
    }
    /**
     * Determines whether the supplied IP address matches an
     * active Indicator of Compromise.
     *
     * @param ip source IP address
     * @return true if an active IOC exists
     */
    public boolean isKnownMaliciousIp(String ip) {
        return findMatchingIp(ip).isPresent();
    }

    /**
     * Converts an IOC entity into a ThreatMatch DTO.
     *
     * @param ioc matched IOC
     * @return threat match DTO
     */
    private ThreatMatch toThreatMatch(IndicatorOfCompromise ioc) {
        return new ThreatMatch(
                true,
                ioc.getValue(),
                ioc.getType(),
                ioc.getConfidence(),
                ioc.getDescription()
        );
    }
}
