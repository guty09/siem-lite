package com.guty.siemlite.dto;

/**
 * Represents the number of alerts mapped to a specific MITRE ATT&CK technique.
 *
 * @param technique MITRE ATT&CK technique ID.
 * @param count     Number of alerts mapped to the technique.
 */
public record MitreStatistic(
        String technique,
        long count
) {
}
