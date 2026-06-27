package com.guty.siemlite.service;

import com.guty.siemlite.dto.IndicatorOfCompromiseRequest;
import com.guty.siemlite.model.IndicatorOfCompromise;
import com.guty.siemlite.repository.IndicatorOfCompromiseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing Indicators of Compromise.
 */
@Service
public class IndicatorOfCompromiseService {

    private final IndicatorOfCompromiseRepository indicatorOfCompromiseRepository;

    /**
     * Creates a new IndicatorOfCompromiseService.
     *
     * @param indicatorOfCompromiseRepository IOC repository
     */
    public IndicatorOfCompromiseService(IndicatorOfCompromiseRepository indicatorOfCompromiseRepository) {
        this.indicatorOfCompromiseRepository = indicatorOfCompromiseRepository;
    }

    /**
     * Creates a new IOC after validating required business rules.
     *
     * @param request IOC creation request
     * @return saved IOC
     */
    public IndicatorOfCompromise createIoc(IndicatorOfCompromiseRequest request) {
        validateRequest(request);
        validateUniqueValue(request.getValue());

        IndicatorOfCompromise ioc = new IndicatorOfCompromise();
        applyRequestToIoc(request, ioc);
        ioc.setActive(true);

        return indicatorOfCompromiseRepository.save(ioc);
    }

    /**
     * Returns all stored IOCs.
     *
     * @return list of IOCs
     */
    public List<IndicatorOfCompromise> getAllIocs() {
        return indicatorOfCompromiseRepository.findAll();
    }

    /**
     * Returns one IOC by ID.
     *
     * @param id IOC ID
     * @return matching IOC
     */
    public IndicatorOfCompromise getIocById(Long id) {
        return indicatorOfCompromiseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("IOC not found with ID: " + id));
    }

    /**
     * Updates an existing IOC.
     *
     * @param id IOC ID
     * @param request update request
     * @return updated IOC
     */
    public IndicatorOfCompromise updateIoc(Long id, IndicatorOfCompromiseRequest request) {
        validateRequest(request);

        IndicatorOfCompromise existingIoc = getIocById(id);

        indicatorOfCompromiseRepository.findByValue(request.getValue())
                .filter(ioc -> !ioc.getId().equals(id))
                .ifPresent(ioc -> {
                    throw new IllegalArgumentException("IOC already exists with value: " + request.getValue());
                });

        applyRequestToIoc(request, existingIoc);

        return indicatorOfCompromiseRepository.save(existingIoc);
    }

    /**
     * Enables an IOC for active matching.
     *
     * @param id IOC ID
     * @return enabled IOC
     */
    public IndicatorOfCompromise enableIoc(Long id) {
        IndicatorOfCompromise ioc = getIocById(id);
        ioc.setActive(true);
        return indicatorOfCompromiseRepository.save(ioc);
    }

    /**
     * Disables an IOC from active matching without deleting it.
     *
     * @param id IOC ID
     * @return disabled IOC
     */
    public IndicatorOfCompromise disableIoc(Long id) {
        IndicatorOfCompromise ioc = getIocById(id);
        ioc.setActive(false);
        return indicatorOfCompromiseRepository.save(ioc);
    }

    /**
     * Deletes an IOC permanently.
     *
     * @param id IOC ID
     */
    public void deleteIoc(Long id) {
        IndicatorOfCompromise ioc = getIocById(id);
        indicatorOfCompromiseRepository.delete(ioc);
    }

    /**
     * Applies request fields to an IOC entity.
     *
     * @param request IOC request
     * @param ioc IOC entity
     */
    private void applyRequestToIoc(IndicatorOfCompromiseRequest request, IndicatorOfCompromise ioc) {
        ioc.setValue(request.getValue().trim());
        ioc.setType(request.getType());
        ioc.setThreatSource(request.getThreatSource());
        ioc.setConfidence(request.getConfidence());
        ioc.setMalwareFamily(request.getMalwareFamily());
        ioc.setCampaign(request.getCampaign());
        ioc.setDescription(request.getDescription());
    }

    /**
     * Validates IOC request input.
     *
     * @param request IOC request
     */
    private void validateRequest(IndicatorOfCompromiseRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("IOC request cannot be null");
        }

        if (request.getValue() == null || request.getValue().isBlank()) {
            throw new IllegalArgumentException("IOC value is required");
        }

        if (request.getType() == null) {
            throw new IllegalArgumentException("IOC type is required");
        }

        if (request.getConfidence() != null
                && (request.getConfidence() < 0 || request.getConfidence() > 100)) {
            throw new IllegalArgumentException("IOC confidence must be between 0 and 100");
        }
    }

    /**
     * Validates that an IOC value is unique.
     *
     * @param value IOC value
     */
    private void validateUniqueValue(String value) {
        indicatorOfCompromiseRepository.findByValue(value.trim())
                .ifPresent(ioc -> {
                    throw new IllegalArgumentException("IOC already exists with value: " + value);
                });
    }
}
