package com.guty.siemlite.controller;

import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST controller responsible for security event endpoints.
 *
 * Events are the raw parsed records received by the SIEM.
 *
 * Example events:
 *
 * FAILED_LOGIN
 * SUCCESSFUL_LOGIN
 *
 * Endpoint:
 *
 * GET /api/events
 */
@RestController
@RequestMapping("/api/events")
@Tag(
        name = "Events",
        description = "Security events generated from logs"
)
public class EventController {

    /*
     * Repository used to interact with
     * the SecurityEvent table.
     */
    private final SecurityEventRepository securityEventRepository;

    /*
     * Constructor injection.
     *
     * Spring automatically supplies the repository.
     */
    public EventController(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    /*
     * GET /api/events
     *
     * Returns every security event stored
     * in the database.
     *
     * Example response:
     *
     * [
     *   {
     *      "sourceIp":"192.168.1.60",
     *      "username":"root",
     *      "eventType":"FAILED_LOGIN"
     *   }
     * ]
     */
    @Operation(summary = "Retrieve all security events")
    @GetMapping
    public List<SecurityEvent> getEvents() {

        /*
         * Retrieve all security events
         * from the H2 database.
         */
        return securityEventRepository.findAll();
    }
}
