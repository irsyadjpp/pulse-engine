package com.irsyad.pulse.orchestrator.process.handler;

import com.irsyad.pulse.orchestrator.domain.dto.RiskAssessment;
import com.irsyad.pulse.orchestrator.domain.model.CheckoutProcessModel;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AssessRiskDelegate {
    
    private static final Logger LOG = LoggerFactory.getLogger(AssessRiskDelegate.class);
    
    public CheckoutProcessModel execute(CheckoutProcessModel model) {
        try {
            LOG.info("Assessing risk for checkout");
            
            // TODO: Implement actual DMN evaluation using Kogito
            // For now, returning a default risk assessment
            // The actual DMN integration will be handled by Kogito's code generation
            
            RiskAssessment risk = new RiskAssessment();
            risk.setDecision("REVIEW");
            risk.setReasonCode("MEDIUM_RISK");
            risk.setRiskLevel("MEDIUM");
            
            model.setRisk(risk);
            
            LOG.info("Risk assessment completed: decision=REVIEW, reason=MEDIUM_RISK, riskLevel=MEDIUM");
            
        } catch (Exception e) {
            LOG.error("Failed to assess risk", e);
            RiskAssessment risk = new RiskAssessment();
            risk.setDecision("REJECT");
            risk.setReasonCode("ERROR");
            risk.setRiskLevel("HIGH");
            model.setRisk(risk);
        }
        
        return model;
    }
}