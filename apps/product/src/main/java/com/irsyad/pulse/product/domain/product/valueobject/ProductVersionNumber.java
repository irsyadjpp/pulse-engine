package com.irsyad.pulse.product.domain.product.valueobject;

public record ProductVersionNumber(Integer value) {
    public static ProductVersionNumber initial() {
        return new ProductVersionNumber(1);
    }
    public ProductVersionNumber next() {
        return new ProductVersionNumber(this.value + 1);
    }
}
