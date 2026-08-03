package com.irsyad.pulse.product.application.command.product;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Command to update a Draft product (FSD_02 FR-02-02).
 * Product Code and Company are not editable.
 */
public record UpdateProductCommand(
        UUID productId,
        String productName,
        String category,
        LocalDate effectiveDate,
        LocalDate expiryDate) {
}
