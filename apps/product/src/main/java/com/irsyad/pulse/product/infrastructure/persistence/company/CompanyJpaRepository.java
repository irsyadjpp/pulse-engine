package com.irsyad.pulse.product.infrastructure.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Company persistence.
 * Infrastructure detail; domain never depends on this interface directly.
 */
public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, UUID> {

    Optional<CompanyJpaEntity> findByCompanyCodeAndDeletedFalse(String companyCode);

    Optional<CompanyJpaEntity> findByCompanyIdAndDeletedFalse(UUID companyId);
}
