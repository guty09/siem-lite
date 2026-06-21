package com.guty.siemlite.repository;

import com.guty.siemlite.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
 * Repository responsible for interacting
 * with the Alert table.
 *
 * JpaRepository automatically provides:
 *
 * save()
 * findAll()
 * findById()
 * delete()
 * etc.
 */
public interface AlertRepository extends JpaRepository<Alert, Long> {

    /*
     * Find alerts matching a source IP and alert type.
     *
     * Example:
     *
     * sourceIp = "192.168.1.60"
     * alertType = "BRUTE_FORCE_ATTEMPT"
     *
     * Used primarily to prevent duplicate alerts.
     *
     * Example:
     *
     * Without this check:
     *
     * FAILED_LOGIN #5
     * → BRUTE_FORCE_ATTEMPT
     *
     * FAILED_LOGIN #6
     * → BRUTE_FORCE_ATTEMPT
     *
     * FAILED_LOGIN #7
     * → BRUTE_FORCE_ATTEMPT
     *
     * The SIEM would flood the database with
     * duplicate alerts.
     *
     * By checking existing alerts first,
     * only one alert is generated.
     */
    List<Alert> findBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

}
