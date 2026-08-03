package com.irsyad.pulse.engine.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checkout_insight", schema = "pulse_engine")
public class CheckoutInsightEntity {

    // Getters and Setters
    @Id
    @Column(name = "checkout_id")
    private String checkoutId;

    @Column(name = "process_id", nullable = false)
    private String processId;

    @Column(name = "event_id", unique = true)
    private String eventId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "decision", nullable = false)
    private String decision;

    @Column(name = "confidence", nullable = false)
    private String confidence;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "explainability_score")
    private Double explainabilityScore;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "insight_type")
    private String insightType;
}
