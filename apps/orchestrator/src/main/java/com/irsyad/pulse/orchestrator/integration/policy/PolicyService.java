package com.irsyad.pulse.orchestrator.integration.policy;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service for querying policy information
 */
@ApplicationScoped
public class PolicyService {

    private static final Logger LOG = LoggerFactory.getLogger(PolicyService.class);

    @Inject
    private PolicyRestClient policyRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 5, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getTotalActiveUPFallback")
    public PolicyInfo getTotalActiveUP(String customerId) {
        LOG.info("Querying total active UP for customerId: {}", customerId);

        Optional<PolicyResponse> response = policyRestClient.getTotalActiveUP(customerId);

        if (response.isPresent()) {
            PolicyResponse policy = response.get();
            LOG.info("Total active UP for {}: {}", customerId, policy.getTotalActiveUP());
            return new PolicyInfo(policy.getCustomerId(), policy.getTotalActiveUP());
        }

        LOG.warn("No policy found for customer: {}", customerId);
        return null;
    }

    public PolicyInfo getTotalActiveUPFallback(String customerId) {
        LOG.warn("Falling back to default policy info for: {}", customerId);
        // In production, return a safe default or throw exception
        return new PolicyInfo(customerId, BigDecimal.ZERO);
    }

    @RegisterRestClient(configKey = "policy-service")
    public interface PolicyRestClient {
        @GET
        @Path("/policy/total-up/{customerId}")
        Optional<PolicyResponse> getTotalActiveUP(@PathParam("customerId") String customerId);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class PolicyResponse {
        private String customerId;
        private BigDecimal totalActiveUP;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class PolicyInfo {
        private String customerId;
        private BigDecimal totalActiveUP;
    }
}