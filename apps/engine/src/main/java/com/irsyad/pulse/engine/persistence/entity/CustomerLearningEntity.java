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
@Table(name = "customer_learning", schema = "pulse_engine")
public class CustomerLearningEntity {

    // Getters and Setters
    @Id
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "purchase_count", nullable = false)
    private Integer purchaseCount;

    @Column(name = "successful_checkout", nullable = false)
    private Integer successfulCheckout;

    @Column(name = "rejected_checkout", nullable = false)
    private Integer rejectedCheckout;

    @Column(name = "average_amount")
    private BigDecimal averageAmount;

    @Column(name = "highest_amount")
    private BigDecimal highestAmount;

    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;

    @Column(name = "customer_segment")
    private String customerSegment;

    @Column(name = "last_checkout_time")
    private Instant lastCheckoutTime;

    @Column(name = "learning_version", nullable = false)
    private Integer learningVersion;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
