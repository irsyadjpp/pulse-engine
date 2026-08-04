package com.irsyad.pulse.product.api.product;

import com.irsyad.pulse.product.api.common.ApiResponse;
import com.irsyad.pulse.product.api.common.PageResult;
import com.irsyad.pulse.product.application.command.product.CreateProductCommand;
import com.irsyad.pulse.product.application.command.product.UpdateProductCommand;
import com.irsyad.pulse.product.application.query.product.SearchProductQuery;
import com.irsyad.pulse.product.application.service.ProductApplicationService;
import com.irsyad.pulse.product.application.service.ProductQueryService;
import com.irsyad.pulse.product.domain.product.Product;
import com.irsyad.pulse.product.domain.shared.ProductStatus;
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

import java.time.LocalDate;
import java.util.UUID;

/**
 * REST controller for Product Management (FSD_02, TSD_04 Section 8).
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "Product Management APIs")
public class ProductController {

    private final ProductApplicationService productApplicationService;
    private final ProductQueryService productQueryService;

    public ProductController(ProductApplicationService productApplicationService,
                             ProductQueryService productQueryService) {
        this.productApplicationService = productApplicationService;
        this.productQueryService = productQueryService;
    }

    @PostMapping
    @Operation(summary = "Create Product", description = "Creates a new Product Draft (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(
                request.companyId(), request.productCode(), request.productName(), request.category(),
                request.effectiveDate(), request.expiryDate());
        Product product = this.productApplicationService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(this.toResponse(product)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product", description = "Updates a Draft Product (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductCommand command = new UpdateProductCommand(
                id, request.productName(), request.category(), request.effectiveDate(), request.expiryDate());
        Product product = this.productApplicationService.update(command);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(product)));
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish Product", description = "Publishes a Draft Product (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<ProductResponse>> publish(@PathVariable UUID id) {
        Product product = this.productApplicationService.publish(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(product)));
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive Product", description = "Archives a Published Product (PRODUCT_ADMIN).")
    public ResponseEntity<ApiResponse<ProductResponse>> archive(@PathVariable UUID id) {
        Product product = this.productApplicationService.archive(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(product)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Product Detail", description = "Returns a single Product by id.")
    public ResponseEntity<ApiResponse<ProductResponse>> detail(@PathVariable UUID id) {
        Product product = this.productQueryService.detail(id);
        return ResponseEntity.ok(ApiResponse.success(this.toResponse(product)));
    }

    @GetMapping
    @Operation(summary = "Search Product", description = "Searches Products with pagination, filtering and sorting (TSD_04 Section 12-14).")
    public ResponseEntity<ApiResponse<PageResult<ProductResponse>>> search(
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) LocalDate effectiveDate,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (size > 100) {
            throw new IllegalArgumentException("size must not exceed 100 (TSD_04 Section 28.2).");
        }
        SearchProductQuery query = new SearchProductQuery(
                companyId, productCode, productName, category, status, effectiveDate, sort, page, size);
        Page<Product> resultPage = this.productQueryService.search(query);
        PageResult<ProductResponse> result = PageResult.of(
                resultPage.getContent().stream().map(this::toResponse).toList(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getProductId(),
                product.getCompanyId(),
                product.getProductCode(),
                product.getProductName(),
                product.getCategory(),
                product.getVersion(),
                product.getStatus(),
                product.getEffectiveDate(),
                product.getExpiryDate());
    }
}
