package com.guty.siemlite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * Represents a parsed security event stored in the database.
 *
 * Examples:
 *
 * FAILED_LOGIN
 * SUCCESSFUL_LOGIN
 * CONNECTION_ATTEMPT
 *
 * Raw logs are transformed into SecurityEvent objects
 * before correlation rules analyze them.
 */
@Entity
public class SecurityEvent {

    /*
     * Primary key generated automatically by H2.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * When the event occurred.
     */
    private LocalDateTime timestamp;

    /*
     * Source IP address that generated the event.
     *
     * Example:
     * 192.168.1.60
     */
    private String sourceIp;

    /*
     * Username involved in the event.
     *
     * Examples:
     * root
     * admin
     * jsmith
     *
     * May be null for network events.
     */
    private String username;

    /*
     * Parsed event type.
     *
     * Examples:
     * FAILED_LOGIN
     * SUCCESSFUL_LOGIN
     * CONNECTION_ATTEMPT
     */
    private String eventType;

    /*
     * Original raw log line received by the SIEM.
     */
    private String rawLog;

    /*
     * Destination port involved in a network connection.
     *
     * Examples:
     * 22
     * 80
     * 443
     *
     * Used for port scan detection.
     * May be null for authentication events.
     */
    private Integer destinationPort;

    /*
     * Default constructor required by JPA.
     */
    public SecurityEvent() {
    }

    /*
     * Constructor used when creating events.
     */
    public SecurityEvent(LocalDateTime timestamp,
                         String sourceIp,
                         String username,
                         String eventType,
                         String rawLog,
                         Integer destinationPort) {

        this.timestamp = timestamp;
        this.sourceIp = sourceIp;
        this.username = username;
        this.eventType = eventType;
        this.rawLog = rawLog;
        this.destinationPort = destinationPort;
    }

    // ==================================================
    // GETTERS
    // ==================================================

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getUsername() {
        return username;
    }

    public String getEventType() {
        return eventType;
    }

    public String getRawLog() {
        return rawLog;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    // ==================================================
    // SETTERS
    // ==================================================

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setRawLog(String rawLog) {
        this.rawLog = rawLog;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
    }
}
