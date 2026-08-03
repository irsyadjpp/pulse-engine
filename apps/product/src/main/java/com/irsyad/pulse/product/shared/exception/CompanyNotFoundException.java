package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class CompanyNotFoundException extends ProductCatalogException {
    public CompanyNotFoundException(String message) {
        super(ErrorCode.COMPANY_NOT_FOUND, message);
    }
}
