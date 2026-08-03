package com.irsyad.pulse.orchestrator.integration.checkout;

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
import java.util.Optional;

/**
 * Service for validating merchant information
 */
@ApplicationScoped
public class MerchantService {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantService.class);

    @Inject
    private MerchantRestClient merchantRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 5, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getMerchantFallback")
    public MerchantInfo getMerchant(String merchantId) {
        LOG.info("Fetching merchant info for merchantId: {}", merchantId);

        Optional<MerchantResponse> response = merchantRestClient.getMerchant(merchantId);

        if (response.isPresent()) {
            MerchantResponse merchant = response.get();
            LOG.info("Merchant found: {}, active: {}, allowed: {}", merchantId, merchant.isActive(), merchant.isAllowed());
            return new MerchantInfo(merchant.getId(), merchant.getName(), merchant.isActive(), merchant.isAllowed());
        }

        LOG.warn("Merchant not found: {}", merchantId);
        return null;
    }

    public MerchantInfo getMerchantFallback(String merchantId) {
        LOG.warn("Falling back to default merchant info for: {}", merchantId);
        // In production, return a safe default or throw exception
        return null;
    }

    @RegisterRestClient(configKey = "merchant-service")
    public interface MerchantRestClient {
        @GET
        @Path("/merchant/{merchantId}")
        Optional<MerchantResponse> getMerchant(@PathParam("merchantId") String merchantId);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class MerchantResponse {
        private String id;
        private String name;
        private boolean active;
        private boolean allowed;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class MerchantInfo {
        private String id;
        private String name;
        private boolean active;
        private boolean allowed;
    }
}