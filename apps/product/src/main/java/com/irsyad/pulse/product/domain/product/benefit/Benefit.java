package com.irsyad.pulse.product.domain.product.benefit;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;
/**
 * Benefit configuration (FSD_03 Section 6).
 * Child entity of the Product aggregate.
 */
@Getter
@Builder
public class Benefit {
    private final UUID benefitId;
    private final UUID productVersionId;
    private String benefitName;
    private String description;
    private BigDecimal maximumLimit;
}
