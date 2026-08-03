package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

public record CompanyActivatedEvent(UUID eventId, Instant occurredAt, UUID companyId) implements DomainEvent {
    public static CompanyActivatedEvent of(UUID companyId) {
        return new CompanyActivatedEvent(UUID.randomUUID(), Instant.now(), companyId);
    }
}
