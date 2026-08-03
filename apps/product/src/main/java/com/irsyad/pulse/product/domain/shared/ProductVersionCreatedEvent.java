package com.irsyad.pulse.product.domain.shared;

import com.irsyad.pulse.product.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductVersionCreatedEvent(UUID eventId, Instant occurredAt, UUID productId, int oldVersion, int newVersion) implements DomainEvent {
    public static ProductVersionCreatedEvent of(UUID productId, int oldVersion, int newVersion) {
        return new ProductVersionCreatedEvent(UUID.randomUUID(), Instant.now(), productId, oldVersion, newVersion);
    }
}
