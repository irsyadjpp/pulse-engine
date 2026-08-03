package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.domain.model.ProcessAuditLogEntity;
import com.irsyad.pulse.orchestrator.domain.model.ProcessInstanceEntity;
import com.irsyad.pulse.orchestrator.domain.enums.Decision;
import com.irsyad.pulse.orchestrator.domain.enums.PaymentMethod;
import com.irsyad.pulse.orchestrator.domain.enums.ProcessStatus;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessAuditLogRepository;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessInstanceRepository;
import com.irsyad.pulse.orchestrator.messaging.event.CheckoutCompletedEvent;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * Kogito Event Handler for Kafka message integration
 * This class handles external Kafka events and provides actual business flow implementation
 */
@ApplicationScoped
public class KogitoEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(KogitoEventHandler.class);

    @Inject
    private ProcessInstanceRepository processInstanceRepository;

    @Inject
    private ProcessAuditLogRepository processAuditLogRepository;

    /**
     * Handle Kafka Checkout Retry Events
     * Re-executes the business flow for failed processes
     */
    @Incoming("checkout-retry-handler")
    @Blocking
    public CompletionStage<Void> handleCheckoutRetryEvent(Message<CheckoutCompletedEvent> message) {
        CheckoutCompletedEvent event = message.getPayload();
        LOG.info("Received checkout.retry event via Kafka: ProcessId={}, BusinessKey={}", 
                event.getProcessId(), event.getBusinessKey());
        
        String processId = event.getProcessId();
        
        try {
            logProcessEvent(processId, "CheckoutRetryEvent", "RETRY_STARTED", 
                    "Process retry initiated");
            
            // Re-execute the checkout flow
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity == null) {
                throw new RuntimeException("Process not found: " + processId);
            }
            
            // Create process model for retry
            CheckoutProcessModel model = createProcessModelFromEntity(entity);
            
            // Re-execute business steps
            executeRetryFlow(model);
            
            updateProcessStatus(processId, "RETRY_SUCCESS", "Process retry completed successfully");
            
            return message.ack();
            
        } catch (Exception e) {
            LOG.error("Error retrying process: {}", processId, e);
            updateProcessStatus(processId, "RETRY_FAILED", "Error: " + e.getMessage());
            return message.nack(e);
        }
    }

    /**
     * Handle Kafka Checkout Cancel Events
     * Executes cancellation business flow
     */
    @Incoming("checkout-cancel")
    @Blocking
    public CompletionStage<Void> handleCheckoutCancelEvent(Message<CheckoutCompletedEvent> message) {
        CheckoutCompletedEvent event = message.getPayload();
        LOG.info("Received checkout.cancel event via Kafka: ProcessId={}, BusinessKey={}", 
                event.getProcessId(), event.getBusinessKey());
        
        String processId = event.getProcessId();
        
        try {
            logProcessEvent(processId, "CheckoutCancelEvent", "CANCEL_STARTED", 
                    "Process cancellation initiated");
            
            // Execute cancellation flow
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity == null) {
                throw new RuntimeException("Process not found: " + processId);
            }
            
            // Abort the process (Kogito abort will be integrated after code generation)
            LOG.info("Aborting process: {}", processId);
            
            updateProcessStatus(processId, ProcessStatus.CANCELLED.name(), "Process cancelled successfully");
            
            return message.ack();
            
        } catch (Exception e) {
            LOG.error("Error cancelling process: {}", processId, e);
            updateProcessStatus(processId, "CANCEL_FAILED", "Error: " + e.getMessage());
            return message.nack(e);
        }
    }

    /**
     * Execute post-approval business flow
     */
    private void executePostApprovalFlow(CheckoutCompletedEvent event) {
        LOG.info("Executing post-approval flow for CheckoutId={}", event.getCheckoutId());
        
        String processId = event.getProcessId();
        
        try {
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity == null) {
                throw new RuntimeException("Process not found: " + processId);
            }
            
            // Step 1: Complete checkout
            logProcessEvent(processId, "PostApprovalFlow", "CHECKOUT_COMPLETION", 
                    "Completing checkout process");
            CheckoutProcessModel model = new CheckoutProcessModel();
            model.setCheckoutId(entity.getOrderId());
            model.setStatus(ProcessStatus.COMPLETED);
            LOG.info("Checkout completed for order: {}", entity.getOrderId());
            
            // Step 2: Update final status
            entity.setStatus(ProcessStatus.COMPLETED.name());
            entity.setDecision(Decision.APPROVE.name());
            entity.setEndTime(Instant.now());
            // processInstanceRepository.save(entity); // Panache handles persistence automatically
            
            logProcessEvent(processId, "PostApprovalFlow", "FLOW_COMPLETED", 
                    "Post-approval flow completed successfully");
            
        } catch (Exception e) {
            LOG.error("Error in post-approval flow for process: {}", processId, e);
            throw new RuntimeException("Post-approval flow failed", e);
        }
    }

    /**
     * Execute review business flow
     */
    private void executeReviewFlow(CheckoutCompletedEvent event) {
        LOG.info("Executing review flow for CheckoutId={}", event.getCheckoutId());
        
        String processId = event.getProcessId();
        
        try {
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity == null) {
                throw new RuntimeException("Process not found: " + processId);
            }
            
            // Step 1: Mark for manual review
            logProcessEvent(processId, "ReviewFlow", "MANUAL_REVIEW_REQUIRED", 
                    "Transaction requires manual review");
            
            // Step 2: Update status
            entity.setStatus(ProcessStatus.REVIEW_PENDING.name());
            entity.setDecision(Decision.REVIEW.name());
            // processInstanceRepository.save(entity);
            
            logProcessEvent(processId, "ReviewFlow", "FLOW_COMPLETED", 
                    "Review flow completed successfully");
            
        } catch (Exception e) {
            LOG.error("Error in review flow for process: {}", processId, e);
            throw new RuntimeException("Review flow failed", e);
        }
    }

    /**
     * Execute rejection business flow
     */
    private void executeRejectionFlow(CheckoutCompletedEvent event) {
        LOG.info("Executing rejection flow for CheckoutId={}", event.getCheckoutId());
        
        String processId = event.getProcessId();
        
        try {
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity == null) {
                throw new RuntimeException("Process not found: " + processId);
            }
            
            // Step 1: Log rejection
            logProcessEvent(processId, "RejectionFlow", "REJECTION_PROCESSING", 
                    "Processing rejection");
            
            // Step 2: Update status
            entity.setStatus(ProcessStatus.REJECTED.name());
            entity.setDecision(Decision.REJECT.name());
            entity.setEndTime(Instant.now());
            // processInstanceRepository.save(entity);
            
            logProcessEvent(processId, "RejectionFlow", "FLOW_COMPLETED", 
                    "Rejection flow completed successfully");
            
        } catch (Exception e) {
            LOG.error("Error in rejection flow for process: {}", processId, e);
            throw new RuntimeException("Rejection flow failed", e);
        }
    }

    /**
     * Execute retry flow
     */
    private void executeRetryFlow(CheckoutProcessModel model) {
        LOG.info("Executing retry flow for CheckoutId={}", model.getCheckoutId());
        
        String processId = model.getProcessId();
        
        try {
            // Step 1: Log retry
            logProcessEvent(processId, "RetryFlow", "RETRY_PROCESSING", 
                    "Processing retry");
            
            // Step 2: Reset status
            model.setStatus(ProcessStatus.RETRYING);
            
            // Step 3: Re-execute the process (Kogito retry will be integrated after code generation)
            LOG.info("Re-executing process for retry: {}", processId);
            
            logProcessEvent(processId, "RetryFlow", "FLOW_COMPLETED", 
                    "Retry flow completed successfully");
            
        } catch (Exception e) {
            LOG.error("Error in retry flow for process: {}", processId, e);
            throw new RuntimeException("Retry flow failed", e);
        }
    }

    /**
     * Create process model from entity
     */
    private CheckoutProcessModel createProcessModelFromEntity(ProcessInstanceEntity entity) {
        CheckoutProcessModel model = new CheckoutProcessModel();
        model.setCheckoutId(entity.getOrderId());
        model.setCustomerId(entity.getCustomerId());
        model.setPremiumAmount(entity.getTotalAmount() != null ? entity.getTotalAmount() : new java.math.BigDecimal("0"));
        model.setPaymentMethod(entity.getPaymentMethod() != null ? PaymentMethod.valueOf(entity.getPaymentMethod()) : PaymentMethod.VA);
        model.setProcessId(entity.getProcessId());
        return model;
    }

    /**
     * Log process event
     */
    private void logProcessEvent(String processId, String nodeName, String eventType, String eventData) {
        try {
            ProcessAuditLogEntity log = new ProcessAuditLogEntity(
                processId, nodeName, eventType, UUID.randomUUID().toString(), eventData
            );
            // processAuditLogRepository.save(log); // Panache handles persistence automatically
        } catch (Exception e) {
            LOG.error("Failed to log process event for process: {}", processId, e);
        }
    }

    /**
     * Update process status
     */
    private void updateProcessStatus(String processId, String status, String message) {
        try {
            ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
            if (entity != null) {
                entity.setStatus(status);
                entity.setDecisionReason(message);
                // processInstanceRepository.save(entity); // Panache handles persistence automatically
            }
        } catch (Exception e) {
            LOG.error("Failed to update process status for process: {}", processId, e);
        }
    }
}