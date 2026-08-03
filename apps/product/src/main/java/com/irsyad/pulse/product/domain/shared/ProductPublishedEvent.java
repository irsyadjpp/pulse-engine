package com.irsyad.pulse.product.domain.shared;

import com.irsyad.pulse.product.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductPublishedEvent(UUID eventId, Instant occurredAt, UUID productId, int version) implements DomainEvent {
    public static ProductPublishedEvent of(UUID productId, int version) {
        return new ProductPublishedEvent(UUID.randomUUID(), Instant.now(), productId, version);
    }
}
