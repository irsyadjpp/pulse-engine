package com.irsyad.pulse.engine.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "checkout_explanation", schema = "pulse_engine")
public class CheckoutExplanationEntity {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "checkout_id", nullable = false)
    private String checkoutId;

    @Column(name = "explanation_type", nullable = false)
    private String explanationType;

    @Column(name = "explanation", nullable = false, columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Default constructor
    public CheckoutExplanationEntity() {
    }

    // Builder pattern
    public static CheckoutExplanationEntityBuilder builder() {
        return new CheckoutExplanationEntityBuilder();
    }
}