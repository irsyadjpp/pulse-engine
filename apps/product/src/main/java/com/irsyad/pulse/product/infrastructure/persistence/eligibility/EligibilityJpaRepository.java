package com.irsyad.pulse.product.infrastructure.persistence.eligibility;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EligibilityJpaRepository extends JpaRepository<EligibilityJpaEntity, UUID> {
    Optional<EligibilityJpaEntity> findByProductVersionId(UUID productVersionId);
    void deleteByProductVersionId(UUID productVersionId);
}
