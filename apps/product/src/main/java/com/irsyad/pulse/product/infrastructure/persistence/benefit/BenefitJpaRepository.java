package com.irsyad.pulse.product.infrastructure.persistence.benefit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BenefitJpaRepository extends JpaRepository<BenefitJpaEntity, UUID> {
    List<BenefitJpaEntity> findByProductVersionId(UUID productVersionId);
    void deleteByProductVersionId(UUID productVersionId);
}
