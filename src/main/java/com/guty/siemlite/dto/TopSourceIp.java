package com.guty.siemlite.dto;

/**
 * Represents an aggregated count of security events grouped by source IP address.
 *
 * @param sourceIp the source IP address associated with the events
 * @param count the total number of events for the source IP address
 */
public record TopSourceIp(String sourceIp, long count) {
}
