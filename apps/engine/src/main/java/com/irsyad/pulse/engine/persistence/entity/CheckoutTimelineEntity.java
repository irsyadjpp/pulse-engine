package com.irsyad.pulse.engine.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "checkout_timeline", schema = "pulse_engine")
public class CheckoutTimelineEntity {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "checkout_id", nullable = false)
    private String checkoutId;

    @Column(name = "capability", nullable = false)
    private String capability;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "message")
    private String message;

    @Column(name = "processing_time_ms")
    private Integer processingTimeMs;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Default constructor
    public CheckoutTimelineEntity() {
    }

    // Builder pattern
    public static CheckoutTimelineEntityBuilder builder() {
        return new CheckoutTimelineEntityBuilder();
    }
}