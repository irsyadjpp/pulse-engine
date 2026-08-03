package com.irsyad.pulse.product.application.command.configuration.benefit;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Command to add a Benefit to a Draft product (FSD_03 Section 6).
 */
public record AddBenefitCommand(
        UUID productId,
        String benefitName,
        String description,
        BigDecimal maximumLimit) {
}
