package com.irsyad.pulse.engine.pipeline;

import lombok.Getter;

import java.util.List;

@Getter
public class ExplanationContext {
    private String decision;
    private String confidence;
    private String reason;
    private List<ExplanationItem> factors;

    public ExplanationContext() {}

    public ExplanationContext(String decision, String confidence, String reason, List<ExplanationItem> factors) {
        this.decision = decision;
        this.confidence = confidence;
        this.reason = reason;
        this.factors = factors;
    }
}