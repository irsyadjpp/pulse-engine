package com.irsyad.pulse.orchestrator.messaging.constants;

/**
 * Kafka topic definitions for Pulse Engine.
 * <p>
 * Topic naming convention: pulse.checkout.{capability}
 * DLQ naming convention:  pulse.checkout.{capability}.dlq
 * <p>
 * Topics are named after capabilities (Observe, Decide, Explain, Fail, Retry)
 * to reflect the product identity rather than implementation details.
 */
public final class KafkaTopics {

    private KafkaTopics() {
        // constants class
    }

    // ========================================================================
    // Core Topics (Capability-Named)
    // ========================================================================

    /** Checkout event completed by Orchestrator with DMN decision (Process + Decision capabilities). */
    public static final String CHECKOUT_COMPLETED = "pulse.checkout.completed.v1";

    /** Decision result produced by Pulse Engine (Decide capability). */
    public static final String CHECKOUT_DECIDED = "pulse.decision.completed.v1";

    /** Explanation produced by Pulse Engine (Explain capability). */
    public static final String CHECKOUT_EXPLAINED = "pulse.insight.generated.v1";

    /** Processing failure event for observability. */
    public static final String CHECKOUT_FAILED = "pulse.checkout.failed.v1";

    /** Retry queue for transient failures with exponential backoff. */
    public static final String CHECKOUT_RETRY = "pulse.checkout.retry.v1";

    // ========================================================================
    // Dead Letter Queues (per-topic DLQ)
    // ========================================================================

    /** DLQ for checkout.completed after exhausted retries. */
    public static final String CHECKOUT_COMPLETED_DLQ = "pulse.checkout.completed.v1.dlq";

    /** DLQ for checkout.decided after exhausted retries. */
    public static final String CHECKOUT_DECIDED_DLQ = "pulse.decision.completed.v1.dlq";

    /** DLQ for checkout.explained after exhausted retries. */
    public static final String CHECKOUT_EXPLAINED_DLQ = "pulse.insight.generated.v1.dlq";

    // ========================================================================
    // Consumer Group IDs
    // ========================================================================

    public static final String GROUP_ENGINE = "pulse-engine";
    public static final String GROUP_ANALYTICS = "pulse-analytics";
    public static final String GROUP_NOTIFICATION = "pulse-notification";
    public static final String GROUP_MONITORING = "pulse-monitoring";
}