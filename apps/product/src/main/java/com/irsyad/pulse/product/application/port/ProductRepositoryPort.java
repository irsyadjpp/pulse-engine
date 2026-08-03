package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Product persistence (Hexagonal Architecture).
 * Implemented by the JPA adapter in infrastructure layer.
 */
public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(UUID productId);

    Optional<Product> findByCompanyIdAndProductCode(UUID companyId, String productCode);

    List<Product> search(UUID companyId, String productCode, String productName, String category,
                         ProductStatus status, LocalDate effectiveDate, int page, int size);
}
