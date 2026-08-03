package com.irsyad.pulse.product.application.command.configuration.premium;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command to add a Premium Configuration (FSD_03 Section 9).
 * Required before publish (BR-011).
 */
public record AddPremiumConfigurationCommand(
        UUID productId,
        String coverageBand,
        String ageBand,
        String occupationClass,
        BigDecimal basePremium) {
}
