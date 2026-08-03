package com.irsyad.pulse.product.domain.shared;

import com.irsyad.pulse.product.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ConfigurationUpdatedEvent(UUID eventId, Instant occurredAt, UUID productId, String configurationType) implements DomainEvent {
    public static ConfigurationUpdatedEvent of(UUID productId, String configurationType) {
        return new ConfigurationUpdatedEvent(UUID.randomUUID(), Instant.now(), productId, configurationType);
    }
}
