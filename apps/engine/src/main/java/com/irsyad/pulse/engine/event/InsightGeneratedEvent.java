package com.irsyad.pulse.engine.event;

import java.time.Instant;

/**
 * Event published by Pulse Engine (Intelligence Service) when insights are
 * generated.
 * <p>
 * Topic: {@code insight.generated}
 * Key: {@code orderId} (for partition ordering)
 */
public class InsightGeneratedEvent {

    private EventHeader header;
    private String orderId;
    private String insightType;
    private String insightValue;
    private String severity;
    private String generatedBy;
    private Instant generatedAt;

    public InsightGeneratedEvent() {
    }

    public InsightGeneratedEvent(EventHeader header, String orderId, String insightType,
            String insightValue, String severity, String generatedBy,
            Instant generatedAt) {
        this.header = header;
        this.orderId = orderId;
        this.insightType = insightType;
        this.insightValue = insightValue;
        this.severity = severity;
        this.generatedBy = generatedBy;
        this.generatedAt = generatedAt;
    }

    public EventHeader getHeader() {
        return header;
    }

    public void setHeader(EventHeader header) {
        this.header = header;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInsightType() {
        return insightType;
    }

    public void setInsightType(String insightType) {
        this.insightType = insightType;
    }

    public String getInsightValue() {
        return insightValue;
    }

    public void setInsightValue(String insightValue) {
        this.insightValue = insightValue;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
