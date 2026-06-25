package com.guty.siemlite.controller;

import com.guty.siemlite.model.SecurityEvent;
import com.guty.siemlite.repository.SecurityEventRepository;
import com.guty.siemlite.specification.SecurityEventSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@Tag(
        name = "Events",
        description = "Security events generated from logs"
)
public class EventController {

    private final SecurityEventRepository securityEventRepository;

    public EventController(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @Operation(summary = "Retrieve paginated and filtered security events")
    @GetMapping
    public Page<SecurityEvent> getEvents(

            @RequestParam(required = false)
            String eventType,

            @RequestParam(required = false)
            String sourceIp,

            @RequestParam(required = false)
            String username,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        Specification<SecurityEvent> specification = Specification
                .where(SecurityEventSpecification.hasEventType(eventType))
                .and(SecurityEventSpecification.hasSourceIp(sourceIp))
                .and(SecurityEventSpecification.hasUsername(username));

        PageRequest pageRequest = PageRequest.of(page, size);

        return securityEventRepository.findAll(specification, pageRequest);
    }
}
