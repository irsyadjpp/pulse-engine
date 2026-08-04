package com.irsyad.pulse.product.domain.product.exclusion;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
/**
 * Exclusion configuration (FSD_03 Section 7).
 * Child entity of the Product aggregate.
 */
@Getter
@Builder
public class Exclusion {
    private final UUID exclusionId;
    private final UUID productVersionId;
    private String description;
}
