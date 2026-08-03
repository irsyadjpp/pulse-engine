package com.irsyad.pulse.orchestrator.process.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.messaging.event.CheckoutCompletedEvent;
import com.irsyad.pulse.orchestrator.messaging.event.EventHeader;
import com.irsyad.pulse.orchestrator.messaging.producer.CheckoutCompletedProducer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Delegate for publishing checkout completed event to Kafka.
 * All paths (APPROVE, REVIEW, REJECT) converge here.
 */
@ApplicationScoped
public class PublishCheckoutCompletedDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(PublishCheckoutCompletedDelegate.class);

    @Inject
    CheckoutCompletedProducer producer;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        CheckoutRequest request = model.getRequest();
        if (request == null) {
            throw new IllegalStateException("CheckoutRequest is null in process model");
        }

        LOG.info("Publishing checkout completed event for order {}", request.getOrderId());

        EventHeader header = EventHeader.builder()
                .eventId(UUID.randomUUID())
                .eventType("CHECKOUT_COMPLETED")
                .correlationId(model.getCorrelationId() != null ? UUID.fromString(model.getCorrelationId()) : null)
                .traceId(model.getTraceId())
                .producer("orchestrator")
                .createdAt(Instant.now())
                .version(1)
                .build();

        java.math.BigDecimal confidenceScore = null;
        if (model.getRisk() != null && model.getRisk().getConfidenceScore() != null) {
            confidenceScore = java.math.BigDecimal.valueOf(model.getRisk().getConfidenceScore());
        }

        String decision = model.getCheckoutStatus() != null ? model.getCheckoutStatus().name() : "COMPLETED";
        String riskLevel = model.getRisk() != null ? model.getRisk().getRiskLevel() : "LOW";
        boolean reviewRequired = "REVIEW".equals(decision);
        String reasonCode = model.getRisk() != null ? model.getRisk().getReasonCode() : null;

        CheckoutCompletedEvent event = new CheckoutCompletedEvent(
                header,
                model.getProcessId(),
                model.getBusinessKey(),
                model.getCheckoutId(),
                request.getCustomerId(),
                request.getAmount(),
                request.getPaymentMethod() != null ? request.getPaymentMethod().name() : "VA",
                decision,
                riskLevel,
                reviewRequired,
                "P1",
                reasonCode,
                confidenceScore,
                0,
                Instant.now()
        );

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("event", event);
            payload.put("checkoutStatus", model.getCheckoutStatus() != null ? model.getCheckoutStatus().name() : null);
            payload.put("riskAssessment", model.getRisk());
            payload.put("paymentAuthorization", model.getPayment());
            payload.put("reviewCase", model.getReviewCase());

            String json = objectMapper.writeValueAsString(payload);
            producer.publish(json);
            model.setPublishResult(payload);
            LOG.info("Published checkout completed event for order {} status {}",
                    request.getOrderId(), model.getCheckoutStatus());
        } catch (Exception e) {
            LOG.error("Failed to publish checkout completed event for order {}", request.getOrderId(), e);
            throw new RuntimeException("Failed to publish checkout completed event", e);
        }

        return model;
    }
}
