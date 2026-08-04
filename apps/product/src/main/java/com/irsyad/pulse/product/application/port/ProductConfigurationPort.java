package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.product.benefit.Benefit;
import com.irsyad.pulse.product.domain.product.coverage.Coverage;
import com.irsyad.pulse.product.domain.product.document.ProductDocument;
import com.irsyad.pulse.product.domain.product.eligibility.Eligibility;
import com.irsyad.pulse.product.domain.product.exclusion.Exclusion;
import com.irsyad.pulse.product.domain.product.premium.PremiumConfiguration;

import java.util.List;
import java.util.UUID;

/**
 * Output port for Product Configuration persistence (Hexagonal Architecture).
 * Implemented by the JPA adapter in infrastructure layer.
 *
 * <p>All child configuration entities are persisted against the current
 * product version (product_version_id).
 */
public interface ProductConfigurationPort {

    List<Coverage> replaceCoverages(UUID productVersionId, List<Coverage> coverages);

    Coverage addCoverage(UUID productVersionId, Coverage coverage);

    List<Benefit> replaceBenefits(UUID productVersionId, List<Benefit> benefits);

    Benefit addBenefit(UUID productVersionId, Benefit benefit);

    List<Exclusion> replaceExclusions(UUID productVersionId, List<Exclusion> exclusions);

    Exclusion addExclusion(UUID productVersionId, Exclusion exclusion);

    Eligibility replaceEligibility(UUID productVersionId, Eligibility eligibility);

    List<PremiumConfiguration> replacePremiums(UUID productVersionId, List<PremiumConfiguration> premiums);

    PremiumConfiguration addPremium(UUID productVersionId, PremiumConfiguration premium);

    List<ProductDocument> replaceDocuments(UUID productVersionId, List<ProductDocument> documents);

    ProductDocument addDocument(UUID productVersionId, ProductDocument document);

    // ---- Read (for immutable snapshot) ----

    List<Coverage> findCoverages(UUID productVersionId);

    List<Benefit> findBenefits(UUID productVersionId);

    List<Exclusion> findExclusions(UUID productVersionId);

    Eligibility findEligibility(UUID productVersionId);

    List<PremiumConfiguration> findPremiums(UUID productVersionId);

    List<ProductDocument> findDocuments(UUID productVersionId);
}
