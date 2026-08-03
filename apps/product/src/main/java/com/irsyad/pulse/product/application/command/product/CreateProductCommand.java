package com.irsyad.pulse.product.application.command.product;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Command to create a Product (FSD_02 FR-02-01).
 * Creates a Draft product with Version = 1.
 */
public record CreateProductCommand(
        UUID companyId,
        String productCode,
        String productName,
        String category,
        LocalDate effectiveDate,
        LocalDate expiryDate) {
}
