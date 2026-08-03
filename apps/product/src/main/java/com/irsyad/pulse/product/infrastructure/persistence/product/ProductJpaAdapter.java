package com.irsyad.pulse.product.infrastructure.persistence.product;

import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing the ProductRepositoryPort (Hexagonal Architecture).
 * Converts between the domain Product and the JPA entity.
 */
@Component
public class ProductJpaAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductJpaAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity saved = this.productJpaRepository.save(this.toEntity(product));
        return this.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return this.productJpaRepository.findByProductIdAndDeletedFalse(productId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findByCompanyIdAndProductCode(UUID companyId, String productCode) {
        return this.productJpaRepository.findByCompanyIdAndProductCodeAndDeletedFalse(companyId, productCode)
                .map(this::toDomain);
    }

    @Override
    public List<Product> search(UUID companyId, String productCode, String productName, String category,
                                ProductStatus status, LocalDate effectiveDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.productJpaRepository.findAll(pageable).stream()
                .filter(entity -> !entity.isDeleted())
                .map(this::toDomain)
                .toList();
    }

    private ProductJpaEntity toEntity(Product product) {
        return ProductJpaEntity.builder()
                .productId(product.getProductId())
                .companyId(product.getCompanyId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .category(product.getCategory())
                .version(product.getVersion())
                .status(product.getStatus() != null ? product.getStatus() : ProductStatus.DRAFT)
                .effectiveDate(product.getEffectiveDate())
                .expiryDate(product.getExpiryDate())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .updatedAt(product.getUpdatedAt())
                .updatedBy(product.getUpdatedBy())
                .optimisticLockVersion(product.getOptimisticLockVersion())
                .deleted(product.isDeleted())
                .build();
    }

    private Product toDomain(ProductJpaEntity entity) {
        return Product.builder()
                .productId(entity.getProductId())
                .companyId(entity.getCompanyId())
                .productCode(entity.getProductCode())
                .productName(entity.getProductName())
                .category(entity.getCategory())
                .version(entity.getVersion())
                .status(entity.getStatus())
                .effectiveDate(entity.getEffectiveDate())
                .expiryDate(entity.getExpiryDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .optimisticLockVersion(entity.getOptimisticLockVersion())
                .deleted(entity.isDeleted())
                .build();
    }
}
