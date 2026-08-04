package com.irsyad.pulse.product.infrastructure.persistence.version;

import com.irsyad.pulse.product.application.port.ProductVersionPort;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
import com.irsyad.pulse.product.domain.version.ProductVersion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing the ProductVersionPort (Hexagonal Architecture).
 */
@Component
public class ProductVersionJpaAdapter implements ProductVersionPort {

    private final ProductVersionJpaRepository productVersionJpaRepository;

    public ProductVersionJpaAdapter(ProductVersionJpaRepository productVersionJpaRepository) {
        this.productVersionJpaRepository = productVersionJpaRepository;
    }

    @Override
    public ProductVersion save(ProductVersion version) {
        ProductVersionJpaEntity saved = this.productVersionJpaRepository.save(this.toEntity(version));
        return this.toDomain(saved);
    }

    @Override
    public List<ProductVersion> findByProductId(UUID productId) {
        return this.productVersionJpaRepository.findByProductIdOrderByVersionAsc(productId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<ProductVersion> findByProductIdAndVersion(UUID productId, int version) {
        return this.productVersionJpaRepository.findByProductIdAndVersion(productId, version)
                .map(this::toDomain);
    }

    private ProductVersionJpaEntity toEntity(ProductVersion version) {
        return ProductVersionJpaEntity.builder()
                .productVersionId(version.getProductVersionId())
                .productId(version.getProductId())
                .version(version.getVersion())
                .status(version.getStatus() != null ? version.getStatus() : ProductStatus.DRAFT)
                .effectiveDate(version.getEffectiveDate())
                .publishedDate(version.getPublishedDate())
                .createdAt(version.getCreatedAt())
                .createdBy(version.getCreatedBy())
                .build();
    }

    private ProductVersion toDomain(ProductVersionJpaEntity entity) {
        return ProductVersion.builder()
                .productVersionId(entity.getProductVersionId())
                .productId(entity.getProductId())
                .version(entity.getVersion())
                .status(entity.getStatus())
                .effectiveDate(entity.getEffectiveDate())
                .publishedDate(entity.getPublishedDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }
}
