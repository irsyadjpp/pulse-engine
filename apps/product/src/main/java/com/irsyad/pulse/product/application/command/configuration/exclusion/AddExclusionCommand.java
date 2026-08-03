package com.irsyad.pulse.product.application.command.configuration.exclusion;

import java.util.UUID;

/**
 * Command to add an Exclusion to a Draft product (FSD_03 Section 7).
 */
public record AddExclusionCommand(
        UUID productId,
        String description) {
}
