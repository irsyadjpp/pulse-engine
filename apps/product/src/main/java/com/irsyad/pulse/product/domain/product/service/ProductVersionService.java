package com.irsyad.pulse.product.domain.product.service;

import com.irsyad.pulse.product.domain.product.valueobject.ProductVersionNumber;

public final class ProductVersionService {
    private ProductVersionService() {}
    public static ProductVersionNumber nextVersion(ProductVersionNumber current) {
        return current.next();
    }
}
