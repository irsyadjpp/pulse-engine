package com.irsyad.pulse.product.application.query.product;

import com.irsyad.pulse.product.domain.shared.ProductStatus;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Query for searching Products (FSD_04 FR-04-01, TSD_04 Section 12-14).
 */
public record SearchProductQuery(
        UUID companyId,
        String productCode,
        String productName,
        String category,
        ProductStatus status,
        LocalDate effectiveDate,
        String sort,
        int page,
        int size) {
}