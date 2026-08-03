package com.irsyad.pulse.orchestrator.integration.checkout;

import com.irsyad.pulse.orchestrator.api.dto.request.CheckoutApiRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckoutCalculationService {

    @ConfigProperty(name = "checkout.service.url", defaultValue = "http://localhost:7014")
    private String checkoutServiceUrl;

    @Inject
    @RestClient
    private CheckoutRestClient checkoutRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 10, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "calculateAmountFallback")
    public BigDecimal calculateAmount(String checkoutId, String customerId,
                                      List<CheckoutApiRequest.Item> items, String paymentMethod) {
        // Call external CheckoutService using REST Client
        CheckoutCalculationRequest request = new CheckoutCalculationRequest();
        request.setCheckoutId(checkoutId);
        request.setCustomerId(customerId);
        request.setPaymentMethod(paymentMethod);
        request.setCurrency("IDR");
        
        // Convert items
        List<PolicyItem> policyItems = items.stream()
                .map(item -> {
                    PolicyItem policyItem = new PolicyItem();
                    policyItem.setProductId(item.getProductId());
                    policyItem.setQuantity(item.getQuantity());
                    policyItem.setUnitPrice(null); // Let service use default price
                    return policyItem;
                })
                .collect(Collectors.toList());
        request.setItems(policyItems);
        
        CheckoutCalculationResponse response = checkoutRestClient.calculateAmount(request);
        
        if (response != null && response.getTotalAmount() != null) {
            return response.getTotalAmount();
        }
        
        // Fallback to mock if external service returns null
        return getMockAmount(items);
    }

    public BigDecimal calculateAmountFallback(String checkoutId, String customerId,
                                              List<CheckoutApiRequest.Item> items, String paymentMethod) {
        // Fallback to mock if external service is unavailable
        return getMockAmount(items);
    }

    private BigDecimal getMockAmount(List<CheckoutApiRequest.Item> items) {
        // Simplified calculation - in real scenario would fetch product prices
        // For demo, assume each item costs 1,000,000 IDR
        int itemCount = items != null ? items.size() : 1;
        return new BigDecimal(itemCount * 1000000);
    }

    // DTO for external service request
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutCalculationRequest {
        private String checkoutId;
        private String customerId;
        private List<PolicyItem> items;
        private String paymentMethod;
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PolicyItem {
        private String productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }

    // DTO for external service response
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckoutCalculationResponse {
        private String checkoutId;
        private BigDecimal subtotal;
        private BigDecimal tax;
        private BigDecimal discount;
        private BigDecimal totalAmount;
        private String currency;
        private String message;
    }
    
    @RegisterRestClient(configKey = "checkout-service")
    public interface CheckoutRestClient {
        @POST
        @Path("/checkout/calculate")
        @Produces(MediaType.APPLICATION_JSON)
        CheckoutCalculationResponse calculateAmount(CheckoutCalculationRequest request);
    }
}