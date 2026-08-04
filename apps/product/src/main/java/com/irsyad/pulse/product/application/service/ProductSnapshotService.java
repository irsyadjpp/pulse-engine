package com.irsyad.pulse.product.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irsyad.pulse.product.application.port.ProductConfigurationPort;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Builds the immutable JSON snapshot for a published ProductVersion (TSD_02 Section 6).
 *
 * <p>The snapshot captures the complete Product configuration (coverages, benefits,
 * exclusions, eligibility, premiums, documents) so that Quote Service can read a
 * full immutable version without depending on the mutable Product draft.
 */
@Service
public class ProductSnapshotService {

    private final ProductConfigurationPort configurationPort;
    private final ObjectMapper objectMapper;

    public ProductSnapshotService(ProductConfigurationPort configurationPort, ObjectMapper objectMapper) {
        this.configurationPort = configurationPort;
        this.objectMapper = objectMapper;
    }

    /**
     * Builds the snapshot JSON for the configuration attached to the given
     * {@code productVersionId}.
     *
     * <p>For a freshly-published Product, child configuration may be attached to the
     * productId (when no ProductVersion row existed yet). In that case the caller
     * must re-map the configuration to the new ProductVersion id before invoking.
     */
    public String buildSnapshot(UUID productVersionId) {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("coverages", this.configurationPort.findCoverages(productVersionId));
        snapshot.put("benefits", this.configurationPort.findBenefits(productVersionId));
        snapshot.put("exclusions", this.configurationPort.findExclusions(productVersionId));
        Eligibility eligibility = this.configurationPort.findEligibility(productVersionId);
        snapshot.put("eligibility", eligibility != null ? eligibility : Map.of());
        snapshot.put("premiums", this.configurationPort.findPremiums(productVersionId));
        List<ProductDocument> documents = this.configurationPort.findDocuments(productVersionId);
        snapshot.put("documents", documents);
        try {
            return this.objectMapper.writeValueAsString(snapshot);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize ProductVersion snapshot.", e);
        }
    }
}