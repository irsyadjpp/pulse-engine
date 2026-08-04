package com.irsyad.pulse.product.infrastructure.persistence.configuration;

import com.irsyad.pulse.product.application.port.ProductConfigurationPort;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * JPA adapter implementing the ProductConfigurationPort (Hexagonal Architecture).
 * Persists child configuration entities against the product version.
 */
@Component
public class ProductConfigurationJpaAdapter implements ProductConfigurationPort {

    private final CoverageJpaRepository coverageJpaRepository;
    private final BenefitJpaRepository benefitJpaRepository;
    private final ExclusionJpaRepository exclusionJpaRepository;
    private final EligibilityJpaRepository eligibilityJpaRepository;
    private final PremiumConfigurationJpaRepository premiumConfigurationJpaRepository;
    private final ProductDocumentJpaRepository productDocumentJpaRepository;

    public ProductConfigurationJpaAdapter(CoverageJpaRepository coverageJpaRepository,
                                          BenefitJpaRepository benefitJpaRepository,
                                          ExclusionJpaRepository exclusionJpaRepository,
                                          EligibilityJpaRepository eligibilityJpaRepository,
                                          PremiumConfigurationJpaRepository premiumConfigurationJpaRepository,
                                          ProductDocumentJpaRepository productDocumentJpaRepository) {
        this.coverageJpaRepository = coverageJpaRepository;
        this.benefitJpaRepository = benefitJpaRepository;
        this.exclusionJpaRepository = exclusionJpaRepository;
        this.eligibilityJpaRepository = eligibilityJpaRepository;
        this.premiumConfigurationJpaRepository = premiumConfigurationJpaRepository;
        this.productDocumentJpaRepository = productDocumentJpaRepository;
    }

    @Override
    @Transactional
    public List<Coverage> replaceCoverages(UUID productVersionId, List<Coverage> coverages) {
        this.coverageJpaRepository.deleteByProductVersionId(productVersionId);
        coverages.forEach(c -> this.coverageJpaRepository.save(CoverageJpaEntity.builder()
                .id(c.getCoverageId() != null ? c.getCoverageId() : UUID.randomUUID())
                .productVersionId(productVersionId)
                .coverageAmount(c.getCoverageAmount())
                .currency(c.getCurrency())
                .build()));
        return this.coverageJpaRepository.findByProductVersionId(productVersionId).stream()
                .map(e -> Coverage.builder().coverageId(e.getId()).productVersionId(e.getProductVersionId())
                        .coverageAmount(e.getCoverageAmount()).currency(e.getCurrency()).build())
                .toList();
    }

    @Override
    @Transactional
    public Coverage addCoverage(UUID productVersionId, Coverage coverage) {
        CoverageJpaEntity saved = this.coverageJpaRepository.save(CoverageJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .coverageAmount(coverage.getCoverageAmount())
                .currency(coverage.getCurrency())
                .build());
        return Coverage.builder().coverageId(saved.getId()).productVersionId(saved.getProductVersionId())
                .coverageAmount(saved.getCoverageAmount()).currency(saved.getCurrency()).build();
    }

    @Override
    @Transactional
    public List<Benefit> replaceBenefits(UUID productVersionId, List<Benefit> benefits) {
        this.benefitJpaRepository.deleteByProductVersionId(productVersionId);
        benefits.forEach(b -> this.benefitJpaRepository.save(BenefitJpaEntity.builder()
                .id(b.getBenefitId() != null ? b.getBenefitId() : UUID.randomUUID())
                .productVersionId(productVersionId)
                .benefitName(b.getBenefitName())
                .description(b.getDescription())
                .maximumLimit(b.getMaximumLimit())
                .build()));
        return this.benefitJpaRepository.findByProductVersionId(productVersionId).stream()
                .map(e -> Benefit.builder().benefitId(e.getId()).productVersionId(e.getProductVersionId())
                        .benefitName(e.getBenefitName()).description(e.getDescription()).maximumLimit(e.getMaximumLimit()).build())
                .toList();
    }

    @Override
    @Transactional
    public Benefit addBenefit(UUID productVersionId, Benefit benefit) {
        BenefitJpaEntity saved = this.benefitJpaRepository.save(BenefitJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .benefitName(benefit.getBenefitName())
                .description(benefit.getDescription())
                .maximumLimit(benefit.getMaximumLimit())
                .build());
        return Benefit.builder().benefitId(saved.getId()).productVersionId(saved.getProductVersionId())
                .benefitName(saved.getBenefitName()).description(saved.getDescription()).maximumLimit(saved.getMaximumLimit()).build();
    }

    @Override
    @Transactional
    public List<Exclusion> replaceExclusions(UUID productVersionId, List<Exclusion> exclusions) {
        this.exclusionJpaRepository.deleteByProductVersionId(productVersionId);
        exclusions.forEach(e -> this.exclusionJpaRepository.save(ExclusionJpaEntity.builder()
                .id(e.getExclusionId() != null ? e.getExclusionId() : UUID.randomUUID())
                .productVersionId(productVersionId)
                .description(e.getDescription())
                .build()));
        return this.exclusionJpaRepository.findByProductVersionId(productVersionId).stream()
                .map(en -> Exclusion.builder().exclusionId(en.getId()).productVersionId(en.getProductVersionId())
                        .description(en.getDescription()).build())
                .toList();
    }

    @Override
    @Transactional
    public Exclusion addExclusion(UUID productVersionId, Exclusion exclusion) {
        ExclusionJpaEntity saved = this.exclusionJpaRepository.save(ExclusionJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .description(exclusion.getDescription())
                .build());
        return Exclusion.builder().exclusionId(saved.getId()).productVersionId(saved.getProductVersionId())
                .description(saved.getDescription()).build();
    }

    @Override
    @Transactional
    public Eligibility replaceEligibility(UUID productVersionId, Eligibility eligibility) {
        this.eligibilityJpaRepository.deleteByProductVersionId(productVersionId);
        EligibilityJpaEntity saved = this.eligibilityJpaRepository.save(EligibilityJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .minimumAge(eligibility.getMinimumAge())
                .maximumAge(eligibility.getMaximumAge())
                .occupationClass(eligibility.getOccupationClass())
                .nationality(eligibility.getNationality())
                .residency(eligibility.getResidency())
                .build());
        return this.toEligibility(saved);
    }

    @Override
    @Transactional
    public List<PremiumConfiguration> replacePremiums(UUID productVersionId, List<PremiumConfiguration> premiums) {
        this.premiumConfigurationJpaRepository.deleteByProductVersionId(productVersionId);
        premiums.forEach(p -> this.premiumConfigurationJpaRepository.save(PremiumConfigurationJpaEntity.builder()
                .id(p.getPremiumConfigurationId() != null ? p.getPremiumConfigurationId() : UUID.randomUUID())
                .productVersionId(productVersionId)
                .coverageBand(p.getCoverageBand())
                .ageBand(p.getAgeBand())
                .occupationClass(p.getOccupationClass())
                .basePremium(p.getBasePremium())
                .build()));
        return this.premiumConfigurationJpaRepository.findByProductVersionId(productVersionId).stream()
                .map(e -> PremiumConfiguration.builder().premiumConfigurationId(e.getId())
                        .productVersionId(e.getProductVersionId()).coverageBand(e.getCoverageBand())
                        .ageBand(e.getAgeBand()).occupationClass(e.getOccupationClass()).basePremium(e.getBasePremium()).build())
                .toList();
    }

    @Override
    @Transactional
    public PremiumConfiguration addPremium(UUID productVersionId, PremiumConfiguration premium) {
        PremiumConfigurationJpaEntity saved = this.premiumConfigurationJpaRepository.save(PremiumConfigurationJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .coverageBand(premium.getCoverageBand())
                .ageBand(premium.getAgeBand())
                .occupationClass(premium.getOccupationClass())
                .basePremium(premium.getBasePremium())
                .build());
        return PremiumConfiguration.builder().premiumConfigurationId(saved.getId())
                .productVersionId(saved.getProductVersionId()).coverageBand(saved.getCoverageBand())
                .ageBand(saved.getAgeBand()).occupationClass(saved.getOccupationClass()).basePremium(saved.getBasePremium()).build();
    }

    @Override
    @Transactional
    public List<ProductDocument> replaceDocuments(UUID productVersionId, List<ProductDocument> documents) {
        this.productDocumentJpaRepository.deleteByProductVersionId(productVersionId);
        documents.forEach(d -> this.productDocumentJpaRepository.save(ProductDocumentJpaEntity.builder()
                .id(d.getDocumentId() != null ? d.getDocumentId() : UUID.randomUUID())
                .productVersionId(productVersionId)
                .documentName(d.getDocumentName())
                .documentType(d.getDocumentType())
                .storageReference(d.getStorageReference())
                .build()));
        return this.productDocumentJpaRepository.findByProductVersionId(productVersionId).stream()
                .map(e -> ProductDocument.builder().documentId(e.getId()).productVersionId(e.getProductVersionId())
                        .documentName(e.getDocumentName()).documentType(e.getDocumentType()).storageReference(e.getStorageReference()).build())
                .toList();
    }

    @Override
    @Transactional
    public ProductDocument addDocument(UUID productVersionId, ProductDocument document) {
        ProductDocumentJpaEntity saved = this.productDocumentJpaRepository.save(ProductDocumentJpaEntity.builder()
                .id(UUID.randomUUID())
                .productVersionId(productVersionId)
                .documentName(document.getDocumentName())
                .documentType(document.getDocumentType())
                .storageReference(document.getStorageReference())
                .build());
        return ProductDocument.builder().documentId(saved.getId()).productVersionId(saved.getProductVersionId())
                .documentName(saved.getDocumentName()).documentType(saved.getDocumentType()).storageReference(saved.getStorageReference()).build();
    }

    private Eligibility toEligibility(EligibilityJpaEntity e) {
        return Eligibility.builder().eligibilityId(e.getId()).productVersionId(e.getProductVersionId())
                .minimumAge(e.getMinimumAge()).maximumAge(e.getMaximumAge()).occupationClass(e.getOccupationClass())
                .nationality(e.getNationality()).residency(e.getResidency()).build();
    }
}