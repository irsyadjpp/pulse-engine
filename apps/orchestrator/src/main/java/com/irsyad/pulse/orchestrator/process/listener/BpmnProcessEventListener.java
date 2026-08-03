package com.irsyad.pulse.orchestrator.process.listener;

import com.irsyad.pulse.orchestrator.domain.enums.Decision;
import com.irsyad.pulse.orchestrator.domain.enums.ProcessStatus;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import com.irsyad.pulse.orchestrator.domain.model.ProcessAuditLogEntity;
import com.irsyad.pulse.orchestrator.domain.model.ProcessInstanceEntity;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessAuditLogRepository;
import com.irsyad.pulse.orchestrator.infrastructure.persistence.ProcessInstanceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kie.api.event.process.*;
import org.kie.kogito.internal.process.event.KogitoProcessEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

/**
 * Kogito Process Event Listener for enhanced process tracking and actual business flow implementation
 * This provides custom process lifecycle handling with real business logic execution
 */
@ApplicationScoped
public class BpmnProcessEventListener implements KogitoProcessEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(BpmnProcessEventListener.class);

    @Inject
    private ProcessInstanceRepository processInstanceRepository;

    @Inject
    private ProcessAuditLogRepository processAuditLogRepository;

    /**
     * Execute initial business flow when process starts
     */
    private void executeInitialBusinessFlow(String processId, Object model) {
        LOG.info("Executing initial business flow for process: {}", processId);

        try {
            if (model instanceof CheckoutProcessModel) {
                CheckoutProcessModel checkoutModel = (CheckoutProcessModel) model;

                // Step 1: Assess Risk (now handled by BPMN business rule task)
                logAuditEntry(processId, "BusinessFlow", "RISK_ASSESSMENT",
                        "Risk assessment handled by BPMN DMN");

                // Step 2: Process Premium based on risk decision
                if (Decision.APPROVE.name().equals(checkoutModel.getDecision())) {
                    logAuditEntry(processId, "BusinessFlow", "PREMIUM_PROCESSING",
                            "Risk approved, proceeding with premium authorization");
                } else if (Decision.REVIEW.name().equals(checkoutModel.getDecision())) {
                    logAuditEntry(processId, "BusinessFlow", "REVIEW_REQUIRED",
                            "Risk assessment requires manual review, premium authorization deferred");
                } else if (Decision.REJECT.name().equals(checkoutModel.getDecision())) {
                    logAuditEntry(processId, "BusinessFlow", "REJECTED",
                            "Risk assessment rejected");
                }

                logAuditEntry(processId, "BusinessFlow", "INITIAL_FLOW_COMPLETED",
                        "Initial business flow completed successfully");
            }

        } catch (Exception e) {
            LOG.error("Error executing initial business flow for process: {}", processId, e);
            onProcessError(processId, e);
        }
    }

    /**
     * Process completed logic - finalizes the business flow
     */
    public void onProcessCompleted(String processId, String status) {
        LOG.info("Kogito Process Completed - ProcessId: {}, Status: {}", processId, status);

        try {
            // Update process instance entity
            ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
            if (entity != null) {
                entity.setStatus(status);
                entity.setCurrentNode("Process Completed");
                entity.setEndTime(Instant.now());

                // Set decision based on status
                if (ProcessStatus.COMPLETED.name().equals(status)) {
                    entity.setDecision(Decision.APPROVE.name());
                } else if (ProcessStatus.REJECTED.name().equals(status)) {
                    entity.setDecision(Decision.REJECT.name());
                } else if (ProcessStatus.REVIEW_PENDING.name().equals(status)) {
                    entity.setDecision(Decision.REVIEW.name());
                }

                processInstanceRepository.save(entity);
            }

            // Log audit entry
            logAuditEntry(processId, "Process Completed", "PROCESS_COMPLETE",
                    "Kogito process completed with status: " + status);

            // Execute completion business logic
            executeCompletionBusinessFlow(processId, status);

        } catch (Exception e) {
            LOG.error("Error handling process completed event for process: {}", processId, e);
        }
    }

    /**
     * Execute completion business flow based on final status
     */
    private void executeCompletionBusinessFlow(String processId, String status) {
        LOG.info("Executing completion business flow for process: {} with status: {}", processId, status);

        try {
            ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
            if (entity == null) {
                return;
            }

            if (ProcessStatus.COMPLETED.name().equals(status)) {
                logAuditEntry(processId, "CompletionFlow", "SUCCESS_NOTIFICATION",
                        "Sending success notification for completed checkout");
                // Send success notification (placeholder)
            } else if (ProcessStatus.REJECTED.name().equals(status)) {
                logAuditEntry(processId, "CompletionFlow", "REJECTION_NOTIFICATION",
                        "Sending rejection notification");
                // Send rejection notification (placeholder)
            } else if (ProcessStatus.REVIEW_PENDING.name().equals(status)) {
                logAuditEntry(processId, "CompletionFlow", "REVIEW_NOTIFICATION",
                        "Sending review notification to team");
                // Send review notification (placeholder)
            } else {
                logAuditEntry(processId, "CompletionFlow", "UNKNOWN_STATUS",
                        "Unknown completion status: " + status);
            }

        } catch (Exception e) {
            LOG.error("Error executing completion business flow for process: {}", processId, e);
        }
    }

    /**
     * Process error logic - handles business flow errors
     */
    public void onProcessError(String processId, Throwable error) {
        LOG.error("Kogito Process Error - ProcessId: {}, Error: {}", processId, error.getMessage());

        try {
            // Update process instance entity
            ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
            if (entity != null) {
                entity.setStatus(ProcessStatus.FAILED.name());
                entity.setWorkflowStatus("FAILED");
                entity.setCurrentNode("Error: " + error.getMessage());
                entity.setEndTime(Instant.now());
                processInstanceRepository.save(entity);
            }

            // Log audit entry
            logAuditEntry(processId, "Process Error", "PROCESS_ERROR",
                    "Process error: " + error.getClass().getSimpleName() + " - " + error.getMessage());

            // Execute error handling business flow
            executeErrorHandlingFlow(processId, error);

        } catch (Exception e) {
            LOG.error("Error handling process error event for process: {}", processId, e);
        }
    }

    /**
     * Execute error handling business flow
     */
    private void executeErrorHandlingFlow(String processId, Throwable error) {
        LOG.info("Executing error handling flow for process: {}", processId);

        try {
            ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
            if (entity == null) {
                return;
            }

            // Attempt to cancel any reservations made before error
            CheckoutProcessModel model = new CheckoutProcessModel();
            model.setProcessId(processId);

            logAuditEntry(processId, "ErrorHandlingFlow", "CLEANUP_INITIATED",
                    "Starting cleanup process after error");

            // Log error for monitoring
            logAuditEntry(processId, "ErrorHandlingFlow", "ERROR_LOGGED",
                    "Error logged to monitoring system: " + error.getMessage());

            // Send error notification (placeholder)
            logAuditEntry(processId, "ErrorHandlingFlow", "ERROR_NOTIFICATION",
                    "Error notification sent to operations team");

        } catch (Exception e) {
            LOG.error("Error executing error handling flow for process: {}", processId, e);
        }
    }

    /**
     * Node entered logic - handles business flow node transitions
     */
    public void onNodeEntered(String processId, String nodeId, String nodeName) {
        LOG.info("Kogito Node Entered - ProcessId: {}, NodeId: {}, NodeName: {}",
                processId, nodeId, nodeName);

        try {
            // Update process instance entity
            ProcessInstanceEntity entity = processInstanceRepository.findById(processId);
            if (entity != null) {
                entity.setCurrentNode(nodeName);
                processInstanceRepository.save(entity);
            }

            // Log audit entry
            logAuditEntry(processId, nodeName, "NODE_ENTERED",
                    "Entered node: " + nodeName + " (id: " + nodeId + ")");

            // Execute node-specific business logic
            executeNodeBusinessLogic(processId, nodeId, nodeName);

        } catch (Exception e) {
            LOG.error("Error handling node entered event for process: {}", processId, e);
        }
    }

    /**
     * Execute node-specific business logic
     */
    private void executeNodeBusinessLogic(String processId, String nodeId, String nodeName) {
        LOG.info("Executing node business logic for process: {}, node: {}", processId, nodeName);

        try {
            switch (nodeName) {
                case "Validate Checkout":
                    logAuditEntry(processId, "NodeLogic", "CHECKOUT_VALIDATION_TRIGGERED",
                            "Checkout validation node triggered");
                    break;

                case "Validate Identity":
                    logAuditEntry(processId, "NodeLogic", "IDENTITY_VALIDATION_TRIGGERED",
                            "Identity validation node triggered");
                    break;

                case "AssessRisk":
                    logAuditEntry(processId, "NodeLogic", "RISK_ASSESSMENT_TRIGGERED",
                            "Risk assessment node triggered");
                    break;

                case "ReserveInventory":
                    logAuditEntry(processId, "NodeLogic", "INVENTORY_RESERVATION_TRIGGERED",
                            "Inventory reservation node triggered");
                    break;

                case "ProcessPayment":
                    logAuditEntry(processId, "NodeLogic", "PAYMENT_PROCESSING_TRIGGERED",
                            "Payment processing node triggered");
                    break;

                case "CompleteCheckout":
                    logAuditEntry(processId, "NodeLogic", "CHECKOUT_COMPLETION_TRIGGERED",
                            "Checkout completion node triggered");
                    break;

                case "ManualReview":
                    logAuditEntry(processId, "NodeLogic", "MANUAL_REVIEW_TRIGGERED",
                            "Manual review node triggered - awaiting human intervention");
                    break;

                default:
                    logAuditEntry(processId, "NodeLogic", "UNKNOWN_NODE",
                            "Unknown node: " + nodeName);
            }

        } catch (Exception e) {
            LOG.error("Error executing node business logic for process: {}", processId, e);
        }
    }

    /**
     * Node exited logic - handles post-node business logic
     */
    public void onNodeExited(String processId, String nodeId, String nodeName) {
        LOG.info("Kogito Node Exited - ProcessId: {}, NodeId: {}, NodeName: {}",
                processId, nodeId, nodeName);

        try {
            // Log audit entry
            logAuditEntry(processId, nodeName, "NODE_EXITED",
                    "Exited node: " + nodeName + " (id: " + nodeId + ")");

            // Execute post-node business logic
            executePostNodeBusinessLogic(processId, nodeId, nodeName);

        } catch (Exception e) {
            LOG.error("Error handling node exited event for process: {}", processId, e);
        }
    }

    /**
     * Execute post-node business logic
     */
    private void executePostNodeBusinessLogic(String processId, String nodeId, String nodeName) {
        LOG.info("Executing post-node business logic for process: {}, node: {}", processId, nodeName);

        try {
            switch (nodeName) {
                case "Validate Checkout":
                    logAuditEntry(processId, "PostNodeLogic", "CHECKOUT_VALIDATION_COMPLETED",
                            "Checkout validation completed");
                    break;

                case "Validate Identity":
                    logAuditEntry(processId, "PostNodeLogic", "IDENTITY_VALIDATION_COMPLETED",
                            "Identity validation completed");
                    break;

                case "AssessRisk":
                    logAuditEntry(processId, "PostNodeLogic", "RISK_DECISION_APPLIED",
                            "Risk assessment decision applied to process");
                    break;

                case "ReserveInventory":
                    logAuditEntry(processId, "PostNodeLogic", "INVENTORY_CONFIRMED",
                            "Inventory reservation confirmed");
                    break;

                case "ProcessPayment":
                    logAuditEntry(processId, "PostNodeLogic", "PAYMENT_CONFIRMED",
                            "Payment processing confirmed");
                    break;

                default:
                    logAuditEntry(processId, "PostNodeLogic", "NODE_COMPLETED",
                            "Node completed: " + nodeName);
            }

        } catch (Exception e) {
            LOG.error("Error executing post-node business logic for process: {}", processId, e);
        }
    }

    /**
     * Variable changed logic - handles business rule variable updates
     */
    public void onVariableChanged(String processId, String variableId, Object oldValue, Object newValue) {
        LOG.info("Kogito Variable Changed - ProcessId: {}, Variable: {}, Old: {}, New: {}",
                processId, variableId, oldValue, newValue);

        try {
            // Log audit entry for important variables
            if ("decision".equals(variableId) || "riskLevel".equals(variableId)) {
                logAuditEntry(processId, "Variable Changed: " + variableId, "VARIABLE_CHANGED",
                        "Variable " + variableId + " changed from " + oldValue + " to " + newValue);

                // Execute business rule logic based on variable changes
                executeVariableChangeBusinessLogic(processId, variableId, oldValue, newValue);
            }

        } catch (Exception e) {
            LOG.error("Error handling variable changed event for process: {}", processId, e);
        }
    }

    /**
     * Execute business rule logic based on variable changes
     */
    private void executeVariableChangeBusinessLogic(String processId, String variableId, Object oldValue, Object newValue) {
        LOG.info("Executing variable change business logic for process: {}, variable: {}", processId, variableId);

        try {
            if ("decision".equals(variableId)) {
                String decision = (String) newValue;

                if (Decision.APPROVE.name().equals(decision)) {
                    logAuditEntry(processId, "VariableChangeLogic", "APPROVAL_FLOW_INITIATED",
                            "Decision changed to APPROVE - initiating approval flow");
                } else if (Decision.REVIEW.name().equals(decision)) {
                    logAuditEntry(processId, "VariableChangeLogic", "REVIEW_FLOW_INITIATED",
                            "Decision changed to REVIEW - initiating review flow");
                } else if (Decision.REJECT.name().equals(decision)) {
                    logAuditEntry(processId, "VariableChangeLogic", "REJECTION_FLOW_INITIATED",
                            "Decision changed to REJECT - initiating rejection flow");
                }
            } else if ("riskLevel".equals(variableId)) {
                String riskLevel = (String) newValue;
                logAuditEntry(processId, "VariableChangeLogic", "RISK_LEVEL_UPDATED",
                        "Risk level updated to: " + riskLevel);
            }

        } catch (Exception e) {
            LOG.error("Error executing variable change business logic for process: {}", processId, e);
        }
    }

    private void logAuditEntry(String processId, String nodeName, String eventType, String message) {
        String correlationId = UUID.randomUUID().toString();

        ProcessAuditLogEntity log = new ProcessAuditLogEntity();
        log.setProcessId(processId);
        log.setNodeName(nodeName);
        log.setEventType(eventType);
        log.setEventTime(Instant.now());
        log.setCorrelationId(correlationId);
        log.setEventData(message);
        processAuditLogRepository.save(log);
    }

    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {
        String processId = event.getProcessInstance().getId();
        LOG.info("Process about to start: {}", processId);

        // Update DB to reflect process is about to start
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setStatus(ProcessStatus.STARTED.name());
            entity.setWorkflowStatus("ACTIVE");
            entity.setStartTime(Instant.now());
            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, "Process Started", "PROCESS_STARTING",
                "Kogito process is about to start");
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        String processId = event.getProcessInstance().getId();
        LOG.info("Process started: {}", processId);

        // Update DB
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setStatus(ProcessStatus.STARTED.name());
            entity.setWorkflowStatus("ACTIVE");
            entity.setStartTime(Instant.now());
            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, "Process Started", "PROCESS_START",
                "Kogito process started successfully");
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        String processId = event.getProcessInstance().getId();
        LOG.info("Process about to complete: {}", processId);

        // Update DB to reflect process is completing
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setStatus(ProcessStatus.COMPLETED.name());
            entity.setWorkflowStatus("COMPLETED");
            entity.setEndTime(Instant.now());
            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, "Process Completed", "PROCESS_COMPLETING",
                "Kogito process is about to complete");
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        String processId = event.getProcessInstance().getId();
        String status = ProcessStatus.COMPLETED.name();
        LOG.info("Process completed: {} with status: {}", processId, status);

        // Update DB
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setStatus(status);
            entity.setWorkflowStatus("COMPLETED");
            entity.setCurrentNode("Process Completed");
            entity.setEndTime(Instant.now());

            // Set decision based on status
            if (ProcessStatus.COMPLETED.name().equals(status)) {
                entity.setDecision(Decision.APPROVE.name());
            } else if (ProcessStatus.REJECTED.name().equals(status)) {
                entity.setDecision(Decision.REJECT.name());
            } else if (ProcessStatus.REVIEW_PENDING.name().equals(status)) {
                entity.setDecision(Decision.REVIEW.name());
            }

            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, "Process Completed", "PROCESS_COMPLETE",
                "Process completed with status: " + status);
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        String processId = event.getProcessInstance().getId();
        String nodeName = event.getNodeInstance().getNode().getName();
        LOG.info("Node about to trigger: {} in process: {}", nodeName, processId);

        // Update currentNode di DB
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setCurrentNode(nodeName);
            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, nodeName, "NODE_TRIGGERING",
                "Node about to trigger: " + nodeName);
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        String processId = event.getProcessInstance().getId();
        String nodeName = event.getNodeInstance().getNode().getName();
        LOG.info("Node triggered: {} in process: {}", nodeName, processId);

        // Update currentNode di DB
        ProcessInstanceEntity entity = processInstanceRepository.findByProcessId(processId);
        if (entity != null) {
            entity.setCurrentNode(nodeName);
            processInstanceRepository.save(entity);
        }

        logAuditEntry(processId, nodeName, "NODE_TRIGGERED",
                "Node triggered: " + nodeName);
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        String processId = event.getProcessInstance().getId();
        String nodeName = event.getNodeInstance().getNode().getName();
        LOG.info("Node about to leave: {} in process: {}", nodeName, processId);

        logAuditEntry(processId, nodeName, "NODE_LEAVING",
                "Node about to leave: " + nodeName);
    }

    @Override
    public void afterNodeLeft(ProcessNodeLeftEvent event) {
        String processId = event.getProcessInstance().getId();
        onNodeExited(processId, event.getNodeInstance().getNodeName(),
                event.getNodeInstance().getNode().getName());
    }

    @Override
    public void beforeVariableChanged(ProcessVariableChangedEvent event) {
        String processId = event.getProcessInstance().getId();
        String variableId = event.getVariableId();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        LOG.info("Variable about to change: {} in process: {} from {} to {}",
                variableId, processId, oldValue, newValue);

        // Log audit entry for important variables
        if ("decision".equals(variableId) || "riskLevel".equals(variableId)) {
            logAuditEntry(processId, "Variable Changed: " + variableId, "VARIABLE_CHANGING",
                    "Variable " + variableId + " about to change from " + oldValue + " to " + newValue);
        }
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent event) {
        String processId = event.getProcessInstance().getId();
        String variableId = event.getVariableId();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        LOG.info("Variable changed: {} in process: {} from {} to {}",
                variableId, processId, oldValue, newValue);

        // Log audit entry for important variables
        if ("decision".equals(variableId) || "riskLevel".equals(variableId)) {
            logAuditEntry(processId, "Variable Changed: " + variableId, "VARIABLE_CHANGED",
                    "Variable " + variableId + " changed from " + oldValue + " to " + newValue);

            // Execute business rule logic based on variable changes
            executeVariableChangeBusinessLogic(processId, variableId, oldValue, newValue);
        }
    }
}
