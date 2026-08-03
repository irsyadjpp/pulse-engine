package com.irsyad.pulse.engine.pipeline;

import com.irsyad.pulse.engine.event.CheckoutCompletedEvent;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ObservationContext {
    private String correlationId;
    private String traceId;
    private CheckoutCompletedEvent event;
    private Instant observedAt;
    private String normalizedDecision;
    private String normalizedPaymentMethod;

    public ObservationContext(CheckoutCompletedEvent event) {
        this.event = event;
        this.correlationId = java.util.UUID.randomUUID().toString();
        this.traceId = java.util.UUID.randomUUID().toString();
        this.observedAt = Instant.now();
        
        // Normalize payload
        this.normalizedDecision = event.getDecision() != null ? event.getDecision().toUpperCase() : "PENDING";
        this.normalizedPaymentMethod = extractPaymentMethod(event.getDecision());
    }

    private String extractPaymentMethod(String decision) {
        // Simple extraction logic - in real implementation, this would parse from event payload
        // For now, return default
        return "VA";
    }

}
