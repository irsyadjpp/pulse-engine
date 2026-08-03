package com.irsyad.pulse.product.api.product;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Update Product request (FSD_02 FR-02-02).
 * Product Code and Company are immutable.
 */
public record UpdateProductRequest(
        @NotBlank String productName,
        String category,
        LocalDate effectiveDate,
        LocalDate expiryDate) {
}
