package com.irsyad.pulse.orchestrator.domain.valueobject;

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