package com.guty.siemlite.dto;

/**
 * Represents an aggregated count of security events grouped by username.
 *
 * @param username the username associated with the security events
 * @param count the total number of events for the username
 */
public record TopUsername(String username, long count) {
}
