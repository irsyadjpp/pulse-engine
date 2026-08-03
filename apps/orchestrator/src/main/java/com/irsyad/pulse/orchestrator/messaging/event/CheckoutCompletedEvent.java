package com.irsyad.pulse.orchestrator.messaging.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Event published by the Orchestrator when a checkout process is completed with
 * DMN decision.
 * <p>
 * Topic: {@code checkout.completed}
 * Key: {@code checkoutId} (for partition ordering)
 * <p>
 * This event contains the decision from the DMN Risk Decision service and is
 * consumed
 * by the Pulse Engine (Intelligence Service) for further processing.
 */
@Setter
@Getter
public class CheckoutCompletedEvent {

    private EventHeader header;
    private String processId;
    private String businessKey;
    private String checkoutId;
    private String customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String decision; // APPROVE, REVIEW, REJECT from DMN
    private String riskLevel; // LOW, MEDIUM, HIGH from DMN
    private boolean reviewRequired; // from DMN
    private String priority; // P1, P2, P3 from DMN
    private String reasonCode; // e.g., FIRST_PURCHASE_HIGH_AMOUNT from DMN
    private BigDecimal confidenceScore;
    private Integer processingTimeMs;
    private Instant decisionTimestamp;

    public CheckoutCompletedEvent(EventHeader header, String processId, String businessKey, String checkoutId,
            String customerId,
            BigDecimal amount, String paymentMethod,
            String decision, String riskLevel, boolean reviewRequired,
            String priority, String reasonCode,
            BigDecimal confidenceScore,
            Integer processingTimeMs, Instant decisionTimestamp) {
        this.header = header;
        this.processId = processId;
        this.businessKey = businessKey;
        this.checkoutId = checkoutId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.decision = decision;
        this.riskLevel = riskLevel;
        this.reviewRequired = reviewRequired;
        this.priority = priority;
        this.reasonCode = reasonCode;
        this.confidenceScore = confidenceScore;
        this.processingTimeMs = processingTimeMs;
        this.decisionTimestamp = decisionTimestamp;
    }

}
