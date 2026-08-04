package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

/**
 * Thrown when a Product is created for an INACTIVE Insurance Company (BR-021).
 */
public class CompanyInactiveException extends ProductCatalogException {
    public CompanyInactiveException(String message) {
        super(ErrorCode.COMPANY_INACTIVE, message);
    }
}