package com.irsyad.pulse.orchestrator.integration.velocity;

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
public class VelocityService {

    @ConfigProperty(name = "velocity.service.url", defaultValue = "http://localhost:7012")
    private String velocityServiceUrl;

    @Inject
    @RestClient
    private VelocityRestClient velocityRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 10, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getVelocityScoreFallback")
    public VelocityInfo getVelocityScore(String policyholderId) {
        // Call external VelocityService using REST Client
        VelocityResponse response = velocityRestClient.getVelocityScore(policyholderId);
        
        if (response != null) {
            VelocityInfo info = new VelocityInfo();
            info.setVelocityScore(response.getVelocityScore());
            return info;
        }
        
        // Fallback to mock if external service returns null
        return getMockVelocityScore(policyholderId);
    }

    public VelocityInfo getVelocityScoreFallback(String policyholderId) {
        // Fallback to mock if external service is unavailable
        return getMockVelocityScore(policyholderId);
    }

    private VelocityInfo getMockVelocityScore(String policyholderId) {
        // Mock velocity calculation
        VelocityInfo info = new VelocityInfo();
        
        if (policyholderId.startsWith("VIP_")) {
            info.setVelocityScore(25); // VIP policyholders have low velocity risk
        } else if (policyholderId.startsWith("NEW_")) {
            info.setVelocityScore(65); // New policyholders have higher velocity risk
        } else {
            info.setVelocityScore(45); // Regular policyholders have medium velocity risk
        }
        
        return info;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class VelocityInfo {
        private Integer velocityScore;
    }

    // DTO for external service response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class VelocityResponse {
        private String customerId;
        private Integer velocityScore;
        private String riskLevel;
        private String message;
    }
    
    @RegisterRestClient(configKey = "velocity-service")
    public interface VelocityRestClient {
        @GET
        @Path("/velocity/check/{customerId}")
        VelocityResponse getVelocityScore(@PathParam("customerId") String customerId);
    }
}