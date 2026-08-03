package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class DuplicateCompanyCodeException extends ProductCatalogException {
    public DuplicateCompanyCodeException(String message) {
        super(ErrorCode.DUPLICATE_COMPANY_CODE, message);
    }
}
