package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class InvalidTransitionException extends ProductCatalogException {
    public InvalidTransitionException(String message) {
        super(ErrorCode.INVALID_TRANSITION, message);
    }
}
