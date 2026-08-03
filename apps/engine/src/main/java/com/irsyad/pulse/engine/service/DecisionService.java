package com.irsyad.pulse.engine.service;

import com.irsyad.pulse.engine.pipeline.UnderstandingContext;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DecisionService {

    public String decide(UnderstandingContext context) {
        // Use DRG-based decision logic instead of simple VIP/amount check
        String overallRisk = context.getOverallRisk();
        
        if (overallRisk == null || overallRisk.isEmpty()) {
            // Fallback to original logic if DRG data not available
            if (!context.isVIP() || context.isHighAmount()) {
                return "REVIEW";
            }
            return "APPROVE";
        }
        
        // DRG-based decision
        switch (overallRisk) {
            case "LOW":
                return "APPROVE";
            case "MEDIUM":
                return "REVIEW";
            case "HIGH":
                return "REJECT";
            default:
                return "REVIEW";
        }
    }

    public String getConfidence(UnderstandingContext context) {
        String overallRisk = context.getOverallRisk();
        
        if (overallRisk == null || overallRisk.isEmpty()) {
            // Fallback to original logic
            if (!context.isVIP() || context.isHighAmount()) {
                return "MEDIUM";
            }
            return "HIGH";
        }
        
        // DRG-based confidence
        switch (overallRisk) {
            case "LOW":
                return "HIGH";
            case "MEDIUM":
                return "MEDIUM";
            case "HIGH":
                return "LOW";
            default:
                return "MEDIUM";
        }
    }

    public String getRiskLevel(UnderstandingContext context) {
        String overallRisk = context.getOverallRisk();
        
        if (overallRisk == null || overallRisk.isEmpty()) {
            // Fallback to original logic
            if (!context.isVIP() || context.isHighAmount()) {
                return "MEDIUM";
            }
            return "LOW";
        }
        
        // DRG-based risk level
        return overallRisk;
    }
}