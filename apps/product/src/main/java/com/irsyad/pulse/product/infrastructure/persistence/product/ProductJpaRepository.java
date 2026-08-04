package com.irsyad.pulse.product.infrastructure.persistence.product;

import com.irsyad.pulse.product.domain.shared.ProductStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for Product persistence.
 * Infrastructure detail; domain never depends on this interface directly.
 */
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByIdAndDeletedFalse(UUID productId);

    Optional<ProductJpaEntity> findByCompanyIdAndProductCodeAndDeletedFalse(UUID companyId, String productCode);

    @Query("""
            SELECT p FROM ProductJpaEntity p
            WHERE p.deleted = false
              AND (:companyId IS NULL OR p.companyId = :companyId)
              AND (:productCode IS NULL OR LOWER(p.productCode) = LOWER(:productCode))
              AND (:productName IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%')))
              AND (:category IS NULL OR LOWER(p.category) = LOWER(:category))
              AND (:status IS NULL OR p.status = :status)
              AND (:effectiveDate IS NULL OR p.effectiveDate = :effectiveDate)
            """)
    List<ProductJpaEntity> search(@Param("companyId") UUID companyId,
                                  @Param("productCode") String productCode,
                                  @Param("productName") String productName,
                                  @Param("category") String category,
                                  @Param("status") ProductStatus status,
                                  @Param("effectiveDate") LocalDate effectiveDate,
                                  Pageable pageable);
}
