package com.guty.siemlite.repository;

import com.guty.siemlite.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository responsible for interacting with the Alert table.
 *
 * <p>Provides standard CRUD operations through {@link JpaRepository},
 * dynamic filtering support through {@link JpaSpecificationExecutor},
 * and custom query methods used by the detection engine and dashboard.</p>
 */
public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

    /**
     * Finds alerts matching a source IP address and alert type.
     *
     * <p>Used by the detection engine to prevent duplicate alerts.</p>
     *
     * @param sourceIp the source IP address associated with the alert
     * @param alertType the alert type to match
     * @return matching alerts
     */
    List<Alert> findBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

    /**
     * Checks whether an alert already exists for a source IP address and alert type.
     *
     * <p>Used for duplicate prevention, alert hierarchy, and alert lifecycle logic.</p>
     *
     * @param sourceIp the source IP address associated with the alert
     * @param alertType the alert type to match
     * @return true if a matching alert exists, otherwise false
     */
    boolean existsBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

    /**
     * Counts alerts matching a specific severity level.
     *
     * <p>Used by the dashboard to display alert counts by severity.</p>
     *
     * @param severity the severity level to count
     * @return the number of alerts with the specified severity
     */
    long countBySeverity(String severity);

    /**
     * Counts alerts created after the specified timestamp.
     *
     * <p>Used by the SOC dashboard to calculate alert trends over
     * common reporting periods such as the last 24 hours,
     * 7 days, and 30 days.</p>
     *
     * @param time lower bound timestamp
     * @return number of alerts created after the specified time
     */
    long countByCreatedAtAfter(LocalDateTime time);

    /**
     * Calculates the total risk score across all alerts.
     *
     * @return total risk score, or null if no alerts exist
     */
    @Query("SELECT SUM(a.riskScore) FROM Alert a")
    Integer sumRiskScore();

    /**
     * Finds the highest risk score across all alerts.
     *
     * @return highest risk score, or null if no alerts exist
     */
    @Query("SELECT MAX(a.riskScore) FROM Alert a")
    Integer maxRiskScore();

    /**
     * Counts alerts with a risk score greater than or equal to the supplied value.
     *
     * @param riskScore minimum risk score
     * @return number of alerts matching or exceeding the risk score
     */
    long countByRiskScoreGreaterThanEqual(Integer riskScore);
}