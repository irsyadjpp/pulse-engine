package com.irsyad.pulse.engine.event;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Builder for {@link CheckoutCompletedEvent}.
 */
public class CheckoutCompletedEventBuilder {
    private final CheckoutCompletedEvent event = new CheckoutCompletedEvent();

    public CheckoutCompletedEventBuilder eventId(String eventId) {
        event.setEventId(eventId);
        return this;
    }

    public CheckoutCompletedEventBuilder processId(String processId) {
        event.setProcessId(processId);
        return this;
    }

    public CheckoutCompletedEventBuilder businessKey(String businessKey) {
        event.setBusinessKey(businessKey);
        return this;
    }

    public CheckoutCompletedEventBuilder customerId(String customerId) {
        event.setCustomerId(customerId);
        return this;
    }

    public CheckoutCompletedEventBuilder orderId(String orderId) {
        event.setOrderId(orderId);
        return this;
    }

    public CheckoutCompletedEventBuilder amount(BigDecimal amount) {
        event.setAmount(amount);
        return this;
    }

    public CheckoutCompletedEventBuilder decision(String decision) {
        event.setDecision(decision);
        return this;
    }

    public CheckoutCompletedEventBuilder riskLevel(String riskLevel) {
        event.setRiskLevel(riskLevel);
        return this;
    }

    public CheckoutCompletedEventBuilder reasonCode(String reasonCode) {
        event.setReasonCode(reasonCode);
        return this;
    }

    public CheckoutCompletedEventBuilder priority(String priority) {
        event.setPriority(priority);
        return this;
    }

    public CheckoutCompletedEventBuilder identityStatus(String identityStatus) {
        event.setIdentityStatus(identityStatus);
        return this;
    }

    public CheckoutCompletedEventBuilder dukcapilStatus(String dukcapilStatus) {
        event.setDukcapilStatus(dukcapilStatus);
        return this;
    }

    public CheckoutCompletedEventBuilder kycStatus(String kycStatus) {
        event.setKycStatus(kycStatus);
        return this;
    }

    public CheckoutCompletedEventBuilder identityRisk(String identityRisk) {
        event.setIdentityRisk(identityRisk);
        return this;
    }

    public CheckoutCompletedEventBuilder transactionRisk(String transactionRisk) {
        event.setTransactionRisk(transactionRisk);
        return this;
    }

    public CheckoutCompletedEventBuilder overallRisk(String overallRisk) {
        event.setOverallRisk(overallRisk);
        return this;
    }

    public CheckoutCompletedEventBuilder velocityRisk(Integer velocityRisk) {
        event.setVelocityRisk(velocityRisk);
        return this;
    }

    public CheckoutCompletedEventBuilder fraudScore(Integer fraudScore) {
        event.setFraudScore(fraudScore);
        return this;
    }

    public CheckoutCompletedEventBuilder timestamp(Instant timestamp) {
        event.setTimestamp(timestamp);
        return this;
    }

    public CheckoutCompletedEvent build() {
        return event;
    }
}