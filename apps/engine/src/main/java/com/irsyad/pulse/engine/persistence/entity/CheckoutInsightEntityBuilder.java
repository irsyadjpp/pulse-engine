package com.irsyad.pulse.engine.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Builder for {@link CheckoutInsightEntity}.
 */
public class CheckoutInsightEntityBuilder {
    private final CheckoutInsightEntity entity = new CheckoutInsightEntity();

    public CheckoutInsightEntityBuilder checkoutId(String checkoutId) {
        entity.setCheckoutId(checkoutId);
        return this;
    }

    public CheckoutInsightEntityBuilder processId(String processId) {
        entity.setProcessId(processId);
        return this;
    }

    public CheckoutInsightEntityBuilder eventId(String eventId) {
        entity.setEventId(eventId);
        return this;
    }

    public CheckoutInsightEntityBuilder customerId(String customerId) {
        entity.setCustomerId(customerId);
        return this;
    }

    public CheckoutInsightEntityBuilder orderId(String orderId) {
        entity.setOrderId(orderId);
        return this;
    }

    public CheckoutInsightEntityBuilder decision(String decision) {
        entity.setDecision(decision);
        return this;
    }

    public CheckoutInsightEntityBuilder confidence(String confidence) {
        entity.setConfidence(confidence);
        return this;
    }

    public CheckoutInsightEntityBuilder riskLevel(String riskLevel) {
        entity.setRiskLevel(riskLevel);
        return this;
    }

    public CheckoutInsightEntityBuilder explainabilityScore(Double explainabilityScore) {
        entity.setExplainabilityScore(explainabilityScore);
        return this;
    }

    public CheckoutInsightEntityBuilder totalAmount(BigDecimal totalAmount) {
        entity.setTotalAmount(totalAmount);
        return this;
    }

    public CheckoutInsightEntityBuilder processedAt(Instant processedAt) {
        entity.setProcessedAt(processedAt);
        return this;
    }

    public CheckoutInsightEntityBuilder createdAt(Instant createdAt) {
        entity.setCreatedAt(createdAt);
        return this;
    }

    public CheckoutInsightEntityBuilder updatedAt(Instant updatedAt) {
        entity.setUpdatedAt(updatedAt);
        return this;
    }

    public CheckoutInsightEntityBuilder insightType(String insightType) {
        entity.setInsightType(insightType);
        return this;
    }

    public CheckoutInsightEntity build() {
        return entity;
    }
}