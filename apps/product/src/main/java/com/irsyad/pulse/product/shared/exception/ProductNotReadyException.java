package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class ProductNotReadyException extends ProductCatalogException {
    public ProductNotReadyException(String message) {
        super(ErrorCode.PRODUCT_NOT_READY, message);
    }
}
