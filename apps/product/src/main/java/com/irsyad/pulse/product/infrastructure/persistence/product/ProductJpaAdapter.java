package com.irsyad.pulse.product.infrastructure.persistence.product;

import com.irsyad.pulse.product.application.port.ProductRepositoryPort;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        return this.productJpaRepository.findByIdAndDeletedFalse(productId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findByCompanyIdAndProductCode(UUID companyId, String productCode) {
        return this.productJpaRepository.findByCompanyIdAndProductCodeAndDeletedFalse(companyId, productCode)
                .map(this::toDomain);
    }

    @Override
    public Page<Product> search(UUID companyId, String productCode, String productName, String category,
                                ProductStatus status, LocalDate effectiveDate, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, this.sort(sort));
        return this.productJpaRepository.search(companyId, productCode, productName, category,
                        status, effectiveDate, pageable)
                .map(this::toDomain);
    }

    private Sort sort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "productName");
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }

    private ProductJpaEntity toEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getProductId())
                .companyId(product.getCompanyId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .category(product.getCategory())
                .currentVersion(product.getVersion())
                .status(product.getStatus() != null ? product.getStatus() : ProductStatus.DRAFT)
                .effectiveDate(product.getEffectiveDate())
                .expiryDate(product.getExpiryDate())
                .createdAt(product.getCreatedAt())
                .createdBy(product.getCreatedBy())
                .updatedAt(product.getUpdatedAt())
                .updatedBy(product.getUpdatedBy())
                .version(product.getOptimisticLockVersion())
                .deleted(product.isDeleted())
                .build();
    }

    private Product toDomain(ProductJpaEntity entity) {
        return Product.builder()
                .productId(entity.getId())
                .companyId(entity.getCompanyId())
                .productCode(entity.getProductCode())
                .productName(entity.getProductName())
                .category(entity.getCategory())
                .version(entity.getCurrentVersion())
                .status(entity.getStatus())
                .effectiveDate(entity.getEffectiveDate())
                .expiryDate(entity.getExpiryDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .optimisticLockVersion(entity.getVersion())
                .deleted(entity.isDeleted())
                .build();
    }
}
