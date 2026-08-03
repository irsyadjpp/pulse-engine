package com.irsyad.pulse.orchestrator.integration.fraud;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class FraudService {

    @ConfigProperty(name = "fraud.service.url", defaultValue = "http://localhost:7013")
    private String fraudServiceUrl;

    @Inject
    @RestClient
    private FraudRestClient fraudRestClient;

    @CircuitBreaker(failureRatio = 0.4, delay = 60000, requestVolumeThreshold = 5)
    @Timeout(value = 10, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getFraudScoreFallback")
    public FraudInfo getFraudScore(String policyholderId, String sumAssured) {
        // Call external FraudService using REST Client
        FraudResponse response = fraudRestClient.getFraudScore(policyholderId);
        
        if (response != null) {
            FraudInfo info = new FraudInfo();
            info.setFraudScore(response.getFraudScore());
            return info;
        }
        
        // Fallback to mock if external service returns null
        return getMockFraudScore(policyholderId, sumAssured);
    }

    public FraudInfo getFraudScoreFallback(String policyholderId, String sumAssured) {
        // Fallback to mock if external service is unavailable
        return getMockFraudScore(policyholderId, sumAssured);
    }

    private FraudInfo getMockFraudScore(String policyholderId, String sumAssured) {
        // Mock fraud score calculation
        FraudInfo info = new FraudInfo();
        
        if (policyholderId.startsWith("VIP_")) {
            info.setFraudScore(15); // VIP policyholders have low fraud risk
        } else if (policyholderId.startsWith("NEW_")) {
            info.setFraudScore(35); // New policyholders have higher fraud risk
        } else {
            info.setFraudScore(20); // Regular policyholders have medium fraud risk
        }
        
        return info;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class FraudInfo {
        private Integer fraudScore;
    }

    // DTO for external service response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class FraudResponse {
        private String customerId;
        private Integer fraudScore;
        private String riskLevel;
        private String message;
    }
    
    @RegisterRestClient(configKey = "fraud-service")
    public interface FraudRestClient {
        @GET
        @Path("/fraud/check/{customerId}")
        FraudResponse getFraudScore(@PathParam("customerId") String customerId);
    }
}