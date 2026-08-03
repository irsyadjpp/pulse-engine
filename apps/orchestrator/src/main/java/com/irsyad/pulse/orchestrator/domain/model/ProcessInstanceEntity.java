package com.irsyad.pulse.orchestrator.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "process_instance", schema = "orchestrator")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProcessInstanceEntity extends PanacheEntityBase {

    @Id
    @Column(name = "process_id")
    private String processId;

    @Column(name = "business_key")
    private String businessKey;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "kyc_reference")
    private String kycReference;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "workflow_status")
    private String workflowStatus;

    @Column(name = "current_node")
    private String currentNode;

    @Column(name = "current_step")
    private String currentStep;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "decision")
    private String decision;

    @Column(name = "decision_reason")
    private String decisionReason;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Custom setters with additional logic
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
        this.updatedAt = Instant.now();
    }
}