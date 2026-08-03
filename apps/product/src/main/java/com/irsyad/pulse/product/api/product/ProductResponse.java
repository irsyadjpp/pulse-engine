package com.irsyad.pulse.product.api.product;

import com.irsyad.pulse.product.domain.shared.ProductStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Product detail response (FSD_04 Section 10).
 */
public record ProductResponse(
        UUID productId,
        UUID companyId,
        String productCode,
        String productName,
        String category,
        int version,
        ProductStatus status,
        LocalDate effectiveDate,
        LocalDate expiryDate) {
}
