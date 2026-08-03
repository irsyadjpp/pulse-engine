package com.irsyad.pulse.orchestrator.integration.dukcapil;

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
public class DukcapilService {

    @ConfigProperty(name = "dukcapil.service.url", defaultValue = "http://localhost:7011")
    private String dukcapilServiceUrl;

    @Inject
    @RestClient
    private DukcapilRestClient dukcapilRestClient;

    @CircuitBreaker(failureRatio = 0.6, delay = 45000, requestVolumeThreshold = 3)
    @Timeout(value = 10, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "verifyIdentityFallback")
    public DukcapilInfo verifyIdentity(String policyholderNik) {
        // Call external DukcapilService using REST Client
        DukcapilResponse response = dukcapilRestClient.verifyIdentity(policyholderNik);
        
        if (response != null) {
            DukcapilInfo info = new DukcapilInfo();
            info.setStatus(response.getStatus());
            info.setDukcapilStatus(response.getDukcapilStatus());
            return info;
        }
        
        // Fallback to mock if external service returns null
        return getMockDukcapilInfo(policyholderNik);
    }

    public DukcapilInfo verifyIdentityFallback(String policyholderNik) {
        // Fallback to mock if external service is unavailable
        return getMockDukcapilInfo(policyholderNik);
    }

    private DukcapilInfo getMockDukcapilInfo(String policyholderNik) {
        // Mock Dukcapil verification for policyholder
        DukcapilInfo info = new DukcapilInfo();
        
        if (policyholderNik == null || policyholderNik.isEmpty()) {
            info.setStatus("NOT_FOUND");
        } else if (policyholderNik.startsWith("320")) {
            info.setStatus("MATCH");
            info.setDukcapilStatus("VALID");
        } else {
            info.setStatus("NOT_MATCH");
            info.setDukcapilStatus("INVALID");
        }
        
        return info;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DukcapilInfo {
        private String status; // MATCH, NOT_MATCH, NOT_FOUND
        private String dukcapilStatus; // VALID, INVALID
    }

    // DTO for external service response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class DukcapilResponse {
        private String nik;
        private String status;
        private String dukcapilStatus;
        private String message;
    }
    
    @RegisterRestClient(configKey = "dukcapil-service")
    public interface DukcapilRestClient {
        @GET
        @Path("/dukcapil/verify/{nik}")
        DukcapilResponse verifyIdentity(@PathParam("nik") String nik);
    }
}