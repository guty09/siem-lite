package com.guty.siemlite.specification;

import com.guty.siemlite.model.Alert;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class containing reusable JPA Specifications for filtering
 * {@link Alert} entities.
 */
public final class AlertSpecification {

    /**
     * Creates a specification that filters alerts by status.
     *
     * @param status the alert status to match
     * @return a specification filtering alerts by status,
     *         or {@code null} if status is {@code null}
     */
    public static Specification<Alert> hasStatus(String status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    /**
     * Creates a specification that filters alerts by assigned analyst.
     *
     * @param assignedTo the assigned analyst to match
     * @return a specification filtering alerts by assigned analyst,
     *         or {@code null} if assignedTo is {@code null}
     */
    public static Specification<Alert> hasAssignedTo(String assignedTo) {
        return (root, query, cb) ->
                assignedTo == null ? null :
                        cb.equal(root.get("assignedAnalyst"), assignedTo);
    }

    /**
     * Creates a specification that filters alerts by MITRE ATT&CK technique.
     *
     * @param technique the MITRE ATT&CK technique identifier to match
     * @return a specification filtering alerts by MITRE technique,
     *         or {@code null} if technique is {@code null}
     */
    public static Specification<Alert> hasMitreTechnique(String technique) {
        return (root, query, cb) ->
                technique == null ? null :
                        cb.equal(root.get("mitreTechnique"), technique);
    }

    /**
     * Prevents instantiation of this utility class.
     */
    private AlertSpecification() {
    }
}