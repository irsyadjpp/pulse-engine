package com.irsyad.pulse.product.infrastructure.persistence.company;

import com.irsyad.pulse.product.application.port.CompanyRepositoryPort;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA adapter implementing the CompanyRepositoryPort (Hexagonal Architecture).
 * Converts between the domain Company and the JPA entity.
 */
@Component
public class CompanyJpaAdapter implements CompanyRepositoryPort {

    private final CompanyJpaRepository companyJpaRepository;

    public CompanyJpaAdapter(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Company save(Company company) {
        CompanyJpaEntity entity = this.toEntity(company);
        CompanyJpaEntity saved = this.companyJpaRepository.save(entity);
        return this.toDomain(saved);
    }

    @Override
    public Optional<Company> findById(UUID companyId) {
        return this.companyJpaRepository.findByIdAndDeletedFalse(companyId)
                .map(this::toDomain);
    }

    @Override
    public Optional<Company> findByCompanyCode(String companyCode) {
        return this.companyJpaRepository.findByCompanyCodeAndDeletedFalse(companyCode)
                .map(this::toDomain);
    }

    @Override
    public Page<Company> search(String keyword, CompanyStatus status, String sort, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, this.sort(sort));
        return this.companyJpaRepository.search(keyword, status, pageable)
                .map(this::toDomain);
    }

    private Sort sort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.ASC, "companyName");
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, property);
    }

    private CompanyJpaEntity toEntity(Company company) {
        return CompanyJpaEntity.builder()
                .id(company.getCompanyId())
                .companyCode(company.getCompanyCode())
                .companyName(company.getCompanyName())
                .logoUrl(company.getLogoUrl())
                .contactInformation(company.getContactInformation())
                .status(company.getStatus() != null ? company.getStatus() : CompanyStatus.ACTIVE)
                .createdAt(company.getCreatedAt())
                .createdBy(company.getCreatedBy())
                .updatedAt(company.getUpdatedAt())
                .updatedBy(company.getUpdatedBy())
                .version(company.getVersion())
                .deleted(company.isDeleted())
                .build();
    }

    private Company toDomain(CompanyJpaEntity entity) {
        return Company.builder()
                .companyId(entity.getId())
                .companyCode(entity.getCompanyCode())
                .companyName(entity.getCompanyName())
                .logoUrl(entity.getLogoUrl())
                .contactInformation(entity.getContactInformation())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .version(entity.getVersion())
                .deleted(entity.isDeleted())
                .build();
    }
}
