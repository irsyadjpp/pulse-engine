package com.irsyad.pulse.orchestrator.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "process_audit_log", schema = "orchestrator")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProcessAuditLogEntity extends PanacheEntityBase {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "process_id", nullable = false)
    private String processId;

    @Column(name = "node_name")
    private String nodeName;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "event_data", columnDefinition = "TEXT")
    private String eventData;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    // Custom constructor for default event time
    public ProcessAuditLogEntity(String processId, String nodeName, String eventType, String correlationId, String eventData) {
        this.processId = processId;
        this.nodeName = nodeName;
        this.eventType = eventType;
        this.correlationId = correlationId;
        this.eventData = eventData;
        this.eventTime = Instant.now();
    }
}