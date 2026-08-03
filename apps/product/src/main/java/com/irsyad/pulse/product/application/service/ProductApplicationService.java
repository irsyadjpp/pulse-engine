package com.irsyad.pulse.product.application.service;

import com.irsyad.pulse.product.application.command.product.CreateProductCommand;
import com.irsyad.pulse.product.application.command.product.UpdateProductCommand;
import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.application.query.product.SearchProductQuery;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Application service orchestrating Product use cases (FSD_02).
 * Product lifecycle operations must be executed through the Product aggregate.
 *
 * <p>Transaction boundary is at Application Layer (TSD_01 Section 18).
 */
@Service
public class ProductApplicationService {

    private final ProductRepositoryPort productRepositoryPort;

    public ProductApplicationService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Transactional
    public Product create(CreateProductCommand command) {
        this.productRepositoryPort.findByCompanyIdAndProductCode(command.companyId(), command.productCode())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Product Code already exists for this company.");
                });
        Product product = Product.builder()
                .productId(UUID.randomUUID())
                .companyId(command.companyId())
                .productCode(command.productCode())
                .productName(command.productName())
                .category(command.category())
                .version(1)
                .status(ProductStatus.DRAFT)
                .effectiveDate(command.effectiveDate())
                .expiryDate(command.expiryDate())
                .createdAt(Instant.now())
                .createdBy("system")
                .updatedAt(Instant.now())
                .updatedBy("system")
                .optimisticLockVersion(0L)
                .deleted(false)
                .build();
        return this.productRepositoryPort.save(product);
    }

    @Transactional
    public Product update(UpdateProductCommand command) {
        Product product = this.productRepositoryPort.findById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        product.updateDraft(command.productName(), command.category(),
                command.effectiveDate(), command.expiryDate());
        return this.productRepositoryPort.save(product);
    }

    @Transactional
    public Product publish(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        product.publish();
        return this.productRepositoryPort.save(product);
    }

    @Transactional
    public Product archive(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        product.archive();
        return this.productRepositoryPort.save(product);
    }

    @Transactional
    public Product createNewVersion(UUID productId) {
        Product product = this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        product.createNewVersion();
        return this.productRepositoryPort.save(product);
    }

    @Transactional(readOnly = true)
    public Product detail(UUID productId) {
        return this.productRepositoryPort.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
    }

    @Transactional(readOnly = true)
    public List<Product> search(SearchProductQuery query) {
        return this.productRepositoryPort.search(
                query.companyId(), query.productCode(), query.productName(), query.category(),
                query.status(), query.effectiveDate(), query.page(), query.size());
    }
}
