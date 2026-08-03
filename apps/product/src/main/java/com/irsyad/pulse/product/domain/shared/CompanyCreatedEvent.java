package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

public record CompanyCreatedEvent(UUID eventId, Instant occurredAt, UUID companyId, String companyCode) implements DomainEvent {
    public static CompanyCreatedEvent of(UUID companyId, String companyCode) {
        return new CompanyCreatedEvent(UUID.randomUUID(), Instant.now(), companyId, companyCode);
    }
}
