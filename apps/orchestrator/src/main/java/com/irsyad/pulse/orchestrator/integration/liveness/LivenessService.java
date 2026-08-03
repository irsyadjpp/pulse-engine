package com.irsyad.pulse.orchestrator.integration.liveness;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.Optional;

/**
 * Service for checking liveness (tanpa face matching)
 */
@ApplicationScoped
public class LivenessService {

    private static final Logger LOG = LoggerFactory.getLogger(LivenessService.class);

    @Inject
    @RestClient
    private LivenessRestClient livenessRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 5, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "checkLivenessFallback")
    public boolean checkLiveness(String customerId) {
        LOG.info("Checking liveness for customerId: {}", customerId);

        Optional<LivenessResponse> response = livenessRestClient.checkLiveness(customerId);

        if (response.isPresent()) {
            LivenessResponse liveness = response.get();
            LOG.info("Liveness check result for {}: passed={}, score={}", customerId, liveness.isPassed(), liveness.getScore());
            return liveness.isPassed();
        }

        LOG.warn("Liveness check failed for customer: {}", customerId);
        return false;
    }

    public boolean checkLivenessFallback(String customerId) {
        LOG.warn("Falling back to default liveness check for: {}", customerId);
        // In production, fail-safe: return false or throw exception
        return false;
    }

    @RegisterRestClient(configKey = "liveness-service")
    public interface LivenessRestClient {
        @POST
        @Path("/liveness/check/{customerId}")
        Optional<LivenessResponse> checkLiveness(@PathParam("customerId") String customerId);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class LivenessResponse {
        private boolean passed;
        private int score;
        private String message;
    }
}