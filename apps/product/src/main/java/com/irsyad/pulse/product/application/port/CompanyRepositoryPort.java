package com.irsyad.pulse.product.application.port;

import com.irsyad.pulse.product.domain.company.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for Company persistence (Hexagonal Architecture).
 * Implemented by the JPA adapter in infrastructure layer.
 */
public interface CompanyRepositoryPort {

    Company save(Company company);

    Optional<Company> findById(UUID companyId);

    Optional<Company> findByCompanyCode(String companyCode);

    List<Company> search(String keyword, int page, int size);
}
