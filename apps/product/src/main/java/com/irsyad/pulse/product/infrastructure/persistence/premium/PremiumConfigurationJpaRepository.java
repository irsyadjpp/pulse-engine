package com.irsyad.pulse.product.infrastructure.persistence.premium;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PremiumConfigurationJpaRepository extends JpaRepository<PremiumConfigurationJpaEntity, UUID> {
    List<PremiumConfigurationJpaEntity> findByProductVersionId(UUID productVersionId);
    void deleteByProductVersionId(UUID productVersionId);
}
