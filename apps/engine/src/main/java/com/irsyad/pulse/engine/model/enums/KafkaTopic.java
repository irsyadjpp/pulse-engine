package com.irsyad.pulse.engine.model.enums;

public enum KafkaTopic {
    CHECKOUT_REQUESTED("pulse.checkout.requested"),
    DECISION_COMPLETED("pulse.decision.completed"),
    INSIGHT_GENERATED("pulse.insight.generated"),
    PROCESSING_FAILED("pulse.processing.failed"),
    RETRY_PROCESSING("pulse.retry.processing"),
    CHECKOUT_REQUESTED_DLQ("pulse.checkout.requested.dlq"),
    DECISION_COMPLETED_DLQ("pulse.decision.completed.dlq"),
    INSIGHT_GENERATED_DLQ("pulse.insight.generated.dlq");

    private final String value;

    KafkaTopic(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}