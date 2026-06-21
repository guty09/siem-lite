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
     * Example:
     * root
     * admin
     * jsmith
     */
    private String username;

    /*
     * Parsed event type.
     *
     * Examples:
     * FAILED_LOGIN
     * SUCCESSFUL_LOGIN
     */
    private String eventType;

    /*
     * Original raw log line received by the SIEM.
     */
    private String rawLog;

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
                         String rawLog) {

        this.timestamp = timestamp;
        this.sourceIp = sourceIp;
        this.username = username;
        this.eventType = eventType;
        this.rawLog = rawLog;
    }

    // ===== GETTERS =====

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

    // ===== SETTERS =====

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
}
