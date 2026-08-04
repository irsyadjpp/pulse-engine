package com.irsyad.pulse.product.domain.product.document;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
/**
 * Product document metadata (FSD_03 Section 10).
 * Child entity of the Product aggregate.
 */
@Getter
@Builder
public class ProductDocument {
    private final UUID documentId;
    private final UUID productVersionId;
    private String documentName;
    private String documentType;
    private String storageReference;
}
