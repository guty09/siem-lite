package com.guty.siemlite.dto;

import com.guty.siemlite.model.IocType;

/**
 * Represents the result of comparing a security event against active
 * indicators of compromise.
 *
 * <p>This DTO intentionally separates threat intelligence match results from
 * the IndicatorOfCompromise persistence entity so service consumers do not
 * depend directly on database models.</p>
 */
public class ThreatMatch {

    private final boolean matched;
    private final String indicator;
    private final IocType type;
    private final Integer confidence;
    private final String description;

    /**
     * Creates a threat intelligence match result.
     *
     * @param matched whether an active IOC match was found
     * @param indicator the matched IOC value
     * @param type the IOC type
     * @param confidence IOC confidence score from 0 to 100
     * @param description IOC description or context
     */
    public ThreatMatch(boolean matched, String indicator, IocType type, Integer confidence, String description) {
        this.matched = matched;
        this.indicator = indicator;
        this.type = type;
        this.confidence = confidence;
        this.description = description;
    }

    public static ThreatMatch noMatch() {
        return new ThreatMatch(false, null, null, null, null);
    }

    public boolean isMatched() {
        return matched;
    }

    public String getIndicator() {
        return indicator;
    }

    public IocType getType() {
        return type;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public String getDescription() {
        return description;
    }
}
