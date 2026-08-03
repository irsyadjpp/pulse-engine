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
 * Service for validating product information
 */
@ApplicationScoped
public class ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Inject
    private ProductRestClient productRestClient;

    @CircuitBreaker(failureRatio = 0.5, delay = 30000, requestVolumeThreshold = 5)
    @Timeout(value = 5, unit = java.time.temporal.ChronoUnit.SECONDS)
    @Fallback(fallbackMethod = "getProductFallback")
    public ProductInfo getProduct(String productId) {
        LOG.info("Fetching product info for productId: {}", productId);

        Optional<ProductResponse> response = productRestClient.getProduct(productId);

        if (response.isPresent()) {
            ProductResponse product = response.get();
            LOG.info("Product found: {}, active: {}, available: {}", productId, product.isActive(), product.isAvailable());
            return new ProductInfo(product.getId(), product.getName(), product.isActive(), product.isAvailable());
        }

        LOG.warn("Product not found: {}", productId);
        return null;
    }

    public ProductInfo getProductFallback(String productId) {
        LOG.warn("Falling back to default product info for: {}", productId);
        // In production, return a safe default or throw exception
        return null;
    }

    @RegisterRestClient(configKey = "product-service")
    public interface ProductRestClient {
        @GET
        @Path("/product/{productId}")
        Optional<ProductResponse> getProduct(@PathParam("productId") String productId);
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class ProductResponse {
        private String id;
        private String name;
        private boolean active;
        private boolean available;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Getter
    @lombok.Setter
    public static class ProductInfo {
        private String id;
        private String name;
        private boolean active;
        private boolean available;
    }
}