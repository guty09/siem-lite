package com.guty.siemlite.controller;

import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
     * Returns security events using pagination.
     *
     * Default behavior:
     * - Page: 0
     * - Size: 20
     * - Sorted by timestamp (newest first)
     *
     * Examples:
     *
     * GET /api/events
     * GET /api/events?page=0&size=10
     * GET /api/events?page=1&size=25
     * GET /api/events?sort=timestamp,desc
     */
    @Operation(summary = "Retrieve paginated security events")
    @GetMapping
    public Page<SecurityEvent> getEvents(

            @RequestParam(required = false)
            String eventType,

            @PageableDefault(
                    size = 20,
                    sort = "timestamp",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        /*
         * If an event type filter is supplied,
         * return only matching events.
         */
        if (eventType != null && !eventType.isBlank()) {
            return securityEventRepository.findByEventType(
                    eventType,
                    pageable);
        }

        /*
         * Otherwise return every event.
         */
        return securityEventRepository.findAll(pageable);
    }
}
