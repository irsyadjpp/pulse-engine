package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.port.ProductConfigurationPort;
import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.product.benefit.Benefit;
import com.irsyad.pulse.product.domain.product.coverage.Coverage;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import com.irsyad.pulse.product.domain.product.exclusion.Exclusion;
import com.irsyad.pulse.product.domain.product.premium.PremiumConfiguration;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import com.irsyad.pulse.product.shared.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Application service for Product Configuration (TSD_04 Section 9, FSD_03).
 *
 * <p>Child configuration entities are persisted against the current product
 * version (product_version_id). Supports both PUT collection (replace all)
 * and CRUD per-item operations (A3 decision).
 *
 * <p>Hexagonal Architecture: persistence is delegated to {@link ProductConfigurationPort},
 * implemented by the JPA adapter in the infrastructure layer.
 */
@Service
public class ProductConfigurationService {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVersionPort productVersionPort;
    private final ProductConfigurationPort productConfigurationPort;

    public ProductConfigurationService(ProductRepositoryPort productRepositoryPort,
                                       ProductVersionPort productVersionPort,
                                       ProductConfigurationPort productConfigurationPort) {
        this.productRepositoryPort = productRepositoryPort;
        this.productVersionPort = productVersionPort;
        this.productConfigurationPort = productConfigurationPort;
    }

    /**
     * Resolves the product_version_id to which child configuration is attached.
     *
     * <p>Published products are represented by an immutable ProductVersion row;
     * the latest version is used. For a DRAFT product that has not been published
     * yet, no ProductVersion row exists, so the productId is used as the
     * version reference (consistent with the current schema).
     */
    private UUID versionId(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        int currentVersion = product.getVersion();
        return this.productVersionPort.findByProductIdAndVersion(productId, currentVersion)
                .map(ProductVersion::getProductVersionId)
                .orElse(productId);
    }

    // ---- Coverage ----

    @Transactional
    public List<Coverage> replaceCoverages(UUID productId, List<Coverage> coverages) {
        return this.productConfigurationPort.replaceCoverages(this.versionId(productId), coverages);
    }

    @Transactional
    public Coverage addCoverage(UUID productId, Coverage coverage) {
        return this.productConfigurationPort.addCoverage(this.versionId(productId), coverage);
    }

    // ---- Benefit ----

    @Transactional
    public List<Benefit> replaceBenefits(UUID productId, List<Benefit> benefits) {
        return this.productConfigurationPort.replaceBenefits(this.versionId(productId), benefits);
    }

    @Transactional
    public Benefit addBenefit(UUID productId, Benefit benefit) {
        return this.productConfigurationPort.addBenefit(this.versionId(productId), benefit);
    }

    // ---- Exclusion ----

    @Transactional
    public List<Exclusion> replaceExclusions(UUID productId, List<Exclusion> exclusions) {
        return this.productConfigurationPort.replaceExclusions(this.versionId(productId), exclusions);
    }

    @Transactional
    public Exclusion addExclusion(UUID productId, Exclusion exclusion) {
        return this.productConfigurationPort.addExclusion(this.versionId(productId), exclusion);
    }

    // ---- Eligibility ----

    @Transactional
    public Eligibility replaceEligibility(UUID productId, Eligibility eligibility) {
        return this.productConfigurationPort.replaceEligibility(this.versionId(productId), eligibility);
    }

    // ---- Premium ----

    @Transactional
    public List<PremiumConfiguration> replacePremiums(UUID productId, List<PremiumConfiguration> premiums) {
        return this.productConfigurationPort.replacePremiums(this.versionId(productId), premiums);
    }

    @Transactional
    public PremiumConfiguration addPremium(UUID productId, PremiumConfiguration premium) {
        return this.productConfigurationPort.addPremium(this.versionId(productId), premium);
    }

    // ---- Document ----

    @Transactional
    public List<ProductDocument> replaceDocuments(UUID productId, List<ProductDocument> documents) {
        return this.productConfigurationPort.replaceDocuments(this.versionId(productId), documents);
    }

    @Transactional
    public ProductDocument addDocument(UUID productId, ProductDocument document) {
        return this.productConfigurationPort.addDocument(this.versionId(productId), document);
    }
}