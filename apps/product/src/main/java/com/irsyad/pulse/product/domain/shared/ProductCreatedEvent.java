package com.irsyad.pulse.product.domain.shared;

import com.irsyad.pulse.product.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductCreatedEvent(UUID eventId, Instant occurredAt, UUID productId, UUID companyId, String productCode) implements DomainEvent {
    public static ProductCreatedEvent of(UUID productId, UUID companyId, String productCode) {
        return new ProductCreatedEvent(UUID.randomUUID(), Instant.now(), productId, companyId, productCode);
    }
}
