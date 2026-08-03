package com.irsyad.pulse.orchestrator.integration.customer;

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
public class CustomerService {

    @ConfigProperty(name = "customer.service.url", defaultValue = "http://localhost:7010")
    private String customerServiceUrl;

    @Inject
    @RestClient
    private CustomerRestClient customerRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 10, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getPolicyholderInfoFallback")
    public CustomerInfo getPolicyholderInfo(String policyholderId) {
        // Call external CustomerService using REST Client
        CustomerResponse response = customerRestClient.getCustomerInfo(policyholderId);
        
        if (response != null) {
            CustomerInfo info = new CustomerInfo();
            info.setCustomerType(response.getCustomerType());
            info.setVip(response.isVip());
            info.setNewPolicyholder(response.isFirstPurchase());
            info.setNik(response.getNik());
            info.setKycId(response.getKycId());
            return info;
        }
        
        // Fallback to mock if external service returns null
        return getMockPolicyholderInfo(policyholderId);
    }

    public CustomerInfo getPolicyholderInfoFallback(String policyholderId) {
        // Fallback to mock if external service is unavailable
        return getMockPolicyholderInfo(policyholderId);
    }

    private CustomerInfo getMockPolicyholderInfo(String policyholderId) {
        CustomerInfo info = new CustomerInfo();
        
        if (policyholderId.startsWith("VIP_")) {
            info.setCustomerType("VIP");
            info.setVip(true);
            info.setNewPolicyholder(false);
            info.setNik("3201010101010001");
            info.setKycId("KYC-VIP-001");
        } else if (policyholderId.startsWith("NEW_")) {
            info.setCustomerType("REGULAR");
            info.setVip(false);
            info.setNewPolicyholder(true);
            info.setNik("3201010101010002");
            info.setKycId("KYC-NEW-001");
        } else {
            info.setCustomerType("REGULAR");
            info.setVip(false);
            info.setNewPolicyholder(false);
            info.setNik("3201010101010003");
            info.setKycId("KYC-REG-001");
        }
        
        return info;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CustomerInfo {
        private String customerType;
        private boolean vip;
        private boolean newPolicyholder;
        private String nik;
        private String kycId;
    }

    // DTO for external service response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CustomerResponse {
        private String customerId;
        private String customerType;
        private boolean vip;
        private boolean firstPurchase;
        private String nik;
        private String kycId;
        private String message;
    }
    
    @RegisterRestClient(configKey = "customer-service")
    public interface CustomerRestClient {
        @GET
        @Path("/customer/info/{customerId}")
        CustomerResponse getCustomerInfo(@PathParam("customerId") String customerId);
    }
}