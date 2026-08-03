package com.irsyad.pulse.engine.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Builder for {@link EventHeader}.
 */
public class EventHeaderBuilder {
    private final EventHeader header = new EventHeader();

    public EventHeaderBuilder eventId(UUID eventId) {
        header.setEventId(eventId);
        return this;
    }

    public EventHeaderBuilder eventType(String eventType) {
        header.setEventType(eventType);
        return this;
    }

    public EventHeaderBuilder correlationId(UUID correlationId) {
        header.setCorrelationId(correlationId);
        return this;
    }

    public EventHeaderBuilder causationId(UUID causationId) {
        header.setCausationId(causationId);
        return this;
    }

    public EventHeaderBuilder traceId(String traceId) {
        header.setTraceId(traceId);
        return this;
    }

    public EventHeaderBuilder producer(String producer) {
        header.setProducer(producer);
        return this;
    }

    public EventHeaderBuilder createdAt(Instant createdAt) {
        header.setCreatedAt(createdAt);
        return this;
    }

    public EventHeaderBuilder version(int version) {
        header.setVersion(version);
        return this;
    }

    public EventHeader build() {
        return header;
    }
}