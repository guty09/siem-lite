package com.guty.siemlite.repository;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.guty.siemlite.dto.TopSourceIp;
import org.springframework.data.jpa.repository.Query;
import com.guty.siemlite.dto.TopUsername;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository responsible for interacting with the SecurityEvent table.
 *
 * <p>Provides standard CRUD operations through {@link JpaRepository},
 * dynamic filtering through {@link JpaSpecificationExecutor}, and
 * derived query methods used by detection rules and dashboard analytics.</p>
 */
public interface SecurityEventRepository extends
        JpaRepository<SecurityEvent, Long>,
        JpaSpecificationExecutor<SecurityEvent> {

    /**
     * Finds all security events matching a source IP address and event type.
     *
     * @param sourceIp source IP address associated with the event
     * @param eventType event type to match
     * @return matching security events
     */
    List<SecurityEvent> findBySourceIpAndEventType(
            String sourceIp,
            String eventType);

    /**
     * Finds security events from a source IP and event type
     * that occurred after the specified timestamp.
     *
     * <p>Used by correlation rules such as brute-force and port-scan detection.</p>
     *
     * @param sourceIp source IP address associated with the event
     * @param eventType event type to match
     * @param timestamp lower bound timestamp
     * @return matching security events
     */
    List<SecurityEvent> findBySourceIpAndEventTypeAndTimestampAfter(
            String sourceIp,
            String eventType,
            LocalDateTime timestamp);

    /**
     * Finds security events matching a source IP address, username,
     * event type, and timestamp window.
     *
     * <p>Used for precise correlation rules such as account compromise detection.</p>
     *
     * @param sourceIp source IP address associated with the event
     * @param username username associated with the event
     * @param eventType event type to match
     * @param timestamp lower bound timestamp
     * @return matching security events
     */
    List<SecurityEvent> findBySourceIpAndUsernameAndEventTypeAndTimestampAfter(
            String sourceIp,
            String username,
            String eventType,
            LocalDateTime timestamp);

    /**
     * Counts the total number of security events matching a specific event type.
     *
     * @param eventType event type to count
     * @return number of matching security events
     */
    long countByEventType(String eventType);

    /**
     * Counts security events that occurred after the specified timestamp.
     *
     * <p>Used by dashboard analytics to measure event volume over
     * common SOC reporting windows.</p>
     *
     * @param timestamp lower bound timestamp
     * @return number of security events after the specified timestamp
     */
    long countByTimestampAfter(LocalDateTime timestamp);
    /**
     * Finds the most active source IP addresses based on total security event count.
     *
     * <p>Used by dashboard analytics to identify the IP addresses generating
     * the highest volume of security events.</p>
     *
     * @return source IP addresses ordered by event count in descending order
     */
    @Query("""
            SELECT new com.guty.siemlite.dto.TopSourceIp(e.sourceIp, COUNT(e))
            FROM SecurityEvent e
            WHERE e.sourceIp IS NOT NULL
            GROUP BY e.sourceIp
            ORDER BY COUNT(e) DESC
            """)
    List<TopSourceIp> findTopSourceIps();
    /**
     * Finds the most active usernames based on total security event count.
     *
     * <p>Used by dashboard analytics to identify which user accounts
     * appear most frequently in security events.</p>
     *
     * @return usernames ordered by event count in descending order
     */
    @Query("""
            SELECT new com.guty.siemlite.dto.TopUsername(e.username, COUNT(e))
            FROM SecurityEvent e
            WHERE e.username IS NOT NULL
            GROUP BY e.username
            ORDER BY COUNT(e) DESC
            """)
    List<TopUsername> findTopUsernames();
}