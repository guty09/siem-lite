package com.guty.siemlite.dto;

/**
 * Represents alert distribution across enterprise risk bands.
 *
 * @param low number of alerts with low risk scores
 * @param medium number of alerts with medium risk scores
 * @param high number of alerts with high risk scores
 * @param critical number of alerts with critical risk scores
 */
public record RiskDistribution(
        long low,
        long medium,
        long high,
        long critical) {
}