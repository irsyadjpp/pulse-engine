package com.irsyad.pulse.orchestrator.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "process_message", schema = "orchestrator")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProcessMessageEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "process_id", nullable = false)
    private String processId;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "status", nullable = false)
    private String status; // PENDING, SUCCESS, FAILED

    @Builder.Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Custom constructor for default values
    public ProcessMessageEntity(String processId, String topic, String eventName, String payload, String status) {
        this.processId = processId;
        this.topic = topic;
        this.eventName = eventName;
        this.payload = payload;
        this.status = status;
        this.createdAt = Instant.now();
        this.retryCount = 0;
    }

    // Custom method
    public void incrementRetryCount() {
        this.retryCount++;
    }
}