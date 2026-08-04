package com.irsyad.pulse.product.domain.product;

import com.irsyad.pulse.product.domain.product.benefit.Benefit;
import com.irsyad.pulse.product.domain.product.coverage.Coverage;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import com.irsyad.pulse.product.domain.product.exclusion.Exclusion;
import com.irsyad.pulse.product.domain.product.premium.PremiumConfiguration;
import com.irsyad.pulse.product.domain.shared.DomainEvent;
import com.irsyad.pulse.product.domain.shared.ProductStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Product Aggregate Root (FSD_02, FSD_03, FSD_05).
 *
 * <p>All operations (Create, Update, Publish, Archive, Create New Version) must
 * be executed through this aggregate to enforce Business Rules BR-001 to BR-012
 * without duplicating domain logic.
 */
@Getter
@Builder
public class Product {

    private final UUID productId;
    private final UUID companyId;
    private final String productCode;
    private String productName;
    private String category;
    private int version;
    private ProductStatus status;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private final Instant createdAt;
    private final String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private long optimisticLockVersion;
    private boolean deleted;

    private final List<Coverage> coverages = new ArrayList<>();
    private final List<Benefit> benefits = new ArrayList<>();
    private final List<Exclusion> exclusions = new ArrayList<>();
    private Eligibility eligibility;
    private final List<PremiumConfiguration> premiumConfigurations = new ArrayList<>();
    private final List<ProductDocument> documents = new ArrayList<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * BR-004: Published product cannot be modified directly.
     * Changing a published product requires a new Draft version (BR-005).
     */
    public void updateDraft(String productName, String category, LocalDate effectiveDate, LocalDate expiryDate) {
        if (this.status == ProductStatus.PUBLISHED || this.status == ProductStatus.ARCHIVED) {
            throw new IllegalStateException("Only DRAFT product can be updated directly.");
        }
        this.productName = productName;
        this.category = category;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.updatedAt = Instant.now();
    }

    /**
     * BR-008 to BR-011: publish validation.
     * Requires at least one benefit, one coverage, eligibility and premium configuration.
     */
    public void publish() {
        if (this.status != ProductStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT product can be published.");
        }
        if (this.benefits.isEmpty()) {
            throw new IllegalStateException("Product must have at least one benefit (BR-008).");
        }
        if (this.coverages.isEmpty()) {
            throw new IllegalStateException("Product must have at least one coverage (BR-009).");
        }
        if (this.eligibility == null) {
            throw new IllegalStateException("Eligibility must be configured before publish (BR-010).");
        }
        if (this.premiumConfigurations.isEmpty()) {
            throw new IllegalStateException("Premium configuration is required before publish (BR-011).");
        }
        this.status = ProductStatus.PUBLISHED;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ProductPublishedEvent.of(this.productId, this.version));
    }

    /**
     * BR-05: archive stops product usage without deleting history.
     */
    public void archive() {
        if (this.status != ProductStatus.PUBLISHED) {
            throw new IllegalStateException("Only PUBLISHED product can be archived.");
        }
        this.status = ProductStatus.ARCHIVED;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ProductArchivedEvent.of(this.productId, this.version));
    }

    /**
     * BR-005: every change to a published product produces a new version (Draft Version n+1).
     */
    public void createNewVersion() {
        if (this.status != ProductStatus.PUBLISHED) {
            throw new IllegalStateException("New version can only be created from PUBLISHED product.");
        }
        int oldVersion = this.version;
        this.status = ProductStatus.DRAFT;
        this.version = this.version + 1;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ProductVersionCreatedEvent.of(this.productId, oldVersion, this.version));
    }

    public void addCoverage(Coverage coverage) {
        this.coverages.add(coverage);
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "coverage"));
    }

    public void addBenefit(Benefit benefit) {
        this.benefits.add(benefit);
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "benefit"));
    }

    public void addExclusion(Exclusion exclusion) {
        this.exclusions.add(exclusion);
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "exclusion"));
    }

    public void configureEligibility(Eligibility eligibility) {
        if (this.status == ProductStatus.PUBLISHED || this.status == ProductStatus.ARCHIVED) {
            throw new IllegalStateException("Published/Archived product configuration cannot be modified directly.");
        }
        this.eligibility = eligibility;
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "eligibility"));
    }

    public void addPremiumConfiguration(PremiumConfiguration premiumConfiguration) {
        this.premiumConfigurations.add(premiumConfiguration);
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "premium"));
    }

    public void addDocument(ProductDocument document) {
        this.documents.add(document);
        this.updatedAt = Instant.now();
        this.record(com.irsyad.pulse.product.domain.shared.ConfigurationUpdatedEvent.of(this.productId, "document"));
    }

    public List<Coverage> getCoverages() {
        return Collections.unmodifiableList(this.coverages);
    }

    public List<Benefit> getBenefits() {
        return Collections.unmodifiableList(this.benefits);
    }

    public List<Exclusion> getExclusions() {
        return Collections.unmodifiableList(this.exclusions);
    }

    public List<PremiumConfiguration> getPremiumConfigurations() {
        return Collections.unmodifiableList(this.premiumConfigurations);
    }

    public List<ProductDocument> getDocuments() {
        return Collections.unmodifiableList(this.documents);
    }

    protected void record(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
}