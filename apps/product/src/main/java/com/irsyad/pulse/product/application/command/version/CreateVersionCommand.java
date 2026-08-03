package com.irsyad.pulse.product.application.command.version;

import java.util.UUID;

/**
 * Command to create a new Product Version (FSD_05 Section 9, BR-005).
 * Triggered when a Published product is changed.
 */
public record CreateVersionCommand(
        UUID productId,
        String createdBy) {
}
