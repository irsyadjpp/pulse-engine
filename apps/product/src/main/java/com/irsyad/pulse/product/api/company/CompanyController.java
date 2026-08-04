package com.irsyad.pulse.product.api.company;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.api.common.PageResult;
import com.irsyad.pulse.product.application.command.company.CreateCompanyCommand;
import com.irsyad.pulse.product.application.command.company.UpdateCompanyCommand;
import com.irsyad.pulse.product.application.query.company.SearchCompanyQuery;
import com.irsyad.pulse.product.application.service.CompanyApplicationService;
import com.irsyad.pulse.product.domain.company.Company;
import com.irsyad.pulse.product.domain.shared.CompanyStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for Insurance Company Management (FSD_01, TSD_04 Section 7).
 */
@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Company", description = "Insurance Company Management APIs")
public class CompanyController {

    private final CompanyApplicationService companyApplicationService;

    public CompanyController(CompanyApplicationService companyApplicationService) {
        this.companyApplicationService = companyApplicationService;
    }

    @PostMapping
    @Operation(summary = "Create Company", description = "Creates a new Insurance Company (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<CompanyResponse>> create(@Valid @RequestBody CreateCompanyRequest request) {
        CreateCompanyCommand command = new CreateCompanyCommand(
                request.companyCode(), request.companyName(), request.logoUrl(), request.contactInformation());
        Company company = this.companyApplicationService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(this.toResponse(company)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Company", description = "Updates an existing Insurance Company (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<CompanyResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody UpdateCompanyRequest request) {
        UpdateCompanyCommand command = new UpdateCompanyCommand(
                id, request.companyName(), request.logoUrl(), request.contactInformation());
        Company company = this.companyApplicationService.update(command);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(company)));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate Company", description = "Activates an Insurance Company (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<CompanyResponse>> activate(@PathVariable UUID id) {
        Company company = this.companyApplicationService.activate(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(company)));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate Company", description = "Deactivates an Insurance Company (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<CompanyResponse>> deactivate(@PathVariable UUID id) {
        Company company = this.companyApplicationService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(company)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Company Detail", description = "Returns a single Insurance Company by id.")
    public ResponseEntity<ApiResponse<CompanyResponse>> detail(@PathVariable UUID id) {
        Company company = this.companyApplicationService.detail(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(company)));
    }

    @GetMapping
    @Operation(summary = "Search Company", description = "Searches Insurance Companies with pagination and filtering (TSD_04 Section 12-14).")
    public ResponseEntity<ApiResponse<PageResult<CompanyResponse>>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CompanyStatus status,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (size > 100) {
            throw new IllegalArgumentException("size must not exceed 100 (TSD_04 Section 28.2).");
        }
        SearchCompanyQuery query = new SearchCompanyQuery(keyword, status, sort, page, size);
        Page<Company> resultPage = this.companyApplicationService.search(query);
        PageResult<CompanyResponse> result = PageResult.of(
                resultPage.getContent().stream().map(this::toResponse).toList(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getCompanyId(),
                company.getCompanyCode(),
                company.getCompanyName(),
                company.getLogoUrl(),
                company.getStatus());
    }
}