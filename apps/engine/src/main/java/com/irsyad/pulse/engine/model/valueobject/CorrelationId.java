package com.irsyad.pulse.engine.model.valueobject;

import java.util.Objects;
import java.util.UUID;

public record CorrelationId(UUID value) {
    public CorrelationId {
        Objects.requireNonNull(value, "correlationId must not be null");
    }

    public static CorrelationId generate() {
        return new CorrelationId(UUID.randomUUID());
    }
}