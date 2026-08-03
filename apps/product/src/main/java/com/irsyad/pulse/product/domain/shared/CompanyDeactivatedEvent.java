package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

public record CompanyDeactivatedEvent(UUID eventId, Instant occurredAt, UUID companyId) implements DomainEvent {
    public static CompanyDeactivatedEvent of(UUID companyId) {
        return new CompanyDeactivatedEvent(UUID.randomUUID(), Instant.now(), companyId);
    }
}
