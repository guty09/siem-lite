package com.guty.siemlite.repository;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;



/*
 * Repository responsible for interacting with the SecurityEvent table.
 *
 * JpaRepository automatically provides:
 * save()
 * findAll()
 * findById()
 * delete()
 * etc.
 */
public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long> {

    /*
     * Find all events matching a source IP and event type.
     *
     * Example:
     * sourceIp = "192.168.1.60"
     * eventType = "FAILED_LOGIN"
     *
     * Used for simple event lookups.
     */
    List<SecurityEvent> findBySourceIpAndEventType(
            String sourceIp,
            String eventType);

    /*
     * Find events from a source IP and event type
     * that occurred after a certain timestamp.
     *
     * Example:
     *
     * IP = 192.168.1.60
     * Event = FAILED_LOGIN
     * Timestamp = now - 5 minutes
     *
     * This allows time-based correlation.
     */
    List<SecurityEvent> findBySourceIpAndEventTypeAndTimestampAfter(
            String sourceIp,
            String eventType,
            LocalDateTime timestamp);

    /*
     * Find events matching:
     * - source IP
     * - username
     * - event type
     * - timestamp window
     *
     * Example:
     *
     * IP = 192.168.1.60
     * Username = root
     * Event = FAILED_LOGIN
     * Time = last 5 minutes
     *
     * Used for more precise correlation rules,
     * such as ACCOUNT_COMPROMISE detection.
     */
    List<SecurityEvent> findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
            String sourceIp,
            String username,
            String eventType,
            LocalDateTime timestamp);

    /*
     * Find events matching a specific event type
     * using pagination.
     *
     * Example:
     * eventType = FAILED_LOGIN
     *
     * Used by:
     * GET /api/events?eventType=FAILED_LOGIN
     */
    Page<SecurityEvent> findByEventType(
            String eventType,
            Pageable pageable);

    /*
     * Counts the total number of events
     * matching a specific event type.
     *
     * Example:
     * eventType = FAILED_LOGIN
     *
     * Spring Data JPA automatically generates
     * the SQL query based on the method name.
     */
    long countByEventType(String eventType);


}
