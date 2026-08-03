package com.irsyad.pulse.product.domain.product.valueobject;

import java.util.UUID;

public record ProductId(UUID value) {
    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
