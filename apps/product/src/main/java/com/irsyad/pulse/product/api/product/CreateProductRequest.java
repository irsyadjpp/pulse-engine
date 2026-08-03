package com.irsyad.pulse.product.api.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Create Product request (FSD_02 FR-02-01).
 */
public record CreateProductRequest(
        @NotNull UUID companyId,
        @NotBlank String productCode,
        @NotBlank String productName,
        String category,
        LocalDate effectiveDate,
        LocalDate expiryDate) {
}
