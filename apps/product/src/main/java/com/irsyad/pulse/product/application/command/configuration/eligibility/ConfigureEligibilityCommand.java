package com.irsyad.pulse.product.application.command.configuration.eligibility;

import java.util.UUID;

/**
 * Command to configure Eligibility for a product (FSD_03 Section 8).
 * Required before publish (BR-010).
 */
public record ConfigureEligibilityCommand(
        UUID productId,
        Integer minimumAge,
        Integer maximumAge,
        String occupationClass,
        String nationality,
        String residency) {
}
