package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

/**
 * Base exception for all Product Catalog domain errors (TSD_10, TSD_04 Section 18).
 */
public class ProductCatalogException extends RuntimeException {

    protected final ErrorCode errorCode;

    protected ProductCatalogException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode errorCode() {
        return this.errorCode;
    }
}
