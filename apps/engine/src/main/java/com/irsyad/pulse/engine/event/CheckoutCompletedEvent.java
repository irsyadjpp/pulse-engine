package com.irsyad.pulse.engine.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
public class CheckoutCompletedEvent {
    // Getters and Setters
    private String eventId;
    private String processId;
    private String businessKey;
    private String customerId;
    private String orderId;
    private BigDecimal amount;
    private String decision;
    private String riskLevel;
    private String reasonCode;
    private String priority;
    
    // New DRG-specific fields
    private String identityStatus; // MATCH, NOT_MATCH, NOT_FOUND
    private String dukcapilStatus; // VALID, INVALID (Dukcapil verification)
    private String kycStatus; // PASSED, REVIEW, FAILED
    private String identityRisk; // LOW, MEDIUM, HIGH
    private String transactionRisk; // LOW, MEDIUM, HIGH
    private String overallRisk; // LOW, MEDIUM, HIGH
    private Integer velocityRisk;
    private Integer fraudScore;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    // Default constructor
    public CheckoutCompletedEvent() {
    }

    // Builder pattern
    public static CheckoutCompletedEventBuilder builder() {
        return new CheckoutCompletedEventBuilder();
    }
}