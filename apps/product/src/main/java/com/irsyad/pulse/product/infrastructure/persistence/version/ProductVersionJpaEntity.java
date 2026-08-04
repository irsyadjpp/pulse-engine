package com.irsyad.pulse.product.infrastructure.persistence.version;

import com.irsyad.pulse.product.domain.shared.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA entity mapping the product_version table (Appendix O).
 * Immutable snapshot of a product version.
 */
@Entity
@Table(name = "product_version")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "snapshot", columnDefinition = "JSONB")
    private String snapshot;

    @Column(name = "published_by", length = 100)
    private String publishedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "updated_by", nullable = false, length = 100)
    private String updatedBy;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
