package com.irsyad.pulse.product.infrastructure.persistence.eligibility;

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

import java.util.UUID;

/**
 * JPA entity mapping the eligibility_configuration table (Appendix O).
 * Child entity of the product aggregate.
 */
@Entity
@Table(name = "eligibility_configuration")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EligibilityJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "eligibility_id", nullable = false, updatable = false)
    private UUID eligibilityId;

    @Column(name = "product_version_id", nullable = false, updatable = false)
    private UUID productVersionId;

    @Column(name = "minimum_age")
    private Integer minimumAge;

    @Column(name = "maximum_age")
    private Integer maximumAge;

    @Column(name = "occupation_class", length = 100)
    private String occupationClass;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "residency", length = 100)
    private String residency;
}
