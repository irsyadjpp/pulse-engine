package com.irsyad.pulse.orchestrator.infrastructure.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MetricsConfig {

    @Inject
    private MeterRegistry meterRegistry;

    public Counter processSuccessCounter() {
        return Counter.builder("process.success.total")
                .description("Total number of successful process executions")
                .register(meterRegistry);
    }

    public Counter processFailureCounter() {
        return Counter.builder("process.failure.total")
                .description("Total number of failed process executions")
                .register(meterRegistry);
    }

    public Counter processRetryCounter() {
        return Counter.builder("process.retry.total")
                .description("Total number of process retries")
                .register(meterRegistry);
    }

    public Timer processProcessingTimer() {
        return Timer.builder("process.processing.time")
                .description("Process execution time")
                .register(meterRegistry);
    }

    public Counter kafkaProducerSuccessCounter() {
        return Counter.builder("kafka.producer.success.total")
                .description("Total number of successful Kafka producer sends")
                .register(meterRegistry);
    }

    public Counter kafkaProducerFailureCounter() {
        return Counter.builder("kafka.producer.failure.total")
                .description("Total number of failed Kafka producer sends")
                .register(meterRegistry);
    }

    public Counter circuitBreakerOpenCounter() {
        return Counter.builder("circuit.breaker.open.total")
                .description("Total number of circuit breaker openings")
                .register(meterRegistry);
    }

    public Counter dlqCounter() {
        return Counter.builder("dlq.messages.total")
                .description("Total number of messages sent to DLQ")
                .register(meterRegistry);
    }
}