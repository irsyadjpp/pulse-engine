package com.irsyad.pulse.product.domain.shared;

/**
 * Business status for a Product.
 *
 * <p>Referenced from FSD_02 (Section 10 Product Lifecycle) and Appendix K.
 * The lifecycle is Draft -> Published -> Archived.
 */
public enum ProductStatus {

    DRAFT,
    PUBLISHED,
    ARCHIVED
}