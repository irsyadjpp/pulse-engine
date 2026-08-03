package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.CheckoutRequest;
import com.irsyad.pulse.orchestrator.domain.dto.IdentityVerification;
import com.irsyad.pulse.orchestrator.domain.dto.RiskAssessment;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kie.dmn.api.core.DMNContext;
import org.kie.dmn.api.core.DMNDecisionResult;
import org.kie.dmn.api.core.DMNResult;
import org.kie.kogito.decision.DecisionModel;
import org.kie.kogito.decision.DecisionModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Bridges the BPMN "Assess Risk" step to the {@code checkout-risk.dmn}
 * underwriting decision table ("AssessRisk" decision).
 *
 * <p>
 * BPMN only orchestrates the workflow (Validate Checkout -&gt; Verify Identity
 * -&gt; Assess Risk -&gt; route on decision). All underwriting rules - age
 * limits,
 * occupation class, eKYC confidence threshold, and sum insured limits - live
 * exclusively in the DMN decision table. Changing business rules (e.g. age
 * bounds, occupation classes, confidence threshold, or sum insured limit)
 * only requires updating {@code checkout-risk.dmn}; this class never needs
 * to change.
 * </p>
 */
@ApplicationScoped
public class AssessRiskDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(AssessRiskDelegate.class);

    private static final String DMN_NAMESPACE = "http://www.irsyad.pulse/checkout-risk";
    private static final String DMN_MODEL_NAME = "checkout-risk";
    private static final String DMN_DECISION_NAME = "AssessRisk";

    @Inject
    DecisionModels decisionModels;

    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        RiskAssessment risk = new RiskAssessment();

        try {
            IdentityVerification identity = model.getIdentity();
            CheckoutRequest request = model.getRequest();

            if (identity == null || request == null) {
                throw new IllegalStateException(
                        "Identity verification or checkout request is missing from process model");
            }

            BigDecimal existingActiveSumInsured = identity.getExistingActiveSumInsured() != null
                    ? identity.getExistingActiveSumInsured()
                    : BigDecimal.ZERO;
            BigDecimal requestedSumInsured = request.getSumInsured() != null
                    ? request.getSumInsured()
                    : BigDecimal.ZERO;

            Map<String, Object> inputs = new HashMap<>();
            inputs.put("age", identity.getAge());
            inputs.put("occupationClass", identity.getOccupationClass());
            inputs.put("confidenceScore", identity.getConfidenceScore());
            inputs.put("existingActiveSumInsured", existingActiveSumInsured);
            inputs.put("requestedSumInsured", requestedSumInsured);

            LOG.info(
                    "Evaluating AssessRisk decision: age={}, occupationClass={}, confidenceScore={}, existingActiveSumInsured={}, requestedSumInsured={}",
                    identity.getAge(), identity.getOccupationClass(), identity.getConfidenceScore(),
                    existingActiveSumInsured, requestedSumInsured);

            DecisionModel decisionModel = decisionModels.getDecisionModel(DMN_NAMESPACE, DMN_MODEL_NAME);
            DMNContext context = decisionModel.newContext(inputs);
            DMNResult dmnResult = decisionModel.evaluateAll(context);

            if (dmnResult.hasErrors()) {
                dmnResult.getMessages().forEach(msg -> LOG.error("DMN message: {}", msg));
                throw new IllegalStateException("DMN evaluation for AssessRisk failed: " + dmnResult.getMessages());
            }

            DMNDecisionResult decisionResult = dmnResult.getDecisionResultByName(DMN_DECISION_NAME);
            if (decisionResult == null || decisionResult.getResult() == null) {
                throw new IllegalStateException("DMN decision AssessRisk did not produce a result");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) decisionResult.getResult();

            risk.setDecision((String) result.get("decision"));
            risk.setReasonCode((String) result.get("reasonCode"));
            risk.setRiskLevel((String) result.get("riskLevel"));
            risk.setConfidenceScore(identity.getConfidenceScore());
            risk.setTotalActiveSumInsured(existingActiveSumInsured.add(requestedSumInsured));
            risk.setRequestedSumInsured(requestedSumInsured);
            risk.setEvaluatedAt(Instant.now());
            risk.setDmnVersion(DMN_MODEL_NAME);

            model.setRisk(risk);

            LOG.info("Risk assessment completed: decision={}, reasonCode={}, riskLevel={}",
                    risk.getDecision(), risk.getReasonCode(), risk.getRiskLevel());

        } catch (Exception e) {
            LOG.error("Failed to assess risk via DMN, defaulting to REJECT (fail-safe)", e);
            risk.setDecision("REJECT");
            risk.setReasonCode("ERR_RISK_EVALUATION_FAILED");
            risk.setRiskLevel("HIGH");
            risk.setEvaluatedAt(Instant.now());
            model.setRisk(risk);
        }

        return model;
    }
}
