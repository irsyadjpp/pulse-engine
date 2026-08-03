package com.irsyad.pulse.product.domain.shared;

import com.irsyad.pulse.product.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductArchivedEvent(UUID eventId, Instant occurredAt, UUID productId, int version) implements DomainEvent {
    public static ProductArchivedEvent of(UUID productId, int version) {
        return new ProductArchivedEvent(UUID.randomUUID(), Instant.now(), productId, version);
    }
}
