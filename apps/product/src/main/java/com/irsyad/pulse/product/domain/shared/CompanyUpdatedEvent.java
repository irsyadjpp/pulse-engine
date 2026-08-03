package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

public record CompanyUpdatedEvent(UUID eventId, Instant occurredAt, UUID companyId) implements DomainEvent {
    public static CompanyUpdatedEvent of(UUID companyId) {
        return new CompanyUpdatedEvent(UUID.randomUUID(), Instant.now(), companyId);
    }
}
