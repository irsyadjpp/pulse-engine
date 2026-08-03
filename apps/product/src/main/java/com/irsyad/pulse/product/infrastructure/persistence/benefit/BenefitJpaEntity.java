package com.irsyad.pulse.product.infrastructure.persistence.benefit;

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
 * JPA entity mapping the benefit table (Appendix O).
 * Child entity of the product aggregate.
 */
@Entity
@Table(name = "benefit")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BenefitJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "benefit_id", nullable = false, updatable = false)
    private UUID benefitId;

    @Column(name = "product_version_id", nullable = false, updatable = false)
    private UUID productVersionId;

    @Column(name = "benefit_name", nullable = false, length = 200)
    private String benefitName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "maximum_limit", precision = 19, scale = 4)
    private BigDecimal maximumLimit;
}
