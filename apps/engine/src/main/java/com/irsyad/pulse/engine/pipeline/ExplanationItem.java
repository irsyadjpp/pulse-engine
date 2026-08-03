package com.irsyad.pulse.engine.pipeline;

import lombok.Getter;

@Getter
public class ExplanationItem {
    private String factor;
    private String impact;
    private String detail;

    public ExplanationItem() {}

    public ExplanationItem(String factor, String impact, String detail) {
        this.factor = factor;
        this.impact = impact;
        this.detail = detail;
    }
}