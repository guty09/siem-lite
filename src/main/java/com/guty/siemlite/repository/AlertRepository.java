package com.guty.siemlite.repository;

import com.guty.siemlite.dto.MitreStatistic;
import com.guty.siemlite.dto.TopAlertType;
import com.guty.siemlite.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository responsible for interacting with the Alert table.
 *
 * <p>Provides CRUD operations, dynamic filtering, detection support,
 * and dashboard analytics queries.</p>
 */
public interface AlertRepository extends
        JpaRepository<Alert, Long>,
        JpaSpecificationExecutor<Alert> {

    /**
     * Finds alerts matching a source IP address and alert type.
     *
     * @param sourceIp source IP address
     * @param alertType alert type
     * @return matching alerts
     */
    List<Alert> findBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

    /**
     * Determines whether a matching alert already exists.
     *
     * @param sourceIp source IP address
     * @param alertType alert type
     * @return true if a matching alert exists
     */
    boolean existsBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

    /**
     * Counts alerts by severity.
     *
     * @param severity severity level
     * @return number of matching alerts
     */
    long countBySeverity(String severity);

    /**
     * Counts alerts created after a timestamp.
     *
     * @param time lower bound timestamp
     * @return number of matching alerts
     */
    long countByCreatedAtAfter(LocalDateTime time);
    /**
     * Counts alerts created within a timestamp range.
     *
     * <p>Used by dashboard timeline analytics to calculate daily
     * alert volume.</p>
     *
     * @param start inclusive start timestamp
     * @param end exclusive end timestamp
     * @return number of alerts created within the timestamp range
     */
    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
            LocalDateTime start,
            LocalDateTime end);

    /**
     * Counts alerts with risk scores below the supplied value.
     *
     * @param riskScore exclusive upper bound
     * @return number of matching alerts
     */
    long countByRiskScoreLessThan(Integer riskScore);

    /**
     * Counts alerts with risk scores between the supplied bounds.
     *
     * @param minRiskScore inclusive lower bound
     * @param maxRiskScore inclusive upper bound
     * @return number of matching alerts
     */
    long countByRiskScoreBetween(
            Integer minRiskScore,
            Integer maxRiskScore);

    /**
     * Counts alerts with risk scores greater than or equal
     * to the supplied value.
     *
     * @param riskScore inclusive lower bound
     * @return number of matching alerts
     */
    long countByRiskScoreGreaterThanEqual(Integer riskScore);

    /**
     * Calculates the total risk score across all alerts.
     *
     * @return total risk score, or null if no alerts exist
     */
    @Query("SELECT SUM(a.riskScore) FROM Alert a")
    Integer sumRiskScore();

    /**
     * Finds the highest risk score.
     *
     * @return highest risk score, or null if no alerts exist
     */
    @Query("SELECT MAX(a.riskScore) FROM Alert a")
    Integer maxRiskScore();

    /**
     * Returns alert types ordered by occurrence.
     *
     * @return alert types ordered by descending count
     */
    @Query("""
            SELECT new com.guty.siemlite.dto.TopAlertType(
                a.alertType,
                COUNT(a)
            )
            FROM Alert a
            GROUP BY a.alertType
            ORDER BY COUNT(a) DESC
            """)
    List<TopAlertType> findTopAlertTypes();

    /**
     * Returns MITRE ATT&CK techniques ordered by alert occurrence.
     *
     * <p>This query supports dashboard analytics by showing which MITRE
     * techniques appear most frequently across generated alerts.</p>
     *
     * @return MITRE technique statistics ordered by descending count
     */
    @Query("""
            SELECT new com.guty.siemlite.dto.MitreStatistic(
                a.mitreTechnique,
                COUNT(a)
            )
            FROM Alert a
            WHERE a.mitreTechnique IS NOT NULL
            GROUP BY a.mitreTechnique
            ORDER BY COUNT(a) DESC
            """)
    List<MitreStatistic> findMitreStatistics();
}