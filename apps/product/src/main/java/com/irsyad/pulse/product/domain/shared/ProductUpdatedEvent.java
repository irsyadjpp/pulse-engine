package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

public record ProductUpdatedEvent(UUID eventId, Instant occurredAt, UUID productId) implements DomainEvent {
    public static ProductUpdatedEvent of(UUID productId) {
        return new ProductUpdatedEvent(UUID.randomUUID(), Instant.now(), productId);
    }
}
