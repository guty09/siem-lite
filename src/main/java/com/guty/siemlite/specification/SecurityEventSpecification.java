package com.guty.siemlite.specification;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.data.jpa.domain.Specification;

/**
 * Provides reusable JPA Specifications for filtering security events.
 */
public class SecurityEventSpecification {

    /**
     * Filters security events by event type.
     *
     * @param eventType event type to filter by
     * @return specification matching the provided event type, or no filter when blank
     */
    public static Specification<SecurityEvent> hasEventType(String eventType) {
        return (root, query, criteriaBuilder) ->
                eventType == null || eventType.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("eventType"), eventType);
    }

    /**
     * Filters security events by source IP address.
     *
     * @param sourceIp source IP address to filter by
     * @return specification matching the provided source IP, or no filter when blank
     */
    public static Specification<SecurityEvent> hasSourceIp(String sourceIp) {
        return (root, query, criteriaBuilder) ->
                sourceIp == null || sourceIp.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("sourceIp"), sourceIp);
    }

    /**
     * Filters security events by username.
     *
     * @param username username to filter by
     * @return specification matching the provided username, or no filter when blank
     */
    public static Specification<SecurityEvent> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                username == null || username.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("username"), username);
    }
}
