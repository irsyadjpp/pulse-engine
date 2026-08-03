package com.irsyad.pulse.product.shared.exception;

import com.irsyad.pulse.product.api.common.ErrorCode;

public class VersionNotFoundException extends ProductCatalogException {
    public VersionNotFoundException(String message) {
        super(ErrorCode.VERSION_NOT_FOUND, message);
    }
}
