package com.irsyad.pulse.product.infrastructure.persistence.company;

import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Company persistence.
 * Infrastructure detail; domain never depends on this interface directly.
 */
public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, UUID> {

    Optional<CompanyJpaEntity> findByCompanyCodeAndDeletedFalse(String companyCode);

    Optional<CompanyJpaEntity> findByIdAndDeletedFalse(UUID companyId);

    @Query("""
            SELECT c FROM CompanyJpaEntity c
            WHERE c.deleted = false
              AND (:keyword IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(c.companyCode) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:status IS NULL OR c.status = :status)
            """)
    Page<CompanyJpaEntity> search(@Param("keyword") String keyword,
                                  @Param("status") CompanyStatus status,
                                  Pageable pageable);
}
