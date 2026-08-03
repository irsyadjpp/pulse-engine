package com.irsyad.pulse.engine.model.event;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Event published by the Orchestrator when a checkout process is completed with
 * DMN decision.
 * <p>
 * Topic: {@code checkout.completed}
 * Key: {@code orderId} (for partition ordering)
 * <p>
 * This event contains the decision from the DMN Risk Decision service and is
 * consumed
 * by the Pulse Engine (Intelligence Service) for further processing.
 */
public class CheckoutCompletedEvent {

    private EventHeader header;
    private String processId;
    private String businessKey;
    private String orderId;
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

    public CheckoutCompletedEvent() {
    }

    public CheckoutCompletedEvent(EventHeader header, String processId, String businessKey, String orderId,
            String customerId,
            BigDecimal amount, String paymentMethod,
            String decision, String riskLevel, boolean reviewRequired,
            String priority, String reasonCode,
            BigDecimal confidenceScore,
            Integer processingTimeMs, Instant decisionTimestamp) {
        this.header = header;
        this.processId = processId;
        this.businessKey = businessKey;
        this.orderId = orderId;
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

    public EventHeader getHeader() {
        return header;
    }

    public void setHeader(EventHeader header) {
        this.header = header;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public boolean isReviewRequired() {
        return reviewRequired;
    }

    public void setReviewRequired(boolean reviewRequired) {
        this.reviewRequired = reviewRequired;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public BigDecimal getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(BigDecimal confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Integer getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(Integer processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public Instant getDecisionTimestamp() {
        return decisionTimestamp;
    }

    public void setDecisionTimestamp(Instant decisionTimestamp) {
        this.decisionTimestamp = decisionTimestamp;
    }

    public String getEventId() {
        return header != null ? header.getEventIdAsString() : null;
    }
}
