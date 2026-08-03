package com.irsyad.pulse.orchestrator.messaging.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Standard Kafka message header for Pulse Engine events.
 * <p>
 * Every event published to Kafka must include these headers for
 * traceability, observability, and event sourcing.
 */
@Setter
@Getter
@Builder
public class EventHeader {

    private UUID eventId;
    private String eventType;
    private UUID correlationId;
    private UUID causationId;
    private String traceId;
    private String producer;
    private Instant createdAt;
    private int version;
}