package com.irsyad.pulse.product.infrastructure.persistence.company;

import com.irsyad.pulse.product.application.port.CompanyRepositoryPort;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<Company> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.companyJpaRepository.findAll(pageable).stream()
                .filter(entity -> !entity.isDeleted())
                .map(this::toDomain)
                .toList();
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
