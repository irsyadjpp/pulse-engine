package com.irsyad.pulse.engine.pipeline;

import com.irsyad.pulse.engine.event.CheckoutCompletedEvent;
import com.irsyad.pulse.engine.kafka.InsightGeneratedProducer;
import com.irsyad.pulse.engine.persistence.entity.CheckoutExplanationEntity;
import com.irsyad.pulse.engine.persistence.entity.CheckoutInsightEntity;
import com.irsyad.pulse.engine.persistence.entity.CheckoutTimelineEntity;
import com.irsyad.pulse.engine.persistence.entity.CustomerLearningEntity;
import com.irsyad.pulse.engine.persistence.repository.CheckoutExplanationRepository;
import com.irsyad.pulse.engine.persistence.repository.CheckoutInsightRepository;
import com.irsyad.pulse.engine.persistence.repository.CheckoutTimelineRepository;
import com.irsyad.pulse.engine.persistence.repository.CustomerLearningRepository;
import com.irsyad.pulse.engine.service.DecisionService;
import com.irsyad.pulse.engine.service.ExplanationService;
import com.irsyad.pulse.engine.service.UnderstandingService;
import com.irsyad.pulse.engine.model.event.EventHeader;
import com.irsyad.pulse.engine.model.event.InsightGeneratedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PulseEnginePipeline {

    private static final Logger LOG = Logger.getLogger(PulseEnginePipeline.class);

    @Inject
    CheckoutInsightRepository checkoutInsightRepository;

    @Inject
    CheckoutTimelineRepository checkoutTimelineRepository;

    @Inject
    CheckoutExplanationRepository checkoutExplanationRepository;

    @Inject
    CustomerLearningRepository customerLearningRepository;

    @Inject
    UnderstandingService understandingService;

    @Inject
    ExplanationService explanationService;

    @Inject
    DecisionService decisionService;

    @Inject
    InsightGeneratedProducer insightGeneratedProducer;

    public void execute(CheckoutCompletedEvent event) {
        LOG.info("Executing Pulse Engine pipeline for: " + event.getBusinessKey());

        // 0. Idempotency check - prevent duplicate processing
        if (isAlreadyProcessed(event.getEventId())) {
            LOG.warn("Duplicate event detected, skipping processing: eventId=" + event.getEventId());
            return;
        }

        // 1. Validate mandatory fields
        validateEvent(event);

        // 2. Observe - validate, generate correlation/trace id, normalize
        ObservationContext observationContext = observe(event);

        // 2. Understand - enrichment, classification
        UnderstandingContext understandingContext = understand(event, observationContext);

        // 3. Explain - generate explanation
        ExplanationContext explanationContext = explain(event, understandingContext);

        // 4. Learn - update customer learning
        learn(event, understandingContext);

        // 5. Persist - save to database
        persist(event, understandingContext, explanationContext);

        // 6. Publish - publish insight.generated
        publish(event, understandingContext, explanationContext);

        LOG.info("Pipeline completed for: " + event.getBusinessKey());
    }

    private boolean isAlreadyProcessed(String eventId) {
        if (eventId == null || eventId.isEmpty()) {
            LOG.warn("EventId is null or empty, skipping idempotency check");
            return false;
        }
        return checkoutInsightRepository.existsByEventId(eventId);
    }

    private void validateEvent(CheckoutCompletedEvent event) {
        if (event.getEventId() == null || event.getEventId().isEmpty()) {
            throw new IllegalArgumentException("eventId is required");
        }
        if (event.getProcessId() == null || event.getProcessId().isEmpty()) {
            throw new IllegalArgumentException("processId is required");
        }
        if (event.getBusinessKey() == null || event.getBusinessKey().isEmpty()) {
            throw new IllegalArgumentException("orderId is required");
        }
        if (event.getCustomerId() == null || event.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("customerId is required");
        }
        if (event.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }
        if (event.getDecision() == null || event.getDecision().isEmpty()) {
            throw new IllegalArgumentException("decision is required");
        }
    }

    private ObservationContext observe(CheckoutCompletedEvent event) {
        LOG.debug("Observe: " + event.getBusinessKey());
        ObservationContext ctx = new ObservationContext(event);
        return ctx;
    }

    private UnderstandingContext understand(CheckoutCompletedEvent event, ObservationContext observationContext) {
        LOG.debug("Understand: " + event.getBusinessKey());
        return understandingService.understand(event);
    }

    private ExplanationContext explain(CheckoutCompletedEvent event, UnderstandingContext understandingContext) {
        LOG.debug("Explain: " + event.getBusinessKey());
        String decision = decisionService.decide(understandingContext);
        String confidence = decisionService.getConfidence(understandingContext);
        String reason = buildDRGExplanation(understandingContext);

        return new ExplanationContext(
                decision,
                confidence,
                reason,
                List.of(
                        new ExplanationItem("customer_segment", "POSITIVE",
                                "Customer is in " + understandingContext.getSegment() + " segment"),
                        new ExplanationItem("identity_risk", understandingContext.getIdentityRisk(),
                                "Identity risk assessment: " + understandingContext.getIdentityRisk()),
                        new ExplanationItem("transaction_risk", understandingContext.getTransactionRisk(),
                                "Transaction risk assessment: " + understandingContext.getTransactionRisk()),
                        new ExplanationItem("overall_risk", understandingContext.getOverallRisk(),
                                "Overall risk assessment: " + understandingContext.getOverallRisk()),
                        new ExplanationItem("payment_history", "POSITIVE",
                                "No failed payments in last 12 months")));
    }

    private String buildDRGExplanation(UnderstandingContext context) {
        StringBuilder explanation = new StringBuilder();
        
        explanation.append("DRG-based risk assessment: ");
        explanation.append("Identity=").append(context.getIdentityRisk());
        explanation.append(", Transaction=").append(context.getTransactionRisk());
        explanation.append(", Overall=").append(context.getOverallRisk());
        
        if (context.getFraudScore() > 0) {
            explanation.append(", FraudScore=").append(context.getFraudScore());
        }
        
        if (context.getVelocityRisk() > 0) {
            explanation.append(", VelocityScore=").append(context.getVelocityRisk());
        }
        
        String decision = decisionService.decide(context);
        explanation.append(" → ").append(decision);
        
        return explanation.toString();
    }

    private void learn(CheckoutCompletedEvent event, UnderstandingContext understandingContext) {
        LOG.debug("Learn: " + event.getBusinessKey());
        // Update customer learning
        CustomerLearningEntity learning = customerLearningRepository.findById(event.getCustomerId());
        if (learning == null) {
            learning = CustomerLearningEntity.builder()
                    .customerId(event.getCustomerId())
                    .customerSegment(understandingContext.getSegment())
                    .purchaseCount(0)
                    .successfulCheckout(0)
                    .rejectedCheckout(0)
                    .learningVersion(1)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
        }
        learning.setPurchaseCount(learning.getPurchaseCount() + 1);
        learning.setSuccessfulCheckout(learning.getSuccessfulCheckout() + 1);
        learning.setLastCheckoutTime(Instant.now());
        learning.setUpdatedAt(Instant.now());
        customerLearningRepository.persist(learning);
    }

    private void persist(CheckoutCompletedEvent event, UnderstandingContext understandingContext,
            ExplanationContext explanationContext) {
        LOG.debug("Persist: " + event.getBusinessKey());
        Instant now = Instant.now();

        // Save insight
        CheckoutInsightEntity insight = CheckoutInsightEntity.builder()
                .checkoutId(event.getBusinessKey())
                .processId(event.getProcessId())
                .eventId(event.getEventId())
                .customerId(event.getCustomerId())
                .orderId(event.getBusinessKey())
                .decision(explanationContext.getDecision())
                .confidence(explanationContext.getConfidence())
                .riskLevel(decisionService.getRiskLevel(understandingContext))
                .totalAmount(event.getAmount())
                .insightType(determineInsightType(understandingContext, event))
                .processedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        try {
            checkoutInsightRepository.persist(insight);
        } catch (Exception e) {
            // Handle duplicate key exception from UNIQUE constraint on event_id
            if (e.getMessage() != null && e.getMessage().contains("duplicate key") || 
                e.getMessage().contains("violates unique constraint")) {
                LOG.warn("Duplicate event detected during persist, eventId=" + event.getEventId() + 
                        ", checkoutId=" + event.getBusinessKey() + ", skipping persistence");
                // Update existing record instead
                CheckoutInsightEntity existing = checkoutInsightRepository.findByCheckoutId(event.getBusinessKey());
                if (existing != null) {
                    existing.setUpdatedAt(now);
                    existing.setDecision(explanationContext.getDecision());
                    existing.setConfidence(explanationContext.getConfidence());
                    existing.setRiskLevel(decisionService.getRiskLevel(understandingContext));
                    checkoutInsightRepository.persist(existing);
                }
            } else {
                throw e;
            }
        }

        // Save timeline events
        saveTimeline(event.getBusinessKey(), "OBSERVED", "SUCCESS", now);
        saveTimeline(event.getBusinessKey(), "UNDERSTOOD", "SUCCESS", now);
        saveTimeline(event.getBusinessKey(), "EXPLAINED", "SUCCESS", now);
        saveTimeline(event.getBusinessKey(), "LEARNED", "SUCCESS", now);
        saveTimeline(event.getBusinessKey(), "PERSISTED", "SUCCESS", now);

        // Save explanation
        CheckoutExplanationEntity explanation = CheckoutExplanationEntity.builder()
                .checkoutId(event.getBusinessKey())
                .explanationType("DECISION")
                .explanation(explanationContext.getReason())
                .createdAt(now)
                .build();
        checkoutExplanationRepository.persist(explanation);
    }

    private String determineInsightType(UnderstandingContext context, CheckoutCompletedEvent event) {
        if (context.getOverallRisk().equals("HIGH")) {
            return "HIGH_FRAUD_RISK";
        }
        if (event.getAmount().compareTo(BigDecimal.valueOf(100_000_000)) > 0) {
            return "HIGH_AMOUNT";
        }
        if (context.getVelocityRisk() > 70) {
            return "VELOCITY_ANOMALY";
        }
        if (context.getFraudScore() > 50) {
            return "PAYMENT_METHOD_RISK";
        }
        if (context.isFirstPurchase()) {
            return "CUSTOMER_FIRST_PURCHASE";
        }
        return "CUSTOMER_BEHAVIOR";
    }

    private void saveTimeline(String checkoutId, String capability, String status, Instant time) {
        CheckoutTimelineEntity timeline = CheckoutTimelineEntity.builder()
                .checkoutId(checkoutId)
                .capability(capability)
                .status(status)
                .eventTime(time)
                .createdAt(time)
                .build();
        checkoutTimelineRepository.persist(timeline);
    }

    private void publish(CheckoutCompletedEvent event, UnderstandingContext understandingContext,
            ExplanationContext explanationContext) {
        LOG.debug("Publish: " + event.getBusinessKey());
        
        InsightGeneratedEvent insightEvent = new InsightGeneratedEvent(
                EventHeader.builder()
                        .eventId(UUID.randomUUID())
                        .eventType("insight.generated")
                        .producer("pulse-engine")
                        .createdAt(Instant.now())
                        .version(1)
                        .build(),
                event.getBusinessKey(),
                determineInsightType(understandingContext, event),
                explanationContext.getDecision(),
                determineSeverity(understandingContext),
                "pulse-engine",
                Instant.now()
        );
        
        insightGeneratedProducer.publish(insightEvent);
        LOG.info("Insight generated and published: " + insightEvent.getOrderId());
    }

    private String determineSeverity(UnderstandingContext context) {
        if (context.getOverallRisk().equals("HIGH")) {
            return "HIGH";
        }
        if (context.getOverallRisk().equals("MEDIUM")) {
            return "MEDIUM";
        }
        return "LOW";
    }
}