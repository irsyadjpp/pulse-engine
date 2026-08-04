package com.irsyad.pulse.product.domain.product.premium;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;
/**
 * Premium configuration metadata (FSD_03 Section 9).
 * Only metadata is managed; calculation is done by Premium Engine.
 */
@Getter
@Builder
public class PremiumConfiguration {
    private final UUID premiumConfigurationId;
    private final UUID productVersionId;
    private String coverageBand;
    private String ageBand;
    private String occupationClass;
    private BigDecimal basePremium;
}
