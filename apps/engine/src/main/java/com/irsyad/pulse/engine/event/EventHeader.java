package com.irsyad.pulse.engine.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Standard Kafka message header for Pulse Engine events.
 * <p>
 * Every event published to Kafka must include these headers for
 * traceability, observability, and event sourcing.
 */
public class EventHeader {

    private UUID eventId;
    private String eventType;
    private UUID correlationId;
    private UUID causationId;
    private String traceId;
    private String producer;
    private Instant createdAt;
    private int version;

    public EventHeader() {
    }

    public EventHeader(UUID eventId, String eventType, UUID correlationId, UUID causationId,
                       String traceId, String producer, Instant createdAt, int version) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.traceId = traceId;
        this.producer = producer;
        this.createdAt = createdAt;
        this.version = version;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventIdAsString() {
        return eventId != null ? eventId.toString() : null;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public UUID getCausationId() {
        return causationId;
    }

    public void setCausationId(UUID causationId) {
        this.causationId = causationId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static EventHeaderBuilder builder() {
        return new EventHeaderBuilder();
    }
}