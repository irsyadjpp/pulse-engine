package com.irsyad.pulse.product.api.version;

import com.irsyad.pulse.product.domain.shared.ProductStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record VersionResponse(
        UUID productVersionId,
        UUID productId,
        int version,
        ProductStatus status,
        LocalDate effectiveDate,
        Instant publishedDate,
        Instant createdAt,
        String createdBy) {
}
