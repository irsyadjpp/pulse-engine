package com.irsyad.pulse.orchestrator.process.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
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

        String decision = model.getCheckoutStatus() != null ? model.getCheckoutStatus().name() : "COMPLETED";
        String riskLevel = model.getRisk() != null ? model.getRisk().getRiskLevel() : "LOW";
        String reasonCode = model.getRisk() != null ? model.getRisk().getReasonCode() : null;

        try {
            // Publish flat JSON matching the Engine's CheckoutCompletedEvent schema
            // so the Engine's ObjectMapperDeserializer can deserialize it directly.
            Map<String, Object> payload = new HashMap<>();
            payload.put("eventId", UUID.randomUUID().toString());
            payload.put("processId", model.getProcessId());
            payload.put("businessKey", model.getBusinessKey());
            payload.put("customerId", request.getCustomerId());
            payload.put("orderId", request.getOrderId());
            payload.put("amount", request.getAmount());
            payload.put("decision", decision);
            payload.put("riskLevel", riskLevel);
            payload.put("reasonCode", reasonCode);
            payload.put("priority", "P1");
            payload.put("timestamp", Instant.now().toString());

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