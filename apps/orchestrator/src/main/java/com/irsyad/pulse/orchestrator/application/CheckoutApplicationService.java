package com.irsyad.pulse.orchestrator.application;

import com.irsyad.pulse.orchestrator.api.dto.request.CancelRequest;
import com.irsyad.pulse.orchestrator.api.dto.request.CheckoutApiRequest;
import com.irsyad.pulse.orchestrator.api.dto.request.RetryRequest;
import com.irsyad.pulse.orchestrator.api.dto.response.*;
import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.enums.Decision;
import com.irsyad.pulse.orchestrator.domain.enums.PaymentMethod;
import com.irsyad.pulse.orchestrator.domain.enums.ProcessStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.domain.model.ProcessAuditLogEntity;
import com.irsyad.pulse.orchestrator.domain.model.ProcessInstanceEntity;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessAuditLogRepository;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessInstanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class CheckoutApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutApplicationService.class);

    @Inject
    @Named("checkout-process")
    private Process<?> checkoutProcess;

    @Inject
    private ProcessInstanceRepository processInstanceRepository;

    @Inject
    private ProcessAuditLogRepository processAuditLogRepository;

    // 1. Start Checkout Process using BPMN
    @Transactional
    public CheckoutStartResponse startCheckout(CheckoutApiRequest request) {
        LOG.info("Starting checkout BPMN process for customerId: {}", request.getCustomerId());

        // Generate checkout ID from customer ID + timestamp
        String checkoutId = "CHK-" + System.currentTimeMillis();

        // Calculate premium amount (simplified - delegates will handle the rest)
        BigDecimal premiumAmount = BigDecimal.ZERO;

        // Create CheckoutRequest DTO for BPMN (simple mapping only)
        CheckoutRequest checkoutRequest = createCheckoutRequest(request);

        // Create CheckoutProcessModel and start Kogito BPMN process
        CheckoutProcessModel processModel = new CheckoutProcessModel();
        processModel.setCheckoutId(checkoutId);
        processModel.setCustomerId(request.getCustomerId());
        processModel.setPremiumAmount(premiumAmount);
        processModel.setCurrency("IDR");
        processModel.setPaymentMethod(request.getPayment() != null ? PaymentMethod.valueOf(request.getPayment().getMethod().name()) : PaymentMethod.VA);
        processModel.setBusinessKey(checkoutId);
        processModel.setRequest(checkoutRequest);

        // Create process instance using Kogito API
        Object kogitoModel = createKogitoModel(processModel);
        ProcessInstance<?> processInstance = checkoutProcess.createInstance((org.kie.kogito.Model) kogitoModel);
        String processId = processInstance.id();

        // Step 8: Create process instance entity for tracking
        // NOTE: Save entity BEFORE process.start() so event listeners can find it
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setProcessId(processId);
        entity.setBusinessKey(checkoutId);
        entity.setOrderId(checkoutId);
        entity.setCustomerId(request.getCustomerId());
        entity.setPaymentMethod(request.getPayment() != null ? request.getPayment().getMethod().name() : "VA");
        entity.setTotalAmount(premiumAmount);
        entity.setCurrency("IDR");
        entity.setStatus(ProcessStatus.STARTED.name());
        entity.setWorkflowStatus("ACTIVE");
        entity.setCurrentNode("VALIDATE_CHECKOUT");
        entity.setCurrentStep("VALIDATION");
        entity.setRetryCount(0);
        entity.setCorrelationId(processId);
        entity.setIdempotencyKey(checkoutId);
        entity.setStartTime(Instant.now());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        processInstanceRepository.save(entity);

        // Start the process AFTER entity is persisted so event listeners can update it
        processInstance.start();

        LOG.info("Started checkout process: {} for checkoutId: {}", processId, checkoutId);

        return new CheckoutStartResponse(
                processId,
                checkoutId,
                "PROCESSING",
                "Checkout process has been started.");
    }

    private CheckoutRequest createCheckoutRequest(CheckoutApiRequest request) {
        CheckoutRequest checkoutRequest = new CheckoutRequest();

        // Generate checkout ID
        String checkoutId = "CHK-" + System.currentTimeMillis();

        // Simple mapping from API request to domain request
        checkoutRequest.setRequestId(checkoutId);
        checkoutRequest.setTraceId(checkoutId);
        checkoutRequest.setOrderId(checkoutId);
        checkoutRequest.setCustomerId(request.getCustomerId());
        checkoutRequest.setNik(request.getNik());
        checkoutRequest.setFullName(request.getFullName());
        checkoutRequest.setDateOfBirth(request.getDateOfBirth());
        checkoutRequest.setOccupation(request.getOccupation());
        checkoutRequest.setMerchantId("DEFAULT_MERCHANT");
        checkoutRequest.setAmount(request.getSumInsured() != null ? request.getSumInsured() : BigDecimal.ZERO);
        checkoutRequest.setSumInsured(request.getSumInsured() != null ? request.getSumInsured() : BigDecimal.ZERO);
        checkoutRequest.setCurrency("IDR");
        checkoutRequest.setPaymentMethod(
                request.getPayment() != null ? request.getPayment().getMethod() : PaymentMethod.VA);
        checkoutRequest.setProductId(request.getItems() != null && !request.getItems().isEmpty() ? request.getItems().get(0).getProductId() : null);

        return checkoutRequest;
    }

    // 2. Get Checkout Process
    public CheckoutProcessResponse getCheckoutProcess(String processId) {
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity == null) {
            throw new RuntimeException("Process not found: " + processId);
        }

        return CheckoutProcessResponse.builder()
                .processId(entity.getProcessId())
                .businessKey(entity.getBusinessKey())
                .status(entity.getStatus())
                .currentNode(entity.getCurrentNode())
                .decision(entity.getDecision())
                .startedAt(entity.getStartTime())
                .lastUpdated(entity.getEndTime() != null ? entity.getEndTime() : entity.getStartTime())
                .build();
    }

    // 3. Get Process Timeline
    public ProcessTimelineResponse getProcessTimeline(String processId) {
        List<ProcessAuditLogEntity> auditLogs = processAuditLogRepository.findByProcessIdOrderByEventTimeAsc(processId);

        List<ProcessTimelineResponse.TimelineEvent> events = auditLogs.stream()
                .map(log -> ProcessTimelineResponse.TimelineEvent.builder()
                        .node(log.getNodeName())
                        .status(log.getEventType())
                        .time(log.getEventTime())
                        .build())
                .toList();

        return ProcessTimelineResponse.builder()
                .processId(processId)
                .events(events)
                .build();
    }

    // 4. Get Decision
    public DecisionResponse getDecision(String processId) {
        ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
        if (entity == null) {
            throw new RuntimeException("Process not found: " + processId);
        }

        return DecisionResponse.builder()
                .decision(entity.getDecision())
                .riskLevel(entity.getDecisionReason())
                .reason(generateDecisionReason(entity))
                .dmnModel("checkout-risk.dmn")
                .executedAt(entity.getStartTime()) // Approximate execution time
                .build();
    }

    private String generateDecisionReason(ProcessInstanceEntity entity) {
        String decision = entity.getDecision();
        String riskLevel = entity.getDecisionReason();

        if (Decision.APPROVE.name().equals(decision)) {
            return "Transaction approved with " + riskLevel + " risk level.";
        } else if (Decision.REVIEW.name().equals(decision)) {
            return "Transaction requires manual review due to " + riskLevel + " risk factors.";
        } else if (Decision.REJECT.name().equals(decision)) {
            return "Transaction rejected due to " + riskLevel + " risk factors.";
        } else {
            return "Decision pending or unknown.";
        }
    }

    // 5. Retry Process
    public RetryResponse retryProcess(String processId, RetryRequest request) {
        ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
        if (entity == null) {
            throw new RuntimeException("Process not found: " + processId);
        }

        if (!ProcessStatus.FAILED.name().equals(entity.getStatus())) {
            throw new RuntimeException("Only failed processes can be retried. Current status: " + entity.getStatus());
        }

        // Reset process state for retry
        entity.setStatus(ProcessStatus.RETRYING.name());
        entity.setStartTime(Instant.now());
        entity.setEndTime(null);
        processInstanceRepository.save(entity);

        // Log retry attempt
        ProcessAuditLogEntity log = new ProcessAuditLogEntity();
        log.setProcessId(processId);
        log.setNodeName("Retry Attempt");
        log.setEventType("RETRY");
        log.setEventTime(Instant.now());
        log.setEventData("Process retry requested with reason: " + request.getReason());
        processAuditLogRepository.save(log);

        // Restart the process (simplified - in real implementation would use Kogito
        // retry mechanism)
        try {
            // For retry, we'd need to reconstruct the original CheckoutRequest
            // This is simplified for demo purposes
            throw new UnsupportedOperationException("Retry functionality requires original request reconstruction");
        } catch (Exception e) {
            entity.setStatus(ProcessStatus.FAILED.name());
            entity.setEndTime(Instant.now());
            processInstanceRepository.save(entity);

            return RetryResponse.builder()
                    .status(ProcessStatus.FAILED.name())
                    .build();
        }
    }

    // 6. Cancel Process
    public CancelResponse cancelProcess(String processId, CancelRequest request) {
        ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
        if (entity == null) {
            throw new RuntimeException("Process not found: " + processId);
        }

        if (ProcessStatus.COMPLETED.name().equals(entity.getStatus()) || ProcessStatus.CANCELLED.name().equals(entity.getStatus())) {
            throw new RuntimeException("Cannot cancel a process that is already " + entity.getStatus());
        }

        // Update process status
        entity.setStatus(ProcessStatus.CANCELLED.name());
        entity.setEndTime(Instant.now());
        entity.setCurrentNode("Cancelled");
        processInstanceRepository.save(entity);

        // Log cancellation
        ProcessAuditLogEntity log = new ProcessAuditLogEntity();
        log.setProcessId(processId);
        log.setNodeName("Cancellation");
        log.setEventType("CANCEL");
        log.setEventTime(Instant.now());
        log.setEventData("Process cancelled with reason: " + request.getReason());
        processAuditLogRepository.save(log);

        return CancelResponse.builder()
                .status(ProcessStatus.CANCELLED.name())
                .build();
    }

    // 7. List Processes
    public ProcessListResponse listProcesses(String status, String customerId, String decision) {
        List<ProcessInstanceEntity> entities = processInstanceRepository.findAll();

        // Apply filters
        if (status != null && !status.isEmpty()) {
            entities = entities.stream()
                    .filter(e -> status.equals(e.getStatus()))
                    .toList();
        }

        if (customerId != null && !customerId.isEmpty()) {
            entities = entities.stream()
                    .filter(e -> customerId.equals(e.getCustomerId()))
                    .toList();
        }

        if (decision != null && !decision.isEmpty()) {
            entities = entities.stream()
                    .filter(e -> decision.equals(e.getDecision()))
                    .toList();
        }

        List<ProcessListResponse.ProcessItem> items = entities.stream()
                .map(entity -> ProcessListResponse.ProcessItem.builder()
                        .processId(entity.getProcessId())
                        .status(entity.getStatus())
                        .currentNode(entity.getCurrentNode())
                        .build())
                .toList();

        return ProcessListResponse.builder()
                .total(items.size())
                .items(items)
                .build();
    }

    // 8. Dashboard Statistics
    public DashboardResponse getDashboard() {
        List<ProcessInstanceEntity> allProcesses = processInstanceRepository.findAll();

        long running = allProcesses.stream()
                .filter(e -> ProcessStatus.STARTED.name().equals(e.getStatus()) || ProcessStatus.RETRYING.name().equals(e.getStatus())).count();
        long completed = allProcesses.stream().filter(e -> ProcessStatus.COMPLETED.name().equals(e.getStatus())).count();
        long failed = allProcesses.stream().filter(e -> ProcessStatus.FAILED.name().equals(e.getStatus())).count();
        long cancelled = allProcesses.stream().filter(e -> ProcessStatus.CANCELLED.name().equals(e.getStatus())).count();

        // Calculate average processing time (for completed processes)
        long avgProcessingTime = 0;
        List<ProcessInstanceEntity> completedProcesses = allProcesses.stream()
                .filter(e -> ProcessStatus.COMPLETED.name().equals(e.getStatus()) && e.getEndTime() != null)
                .toList();

        if (!completedProcesses.isEmpty()) {
            long totalTime = completedProcesses.stream()
                    .mapToLong(e -> ChronoUnit.MILLIS.between(e.getStartTime(), e.getEndTime()))
                    .sum();
            avgProcessingTime = totalTime / completedProcesses.size();
        }

        return DashboardResponse.builder()
                .running(running)
                .completed(completed)
                .failed(failed)
                .cancelled(cancelled)
                .averageProcessingTimeMs(avgProcessingTime)
                .build();
    }

    // Helper method to create Kogito model via reflection
    private Object createKogitoModel(com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel processModel) {
        try {
            Class<?> modelClass = Class.forName("org.drools.bpmn2.Checkout_processModel");
            Object model = modelClass.getDeclaredConstructor().newInstance();
            modelClass.getMethod("setCheckoutProcessModel", Object.class).invoke(model, processModel);
            return model;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Kogito model", e);
        }
    }

    // 9. Get Process Status
    public ProcessStatusResponse getProcessStatus(String processId) {
        ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
        if (entity == null) {
            throw new RuntimeException("Process not found: " + processId);
        }

        return ProcessStatusResponse.builder()
                .processId(entity.getProcessId())
                .processName("CheckoutProcess")
                .status(entity.getStatus())
                .nodeNames(entity.getCurrentNode())
                .startTime(entity.getStartTime())
                .build();
    }
}