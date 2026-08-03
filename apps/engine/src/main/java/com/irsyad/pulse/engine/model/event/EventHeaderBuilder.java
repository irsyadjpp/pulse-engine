package com.irsyad.pulse.engine.model.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Builder for {@link EventHeader}.
 */
public final class EventHeaderBuilder {
    private UUID eventId;
    private String eventType;
    private UUID correlationId;
    private UUID causationId;
    private String traceId;
    private String producer;
    private Instant createdAt;
    private int version;

    EventHeaderBuilder() {
    }

    public EventHeaderBuilder eventId(UUID eventId) {
        this.eventId = eventId;
        return this;
    }

    public EventHeaderBuilder eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventHeaderBuilder correlationId(UUID correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public EventHeaderBuilder causationId(UUID causationId) {
        this.causationId = causationId;
        return this;
    }

    public EventHeaderBuilder traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public EventHeaderBuilder producer(String producer) {
        this.producer = producer;
        return this;
    }

    public EventHeaderBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public EventHeaderBuilder version(int version) {
        this.version = version;
        return this;
    }

    public EventHeader build() {
        return new EventHeader(eventId, eventType, correlationId, causationId,
                traceId, producer, createdAt, version);
    }
}