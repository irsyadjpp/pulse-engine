package com.irsyad.pulse.engine.util;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class TraceIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String CORRELATION_ID = "correlation-id";
    public static final String TRACE_ID = "trace-id";

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String correlationId = ctx.getHeaderString(CORRELATION_ID);
        String traceId = ctx.getHeaderString(TRACE_ID);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put(CORRELATION_ID, correlationId);
        MDC.put(TRACE_ID, traceId);
        ctx.getHeaders().putSingle(CORRELATION_ID, correlationId);
        ctx.getHeaders().putSingle(TRACE_ID, traceId);
    }

    @Override
    public void filter(ContainerRequestContext requestCtx, ContainerResponseContext responseCtx) throws IOException {
        String correlationId = requestCtx.getHeaderString(CORRELATION_ID);
        String traceId = requestCtx.getHeaderString(TRACE_ID);
        if (correlationId != null) {
            responseCtx.getHeaders().putSingle(CORRELATION_ID, correlationId);
        }
        if (traceId != null) {
            responseCtx.getHeaders().putSingle(TRACE_ID, traceId);
        }
        MDC.clear();
    }
}