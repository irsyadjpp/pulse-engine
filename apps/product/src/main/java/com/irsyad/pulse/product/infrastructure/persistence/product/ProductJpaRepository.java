package com.irsyad.pulse.product.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Product persistence.
 * Infrastructure detail; domain never depends on this interface directly.
 */
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByProductIdAndDeletedFalse(UUID productId);

    Optional<ProductJpaEntity> findByCompanyIdAndProductCodeAndDeletedFalse(UUID companyId, String productCode);
}
