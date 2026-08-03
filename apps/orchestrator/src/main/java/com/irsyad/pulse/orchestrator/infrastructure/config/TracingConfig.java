package com.irsyad.pulse.orchestrator.infrastructure.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.TextMapPropagator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import java.util.UUID;

@ApplicationScoped
public class TracingConfig {

    @Produces
    @Singleton
    public Tracer tracer() {
        return OpenTelemetry.noop().getTracer("pulse-orchestrator");
    }

    @Produces
    @Singleton
    public TextMapPropagator textMapPropagator() {
        return TextMapPropagator.composite();
    }

    @Produces
    @Singleton
    public TraceContextProvider traceContextProvider() {
        return new TraceContextProvider();
    }

    public static class TraceContextProvider {
        public String generateTraceId() {
            return UUID.randomUUID().toString();
        }

        public String generateSpanId() {
            return UUID.randomUUID().toString().substring(0, 16);
        }

        public String getParentSpanId() {
            return UUID.randomUUID().toString().substring(0, 16);
        }
    }
}