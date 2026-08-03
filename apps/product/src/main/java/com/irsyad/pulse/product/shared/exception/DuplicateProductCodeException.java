package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class DuplicateProductCodeException extends ProductCatalogException {
    public DuplicateProductCodeException(String message) {
        super(ErrorCode.DUPLICATE_PRODUCT_CODE, message);
    }
}
