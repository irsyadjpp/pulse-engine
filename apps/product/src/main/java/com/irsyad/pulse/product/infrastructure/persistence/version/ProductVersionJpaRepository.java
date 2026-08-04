package com.irsyad.pulse.product.infrastructure.persistence.version;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductVersionJpaRepository extends JpaRepository<ProductVersionJpaEntity, UUID> {
    List<ProductVersionJpaEntity> findByProductIdOrderByVersionNumberAsc(UUID productId);
    Optional<ProductVersionJpaEntity> findByProductIdAndVersionNumber(UUID productId, int versionNumber);
}
