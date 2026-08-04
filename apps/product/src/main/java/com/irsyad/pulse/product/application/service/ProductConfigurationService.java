package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.product.benefit.Benefit;
import com.irsyad.pulse.product.domain.product.coverage.Coverage;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import com.irsyad.pulse.product.domain.product.exclusion.Exclusion;
import com.irsyad.pulse.product.domain.product.premium.PremiumConfiguration;
import com.irsyad.pulse.product.infrastructure.persistence.benefit.BenefitJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.benefit.BenefitJpaRepository;
import com.irsyad.pulse.product.infrastructure.persistence.coverage.CoverageJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.coverage.CoverageJpaRepository;
import com.irsyad.pulse.product.infrastructure.persistence.document.ProductDocumentJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.document.ProductDocumentJpaRepository;
import com.irsyad.pulse.product.infrastructure.persistence.eligibility.EligibilityJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.eligibility.EligibilityJpaRepository;
import com.irsyad.pulse.product.infrastructure.persistence.exclusion.ExclusionJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.exclusion.ExclusionJpaRepository;
import com.irsyad.pulse.product.infrastructure.persistence.premium.PremiumConfigurationJpaEntity;
import com.irsyad.pulse.product.infrastructure.persistence.premium.PremiumConfigurationJpaRepository;
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
 */
@Service
public class ProductConfigurationService {

    private final ProductApplicationService productApplicationService;
    private final CoverageJpaRepository coverageJpaRepository;
    private final BenefitJpaRepository benefitJpaRepository;
    private final ExclusionJpaRepository exclusionJpaRepository;
    private final EligibilityJpaRepository eligibilityJpaRepository;
    private final PremiumConfigurationJpaRepository premiumConfigurationJpaRepository;
    private final ProductDocumentJpaRepository productDocumentJpaRepository;

    public ProductConfigurationService(ProductApplicationService productApplicationService,
                                        CoverageJpaRepository coverageJpaRepository,
                                        BenefitJpaRepository benefitJpaRepository,
                                        ExclusionJpaRepository exclusionJpaRepository,
                                        EligibilityJpaRepository eligibilityJpaRepository,
                                        PremiumConfigurationJpaRepository premiumConfigurationJpaRepository,
                                        ProductDocumentJpaRepository productDocumentJpaRepository) {
        this.productApplicationService = productApplicationService;
        this.coverageJpaRepository = coverageJpaRepository;
        this.benefitJpaRepository = benefitJpaRepository;
        this.exclusionJpaRepository = exclusionJpaRepository;
        this.eligibilityJpaRepository = eligibilityJpaRepository;
        this.premiumConfigurationJpaRepository = premiumConfigurationJpaRepository;
        this.productDocumentJpaRepository = productDocumentJpaRepository;
    }

    private UUID versionId(UUID productId) {
        Product product = this.productApplicationService.detail(productId);
        return product.getProductId();
    }

    // ---- Coverage ----

    @Transactional
    public List<Coverage> replaceCoverages(UUID productId, List<Coverage> coverages) {
        UUID versionId = this.versionId(productId);
        this.coverageJpaRepository.deleteByProductVersionId(versionId);
        coverages.forEach(c -> this.coverageJpaRepository.save(CoverageJpaEntity.builder()
                .coverageId(c.getCoverageId() != null ? c.getCoverageId() : UUID.randomUUID())
                .productVersionId(versionId)
                .coverageAmount(c.getCoverageAmount())
                .currency(c.getCurrency())
                .build()));
        return this.coverageJpaRepository.findByProductVersionId(versionId).stream()
                .map(e -> Coverage.builder().coverageId(e.getCoverageId()).productVersionId(e.getProductVersionId())
                        .coverageAmount(e.getCoverageAmount()).currency(e.getCurrency()).build())
                .toList();
    }

    @Transactional
    public Coverage addCoverage(UUID productId, Coverage coverage) {
        UUID versionId = this.versionId(productId);
        CoverageJpaEntity saved = this.coverageJpaRepository.save(CoverageJpaEntity.builder()
                .coverageId(UUID.randomUUID())
                .productVersionId(versionId)
                .coverageAmount(coverage.getCoverageAmount())
                .currency(coverage.getCurrency())
                .build());
        return Coverage.builder().coverageId(saved.getCoverageId()).productVersionId(saved.getProductVersionId())
                .coverageAmount(saved.getCoverageAmount()).currency(saved.getCurrency()).build();
    }

    // ---- Benefit ----

    @Transactional
    public List<Benefit> replaceBenefits(UUID productId, List<Benefit> benefits) {
        UUID versionId = this.versionId(productId);
        this.benefitJpaRepository.deleteByProductVersionId(versionId);
        benefits.forEach(b -> this.benefitJpaRepository.save(BenefitJpaEntity.builder()
                .benefitId(b.getBenefitId() != null ? b.getBenefitId() : UUID.randomUUID())
                .productVersionId(versionId)
                .benefitName(b.getBenefitName())
                .description(b.getDescription())
                .maximumLimit(b.getMaximumLimit())
                .build()));
        return this.benefitJpaRepository.findByProductVersionId(versionId).stream()
                .map(e -> Benefit.builder().benefitId(e.getBenefitId()).productVersionId(e.getProductVersionId())
                        .benefitName(e.getBenefitName()).description(e.getDescription()).maximumLimit(e.getMaximumLimit()).build())
                .toList();
    }

    @Transactional
    public Benefit addBenefit(UUID productId, Benefit benefit) {
        UUID versionId = this.versionId(productId);
        BenefitJpaEntity saved = this.benefitJpaRepository.save(BenefitJpaEntity.builder()
                .benefitId(UUID.randomUUID())
                .productVersionId(versionId)
                .benefitName(benefit.getBenefitName())
                .description(benefit.getDescription())
                .maximumLimit(benefit.getMaximumLimit())
                .build());
        return Benefit.builder().benefitId(saved.getBenefitId()).productVersionId(saved.getProductVersionId())
                .benefitName(saved.getBenefitName()).description(saved.getDescription()).maximumLimit(saved.getMaximumLimit()).build();
    }

    // ---- Exclusion ----

    @Transactional
    public List<Exclusion> replaceExclusions(UUID productId, List<Exclusion> exclusions) {
        UUID versionId = this.versionId(productId);
        this.exclusionJpaRepository.deleteByProductVersionId(versionId);
        exclusions.forEach(e -> this.exclusionJpaRepository.save(ExclusionJpaEntity.builder()
                .exclusionId(e.getExclusionId() != null ? e.getExclusionId() : UUID.randomUUID())
                .productVersionId(versionId)
                .description(e.getDescription())
                .build()));
        return this.exclusionJpaRepository.findByProductVersionId(versionId).stream()
                .map(en -> Exclusion.builder().exclusionId(en.getExclusionId()).productVersionId(en.getProductVersionId())
                        .description(en.getDescription()).build())
                .toList();
    }

    @Transactional
    public Exclusion addExclusion(UUID productId, Exclusion exclusion) {
        UUID versionId = this.versionId(productId);
        ExclusionJpaEntity saved = this.exclusionJpaRepository.save(ExclusionJpaEntity.builder()
                .exclusionId(UUID.randomUUID())
                .productVersionId(versionId)
                .description(exclusion.getDescription())
                .build());
        return Exclusion.builder().exclusionId(saved.getExclusionId()).productVersionId(saved.getProductVersionId())
                .description(saved.getDescription()).build();
    }

    // ---- Eligibility ----

    @Transactional
    public Eligibility replaceEligibility(UUID productId, Eligibility eligibility) {
        UUID versionId = this.versionId(productId);
        this.eligibilityJpaRepository.deleteByProductVersionId(versionId);
        EligibilityJpaEntity saved = this.eligibilityJpaRepository.save(EligibilityJpaEntity.builder()
                .eligibilityId(UUID.randomUUID())
                .productVersionId(versionId)
                .minimumAge(eligibility.getMinimumAge())
                .maximumAge(eligibility.getMaximumAge())
                .occupationClass(eligibility.getOccupationClass())
                .nationality(eligibility.getNationality())
                .residency(eligibility.getResidency())
                .build());
        return this.toEligibility(saved);
    }

    // ---- Premium ----

    @Transactional
    public List<PremiumConfiguration> replacePremiums(UUID productId, List<PremiumConfiguration> premiums) {
        UUID versionId = this.versionId(productId);
        this.premiumConfigurationJpaRepository.deleteByProductVersionId(versionId);
        premiums.forEach(p -> this.premiumConfigurationJpaRepository.save(PremiumConfigurationJpaEntity.builder()
                .premiumConfigurationId(p.getPremiumConfigurationId() != null ? p.getPremiumConfigurationId() : UUID.randomUUID())
                .productVersionId(versionId)
                .coverageBand(p.getCoverageBand())
                .ageBand(p.getAgeBand())
                .occupationClass(p.getOccupationClass())
                .basePremium(p.getBasePremium())
                .build()));
        return this.premiumConfigurationJpaRepository.findByProductVersionId(versionId).stream()
                .map(e -> PremiumConfiguration.builder().premiumConfigurationId(e.getPremiumConfigurationId())
                        .productVersionId(e.getProductVersionId()).coverageBand(e.getCoverageBand())
                        .ageBand(e.getAgeBand()).occupationClass(e.getOccupationClass()).basePremium(e.getBasePremium()).build())
                .toList();
    }

    @Transactional
    public PremiumConfiguration addPremium(UUID productId, PremiumConfiguration premium) {
        UUID versionId = this.versionId(productId);
        PremiumConfigurationJpaEntity saved = this.premiumConfigurationJpaRepository.save(PremiumConfigurationJpaEntity.builder()
                .premiumConfigurationId(UUID.randomUUID())
                .productVersionId(versionId)
                .coverageBand(premium.getCoverageBand())
                .ageBand(premium.getAgeBand())
                .occupationClass(premium.getOccupationClass())
                .basePremium(premium.getBasePremium())
                .build());
        return PremiumConfiguration.builder().premiumConfigurationId(saved.getPremiumConfigurationId())
                .productVersionId(saved.getProductVersionId()).coverageBand(saved.getCoverageBand())
                .ageBand(saved.getAgeBand()).occupationClass(saved.getOccupationClass()).basePremium(saved.getBasePremium()).build();
    }

    // ---- Document ----

    @Transactional
    public List<ProductDocument> replaceDocuments(UUID productId, List<ProductDocument> documents) {
        UUID versionId = this.versionId(productId);
        this.productDocumentJpaRepository.deleteByProductVersionId(versionId);
        documents.forEach(d -> this.productDocumentJpaRepository.save(ProductDocumentJpaEntity.builder()
                .documentId(d.getDocumentId() != null ? d.getDocumentId() : UUID.randomUUID())
                .productVersionId(versionId)
                .documentName(d.getDocumentName())
                .documentType(d.getDocumentType())
                .storageReference(d.getStorageReference())
                .build()));
        return this.productDocumentJpaRepository.findByProductVersionId(versionId).stream()
                .map(e -> ProductDocument.builder().documentId(e.getDocumentId()).productVersionId(e.getProductVersionId())
                        .documentName(e.getDocumentName()).documentType(e.getDocumentType()).storageReference(e.getStorageReference()).build())
                .toList();
    }

    @Transactional
    public ProductDocument addDocument(UUID productId, ProductDocument document) {
        UUID versionId = this.versionId(productId);
        ProductDocumentJpaEntity saved = this.productDocumentJpaRepository.save(ProductDocumentJpaEntity.builder()
                .documentId(UUID.randomUUID())
                .productVersionId(versionId)
                .documentName(document.getDocumentName())
                .documentType(document.getDocumentType())
                .storageReference(document.getStorageReference())
                .build());
        return ProductDocument.builder().documentId(saved.getDocumentId()).productVersionId(saved.getProductVersionId())
                .documentName(saved.getDocumentName()).documentType(saved.getDocumentType()).storageReference(saved.getStorageReference()).build();
    }

    private Eligibility toEligibility(EligibilityJpaEntity e) {
        return Eligibility.builder().eligibilityId(e.getEligibilityId()).productVersionId(e.getProductVersionId())
                .minimumAge(e.getMinimumAge()).maximumAge(e.getMaximumAge()).occupationClass(e.getOccupationClass())
                .nationality(e.getNationality()).residency(e.getResidency()).build();
    }
}
