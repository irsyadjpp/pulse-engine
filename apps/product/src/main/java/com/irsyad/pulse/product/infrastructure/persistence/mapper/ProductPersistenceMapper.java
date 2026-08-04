package com.irsyad.pulse.product.infrastructure.persistence.mapper;

import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.infrastructure.persistence.product.ProductJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between Product domain aggregate and Product persistence representation.
 * Keeps JPA concerns isolated within the infrastructure layer.
 */
@Component
public class ProductPersistenceMapper {

    public ProductJpaEntity toEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getProductId())
                .companyId(product.getCompanyId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .category(product.getCategory())
                .currentVersion(product.getVersion())
                .status(product.getStatus())
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

    public Product toDomain(ProductJpaEntity entity) {
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