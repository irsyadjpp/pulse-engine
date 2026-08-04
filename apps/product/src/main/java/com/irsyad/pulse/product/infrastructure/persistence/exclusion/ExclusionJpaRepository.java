package com.irsyad.pulse.product.infrastructure.persistence.exclusion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExclusionJpaRepository extends JpaRepository<ExclusionJpaEntity, UUID> {
    List<ExclusionJpaEntity> findByProductVersionId(UUID productVersionId);
    void deleteByProductVersionId(UUID productVersionId);
}
