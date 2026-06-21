package com.guty.siemlite.controller;

import com.guty.siemlite.model.Alert;
import com.guty.siemlite.repository.AlertRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * REST controller responsible for alert-related endpoints.
 *
 * Clients can retrieve all generated alerts through:
 *
 * GET /api/alerts
 */
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    /*
     * Repository used to access the Alert table.
     */
    private final AlertRepository alertRepository;

    /*
     * Constructor injection.
     * Spring automatically provides AlertRepository.
     */
    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    /*
     * GET /api/alerts
     *
     * Returns every alert stored in the database.
     *
     * Example response:
     *
     * [
     *   {
     *     "alertType":"BRUTE_FORCE_ATTEMPT",
     *     "severity":"HIGH"
     *   }
     * ]
     */
    @GetMapping
    public List<Alert> getAlerts() {

        /*
         * Retrieve all alerts from H2 database.
         */
        return alertRepository.findAll();
    }
}
