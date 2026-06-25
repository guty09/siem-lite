package com.guty.siemlite.specification;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.data.jpa.domain.Specification;

public class SecurityEventSpecification {

    public static Specification<SecurityEvent> hasEventType(String eventType) {
        return (root, query, criteriaBuilder) ->
                eventType == null || eventType.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("eventType"), eventType);
    }

    public static Specification<SecurityEvent> hasSourceIp(String sourceIp) {
        return (root, query, criteriaBuilder) ->
                sourceIp == null || sourceIp.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("sourceIp"), sourceIp);
    }

    public static Specification<SecurityEvent> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                username == null || username.isBlank()
                        ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("username"), username);
    }
}