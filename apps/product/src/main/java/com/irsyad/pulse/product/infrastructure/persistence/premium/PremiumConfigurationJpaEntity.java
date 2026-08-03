package com.irsyad.pulse.product.infrastructure.persistence.premium;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity mapping the premium_configuration table (Appendix O).
 * Child entity of the product aggregate.
 */
@Entity
@Table(name = "premium_configuration")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PremiumConfigurationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "premium_configuration_id", nullable = false, updatable = false)
    private UUID premiumConfigurationId;

    @Column(name = "product_version_id", nullable = false, updatable = false)
    private UUID productVersionId;

    @Column(name = "coverage_band", length = 50)
    private String coverageBand;

    @Column(name = "age_band", length = 50)
    private String ageBand;

    @Column(name = "occupation_class", length = 100)
    private String occupationClass;

    @Column(name = "base_premium", nullable = false, precision = 19, scale = 4)
    private BigDecimal basePremium;
}
