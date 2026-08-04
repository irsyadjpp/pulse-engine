package com.irsyad.pulse.product.domain.version;

import com.irsyad.pulse.product.domain.shared.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Immutable Product Version snapshot (FSD_05 Section 5, 10, 17).
 *
 * <p>Published versions are immutable. Versions are never reset and never deleted.
 */
@Getter
@Builder
public class ProductVersion {

    private final UUID productVersionId;
    private final UUID productId;
    private final int version;
    private ProductStatus status;
    private final LocalDate effectiveDate;
    private final Instant publishedDate;
    private final String snapshot;
    private final Instant createdAt;
    private final String createdBy;
}
