# TSD_10_ERROR_HANDLING.md

> **Technical Specification Document (TSD)**  
> **Module:** Error Handling  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft

---

# 1. Purpose

Dokumen ini mendefinisikan standar penanganan error pada Product Catalog Service.

Tujuan utama:

- menghasilkan error yang konsisten
- memudahkan debugging
- memudahkan observability
- memudahkan integrasi antar service
- menghindari kebocoran informasi sensitif
- mendukung automated monitoring

Semua endpoint wajib mengikuti standar ini.

---

# 2. Objectives

Error Handling harus memenuhi prinsip berikut.

- Predictable
- Consistent
- Traceable
- Secure
- Machine Readable
- Human Readable

---

# 3. Design Principles

## Standardized Error Response

Seluruh API mengembalikan format yang sama.

---

## Fail Fast

Validasi dilakukan sedini mungkin.

---

## No Sensitive Information

Response tidak boleh mengandung:

- SQL Query
- Stacktrace
- Password
- JWT
- Internal Server Path
- Connection String

---

## Structured Logging

Seluruh exception wajib dicatat.

---

## Correlation ID

Setiap error memiliki Correlation ID.

---

# 4. Error Classification

| Category | Description |
| ----------- | ------------- |
| Validation Error | Kesalahan input |
| Business Error | Pelanggaran business rule |
| Authentication Error | Token tidak valid |
| Authorization Error | Hak akses tidak cukup |
| Resource Error | Data tidak ditemukan |
| Conflict Error | Konflik data |
| Infrastructure Error | Database, Redis, Network |
| Unexpected Error | Internal Server Error |

---

# 5. Exception Hierarchy

```mermaid
classDiagram

Exception <|-- ProductCatalogException

ProductCatalogException <|-- ValidationException

ProductCatalogException <|-- BusinessRuleException

ProductCatalogException <|-- ResourceNotFoundException

ProductCatalogException <|-- ConflictException

ProductCatalogException <|-- InfrastructureException

InfrastructureException <|-- DatabaseException

InfrastructureException <|-- RedisException
```

---

# 6. HTTP Status Mapping

| Status | Description |
| ---------- | ------------- |
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Validation Error |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Resource Not Found |
| 409 | Conflict |
| 422 | Business Rule Violation |
| 429 | Too Many Requests* |
| 500 | Internal Server Error |
| 503 | Service Unavailable |

\* Bergantung implementasi Rate Limiting.

---

# 7. Error Response Format

Menggunakan format yang konsisten.

```json
{
  "timestamp": "2026-08-03T10:15:20Z",
  "status": 409,
  "error": "Conflict",
  "code": "PRODUCT_ALREADY_PUBLISHED",
  "message": "Published product cannot be modified.",
  "path": "/api/v1/products/123",
  "correlationId": "7bde5b1cfd9a",
  "traceId": "8cf9a6e5f4a7"
}
```

---

# 8. Error Response Fields

| Field | Description |
| --------- | ------------- |
| timestamp | Waktu error |
| status | HTTP Status |
| error | HTTP Description |
| code | Business Error Code |
| message | User-friendly message |
| path | Endpoint |
| correlationId | Correlation ID |
| traceId | Distributed Trace ID |

---

# 9. Business Error Codes

## Company

| Code | Description |
| -------- | ------------- |
| COMPANY_NOT_FOUND | Company tidak ditemukan |
| COMPANY_ALREADY_EXISTS | Company sudah ada |
| COMPANY_ALREADY_ACTIVE | Company sudah aktif |
| COMPANY_ALREADY_INACTIVE | Company sudah nonaktif |

---

## Product

| Code | Description |
| --------- | ------------ |
| PRODUCT_NOT_FOUND | Product tidak ditemukan |
| PRODUCT_ALREADY_EXISTS | Product Code sudah digunakan |
| PRODUCT_ALREADY_PUBLISHED | Product sudah dipublish |
| PRODUCT_ALREADY_ARCHIVED | Product sudah diarsipkan |
| PRODUCT_INVALID_STATE | Status Product tidak valid |

---

## Version

| Code | Description |
| --------- | ------------ |
| VERSION_NOT_FOUND | Version tidak ditemukan |
| VERSION_ALREADY_EXISTS | Version sudah ada |

---

## Validation

| Code | Description |
| -------- | ------------ |
| VALIDATION_ERROR | Request tidak valid |
| REQUIRED_FIELD | Mandatory field kosong |
| INVALID_FORMAT | Format tidak valid |

---

# 10. Validation Error

Contoh

```json
{
  "status":400,
  "code":"VALIDATION_ERROR",
  "message":"Validation failed.",
  "errors":[
    {
      "field":"productName",
      "message":"must not be blank"
    },
    {
      "field":"companyId",
      "message":"must not be null"
    }
  ]
}
```

---

# 11. Business Rule Error

Contoh

```json
{
  "status":422,
  "code":"PRODUCT_ALREADY_PUBLISHED",
  "message":"Published product cannot be modified."
}
```

---

# 12. Resource Not Found

```json
{
  "status":404,
  "code":"PRODUCT_NOT_FOUND",
  "message":"Product not found."
}
```

---

# 13. Authentication Error

```json
{
  "status":401,
  "code":"INVALID_TOKEN",
  "message":"Authentication failed."
}
```

---

# 14. Authorization Error

```json
{
  "status":403,
  "code":"ACCESS_DENIED",
  "message":"Access denied."
}
```

---

# 15. Conflict Error

Optimistic Lock.

```json
{
  "status":409,
  "code":"OPTIMISTIC_LOCK",
  "message":"The resource has been modified by another user."
}
```

---

# 16. Infrastructure Error

Database.

```json
{
  "status":503,
  "code":"DATABASE_UNAVAILABLE",
  "message":"Service temporarily unavailable."
}
```

---

# 17. Internal Server Error

```json
{
  "status":500,
  "code":"INTERNAL_SERVER_ERROR",
  "message":"Unexpected error occurred."
}
```

Tidak boleh mengembalikan stacktrace.

---

# 18. Exception Flow

```mermaid
flowchart TD

Request

↓

Validation

↓

Business Rule

↓

Repository

↓

Response

Request --> Validation

Validation --> BusinessRule

BusinessRule --> Repository

Repository --> Response

Validation --> Exception

BusinessRule --> Exception

Repository --> Exception

Exception --> GlobalExceptionHandler
```

---

# 19. Global Exception Handler

Menggunakan `@RestControllerAdvice`.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

}
```

---

# 20. Validation Handler

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiErrorResponse> handleValidation(...) {

}
```

---

# 21. Business Exception

```java
public class BusinessRuleException
        extends ProductCatalogException {

}
```

---

# 22. Resource Not Found

```java
public class ResourceNotFoundException
        extends ProductCatalogException {

}
```

---

# 23. Conflict Exception

```java
public class ConflictException
        extends ProductCatalogException {

}
```

---

# 24. Infrastructure Exception

```java
public class InfrastructureException
        extends RuntimeException {

}
```

---

# 25. Retry Strategy

Retry hanya diperbolehkan untuk:

- Redis Timeout
- Connection Timeout
- Temporary Network Failure

Tidak boleh Retry untuk:

- Validation Error
- Business Rule Error
- Conflict Error

---

# 26. Logging Strategy

Semua exception dicatat.

| Exception | Log Level |
| ----------- | ----------- |
| Validation | WARN |
| Business | WARN |
| Authentication | WARN |
| Authorization | WARN |
| Conflict | WARN |
| Database | ERROR |
| Redis | ERROR |
| Unknown | ERROR |

---

# 27. Correlation ID

Seluruh exception wajib memiliki.

```
X-Correlation-ID
```

Jika tidak ada.

Service membuat otomatis.

---

# 28. Trace ID

Menggunakan OpenTelemetry.

```
Trace ID

↓

Span ID
```

---

# 29. Sensitive Data

Tidak boleh dicatat.

- JWT
- Password
- Authorization Header
- Database Password
- Connection URL

---

# 30. Database Exception Mapping

| Exception | HTTP |
| ------------ | ------ |
| Duplicate Key | 409 |
| FK Constraint | 409 |
| Timeout | 503 |
| Connection Failed | 503 |

---

# 31. Optimistic Lock

```java
OptimisticLockingFailureException
```

↓

```
409 Conflict
```

---

# 32. Spring Validation

Menggunakan

```java
@Valid

@NotBlank

@NotNull

@Pattern

@Size
```

---

# 33. Java Error Response

```java
public record ApiErrorResponse(

        Instant timestamp,

        Integer status,

        String error,

        String code,

        String message,

        String path,

        String correlationId,

        String traceId

) {
}
```

---

# 34. Sequence Diagram

```mermaid
sequenceDiagram

actor Client

participant Controller

participant Service

participant Repository

participant ExceptionHandler

Client->>Controller

Controller->>Service

Service->>Repository

Repository-->>Service: Exception

Service-->>Controller

Controller->>ExceptionHandler

ExceptionHandler-->>Client
```

---

# 35. RFC Compliance

Direkomendasikan mengikuti **RFC 7807 (Problem Details for HTTP APIs)** sebagai standar error response agar interoperabilitas lebih baik dengan consumer lintas platform.

> **Catatan:** Struktur response pada dokumen ini dapat dipetakan ke RFC 7807 tanpa mengubah business behavior.

---

# 36. Architectural Decisions

| Decision | Rationale |
| ---------- | ----------- |
| Global Exception Handler | Konsisten |
| Business Error Code | Mudah diintegrasikan |
| Correlation ID | Traceability |
| Structured Response | Machine Readable |
| No Stacktrace | Security |

---

# 37. Alternatives Considered

| Alternative | Decision | Reason |
| ------------ | ---------- | -------- |
| Return Stacktrace | Tidak dipilih | Security Risk |
| Exception per Controller | Tidak dipilih | Sulit dipelihara |
| Plain String Error | Tidak dipilih | Tidak konsisten |
| Vendor-specific Error Format | Tidak dipilih | Mengurangi interoperabilitas |
| Silent Error Handling | Tidak dipilih | Sulit diobservasi |

---

# 38. Technical Risks

| Risk | Mitigation |
| ------ | ------------ |
| Error Code tidak konsisten | Centralized Error Catalog |
| Sensitive Data bocor | Response & Log Sanitization |
| Duplicate Error Mapping | Global Exception Handler |
| Retry berulang | Retry hanya untuk Infrastructure Error |
| Sulit melakukan root cause analysis | Correlation ID + Trace ID + Structured Logging |

---

# 39. Recommendations

1. Gunakan **RFC 7807** sebagai standar response error.
2. Pisahkan **Business Error Code** dari **HTTP Status Code**.
3. Gunakan **enum ErrorCode** agar seluruh kode error konsisten.
4. Semua exception harus menghasilkan log terstruktur dengan Correlation ID dan Trace ID.
5. Jangan pernah mengekspos stacktrace atau detail exception internal ke consumer.

---

# 40. Requires Functional Clarification

| Item | Status |
| ------ | -------- |
| Standard Error Code Organization | Requires Functional Clarification |
| Localization (Bahasa Indonesia / English) | Requires Functional Clarification |
| Error Message Customization per Consumer | Requires Functional Clarification |
| Rate Limiting Error Response | Requires Functional Clarification |
| Retry-After Header Policy | Requires Functional Clarification |

---

# 41. Traceability

| BRD | FSD | Component | API | Test Case |
| ----- | ----- | ----------- | ----- | ----------- |
| Validation | FSD-09 | Validation Layer | Semua Write API | TC-ERR-001 |
| Business Rule | FSD-09 | Domain | Publish Product | TC-ERR-002 |
| Audit | FSD-05 | Exception Handler | Semua Write API | TC-ERR-003 |
| Query Product | FSD-04 | Resource Handler | GET /products/{id} | TC-ERR-004 |
| Optimistic Lock | FSD-05 | Repository | PUT /products/{id} | TC-ERR-005 |

---

# 42. Next Document

**TSD_11_LOGGING.md**

Dokumen berikut akan membahas:

- Structured Logging
- JSON Log Format
- Correlation ID
- Trace ID
- Logback Configuration
- Log Levels
- Audit Logging
- OpenTelemetry Integration
- Sensitive Data Masking
- Logging Best Practices
- Java Implementation
- Spring Boot 4 Logging Configuration
- Observability Integration
