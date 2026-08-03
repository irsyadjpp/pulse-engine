package com.irsyad.pulse.product.domain.shared;

import java.time.Instant;
import java.util.UUID;

/**
 * Base sealed interface for all Domain Events (TSD_02 Section 13, Section 26).
 */
public sealed interface DomainEvent
        permits CompanyCreatedEvent,
        CompanyUpdatedEvent,
        CompanyActivatedEvent,
        CompanyDeactivatedEvent,
        ProductCreatedEvent,
        ProductUpdatedEvent,
        ProductPublishedEvent,
        ProductArchivedEvent,
        ProductVersionCreatedEvent,
        ConfigurationUpdatedEvent {

    UUID eventId();

    Instant occurredAt();
}
