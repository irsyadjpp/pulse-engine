package com.irsyad.pulse.product.domain.product.eligibility;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Eligibility configuration (FSD_03 Section 8).
 * Child entity of the Product aggregate.
 */
@Getter
@Builder
public class Eligibility {

    private final UUID eligibilityId;
    private final UUID productVersionId;
    private Integer minimumAge;
    private Integer maximumAge;
    private String occupationClass;
    private String nationality;
    private String residency;
}
