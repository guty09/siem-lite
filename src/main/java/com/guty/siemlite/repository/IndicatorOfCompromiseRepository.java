package com.guty.siemlite.repository;

import com.guty.siemlite.model.IndicatorOfCompromise;
import com.guty.siemlite.model.IocType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Indicators of Compromise.
 */
public interface IndicatorOfCompromiseRepository extends JpaRepository<IndicatorOfCompromise, Long> {

    /**
     * Finds an IOC by its exact value.
     *
     * @param value the IOC value
     * @return the matching IOC, if present
     */
    Optional<IndicatorOfCompromise> findByValue(String value);

    /**
     * Finds all active IOCs.
     *
     * @param active active status
     * @return active or inactive IOCs
     */
    List<IndicatorOfCompromise> findByActive(boolean active);

    /**
     * Finds active IOCs by type.
     *
     * @param type IOC type
     * @param active active status
     * @return matching IOCs
     */
    List<IndicatorOfCompromise> findByTypeAndActive(IocType type, boolean active);
}
