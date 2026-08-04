# TSD_06_WORKFLOW.md

> **Technical Specification Document (TSD)**  
> **Module:** Workflow Design  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft

---

# 1. Purpose

Dokumen ini mendefinisikan workflow teknis Product Catalog Service.

Workflow menjelaskan:

- Request Flow
- Service Interaction
- Transaction Boundary
- Domain Flow
- Error Flow
- Versioning Flow
- Audit Flow

Workflow hanya mencakup Product Catalog.

Dokumen ini **tidak membahas**:

- Quote
- Premium Calculation
- Eligibility Engine
- Checkout
- Payment
- Policy Issuance

karena berada di luar ruang lingkup Product Catalog.

---

# 2. Workflow Overview

Product Catalog memiliki workflow utama berikut.

| Workflow | Description |
| ----------- | ------------- |
| Create Company | Membuat Insurance Company |
| Update Company | Mengubah Company |
| Activate Company | Mengaktifkan Company |
| Deactivate Company | Menonaktifkan Company |
| Create Product | Membuat Product Draft |
| Update Product | Mengubah Product Draft |
| Update Configuration | Mengubah Coverage, Benefit, Eligibility, Premium Configuration, Product Document |
| Publish Product | Publish Product menjadi Published Version |
| Archive Product | Mengarsipkan Product |
| Query Product | Membaca Product |
| Query Version | Membaca Product Version |
| Query Audit | Membaca Audit History |

---

# 3. Workflow Architecture

```mermaid
flowchart LR

Client

API

Application

Domain

Repository

Database

Client --> API

API --> Application

Application --> Domain

Application --> Repository

Repository --> Database
```

---

# 4. General Request Flow

```mermaid
sequenceDiagram

actor User

participant API

participant Application

participant Domain

participant Repository

database PostgreSQL

User->>API: HTTP Request

API->>API: Request Validation

API->>Application: Command

Application->>Domain: Execute Business Rule

Domain-->>Application: Aggregate

Application->>Repository: Save

Repository->>PostgreSQL: SQL

PostgreSQL-->>Repository

Repository-->>Application

Application-->>API

API-->>User: HTTP Response
```

---

# 5. Transaction Boundary

Seluruh perubahan data dilakukan dalam **satu transaction**.

```text
BEGIN

↓

Business Validation

↓

Domain Execution

↓

Repository Save

↓

Audit Save

↓

COMMIT
```

Jika salah satu langkah gagal.

```text
ROLLBACK
```

---

# 6. Create Company Workflow

## Description

Workflow membuat Insurance Company baru.

---

## Sequence Diagram

```mermaid
sequenceDiagram

actor Admin

participant CompanyController

participant CompanyApplicationService

participant Company

participant CompanyRepository

participant AuditRepository

database PostgreSQL

Admin->>CompanyController: POST /companies

CompanyController->>CompanyApplicationService: CreateCompanyCommand

CompanyApplicationService->>Company: create()

Company->>Company: validate()

CompanyApplicationService->>CompanyRepository: save()

CompanyApplicationService->>AuditRepository: insert()

CompanyApplicationService-->>CompanyController

CompanyController-->>Admin: 201 Created
```

---

# 7. Create Product Workflow

## Description

Product baru selalu dibuat dengan status:

```
DRAFT
```

---

## Sequence Diagram

```mermaid
sequenceDiagram

actor Admin

participant Controller

participant ProductApplicationService

participant Product

participant Repository

database PostgreSQL

Admin->>Controller: POST /products

Controller->>ProductApplicationService

ProductApplicationService->>Product:create()

Product->>Product:validate()

ProductApplicationService->>Repository:save()

Repository->>PostgreSQL

PostgreSQL-->>Repository

Repository-->>ProductApplicationService

ProductApplicationService-->>Controller

Controller-->>Admin
```

---

# 8. Update Product Workflow

Hanya Product dengan status:

```
DRAFT
```

yang boleh diubah.

---

## Workflow

```mermaid
flowchart TD

LoadProduct

ValidateStatus

UpdateAggregate

Save

Audit

End

LoadProduct --> ValidateStatus

ValidateStatus --> UpdateAggregate

UpdateAggregate --> Save

Save --> Audit

Audit --> End
```

---

# 9. Update Product Configuration Workflow

Workflow ini berlaku untuk:

- Coverage
- Benefit
- Exclusion
- Eligibility
- Premium Configuration
- Product Document

---

## Flow

```text
Load Product

↓

Validate Draft

↓

Update Configuration

↓

Save

↓

Audit
```

---

# 10. Publish Product Workflow

Publish merupakan workflow paling penting.

---

## Preconditions

Product harus memiliki:

- Coverage
- Benefit
- Eligibility Configuration
- Premium Configuration

Jika salah satu belum ada maka Publish ditolak.

---

## Sequence Diagram

```mermaid
sequenceDiagram

actor Admin

participant Controller

participant ProductApplicationService

participant Product

participant VersionRepository

participant ProductRepository

participant AuditRepository

database PostgreSQL

Admin->>Controller: Publish Product

Controller->>ProductApplicationService

ProductApplicationService->>Product:publish()

Product->>Product:validateReadyForPublish()

Product->>VersionRepository:createSnapshot()

Product->>Product:setPublished()

ProductApplicationService->>ProductRepository:save()

ProductApplicationService->>AuditRepository:insert()

ProductApplicationService-->>Controller

Controller-->>Admin
```

---

# 11. Publish State Transition

```mermaid
stateDiagram-v2

Draft --> Published
```

Transition lain ditolak.

---

# 12. Archive Product Workflow

Workflow Archive tidak menghapus Product.

---

## Flow

```text
Load Product

↓

Validate Published

↓

Archive

↓

Save

↓

Audit
```

---

## State

```mermaid
stateDiagram-v2

Published --> Archived
```

---

# 13. Product Version Workflow

Setiap Publish menghasilkan snapshot baru.

---

## Workflow

```mermaid
flowchart TD

Draft

Validate

Snapshot

IncrementVersion

Publish

Audit

Draft --> Validate

Validate --> Snapshot

Snapshot --> IncrementVersion

IncrementVersion --> Publish

Publish --> Audit
```

---

# 14. Version Snapshot

Snapshot menyimpan representasi Product saat dipublish.

```text
Product

↓

JSON Snapshot

↓

Product Version
```

Snapshot bersifat immutable.

---

# 15. Query Product Workflow

Query Product tidak mengubah data.

---

## Flow

```text
Receive Request

↓

Repository Query

↓

Mapping DTO

↓

Return Response
```

---

## Sequence

```mermaid
sequenceDiagram

actor User

participant Controller

participant QueryService

participant Repository

database PostgreSQL

User->>Controller

Controller->>QueryService

QueryService->>Repository

Repository->>PostgreSQL

PostgreSQL-->>Repository

Repository-->>QueryService

QueryService-->>Controller

Controller-->>User
```

---

# 16. Query Version Workflow

```text
Receive Request

↓

Load Version

↓

Return Snapshot
```

---

# 17. Query Audit Workflow

Audit bersifat Read Only.

```text
Load Audit

↓

Return Audit History
```

---

# 18. Error Flow

```mermaid
flowchart TD

Request

↓

Validate

↓

Business Rule

↓

Persistence

↓

Success

Request --> Validate

Validate --> BusinessRule

BusinessRule --> Persistence

Persistence --> Success

Validate --> Error

BusinessRule --> Error

Persistence --> Error
```

---

# 19. Exception Flow

| Stage | Exception |
| --------- | ----------- |
| Validation | ValidationException |
| Domain | BusinessException |
| Repository | DataAccessException |
| Database | ConstraintViolation |
| Optimistic Lock | OptimisticLockException |

---

# 20. Retry Strategy

Product Catalog tidak menggunakan retry pada Business Command.

Retry hanya diperbolehkan pada:

- database connection transient failure
- infrastructure timeout

Retry **tidak boleh** mengulangi Business Rule yang sudah berhasil dieksekusi sebagian.

---

# 21. Compensation Strategy

Product Catalog menggunakan database transaction.

Tidak menggunakan Saga maupun Compensation.

Jika transaction gagal.

```
ROLLBACK
```

---

# 22. Concurrency Workflow

Menggunakan Optimistic Lock.

```text
Load Product

↓

Version = 5

↓

Update

↓

Save

↓

Version = 6
```

Jika terdapat update bersamaan.

```
409 Conflict
```

---

# 23. Audit Workflow

Audit dibuat setelah perubahan Aggregate berhasil.

```mermaid
flowchart TD

Update Aggregate

↓

Save Aggregate

↓

Insert Audit

↓

Commit
```

Audit menjadi bagian dari transaction.

---

# 24. Logging Workflow

Setiap request menghasilkan:

- Correlation ID
- Trace ID
- Request Log
- Response Log

Contoh:

```text
HTTP Request

↓

Application Log

↓

Domain Log

↓

Repository Log

↓

HTTP Response
```

---

# 25. Workflow Dependency

```mermaid
graph TD

Company --> Product

Product --> ProductConfiguration

ProductConfiguration --> Publish

Publish --> Version

Publish --> Audit

Archive --> Audit
```

---

# 26. Architectural Decisions

| Decision | Rationale |
| ----------- | ----------- |
| Application Service mengelola workflow | Memisahkan orchestration dari Domain |
| Domain Aggregate mengelola Business Rule | Konsistensi Domain |
| Repository hanya Persistence | Separation of Concern |
| Audit bagian dari transaction | Menjamin konsistensi |
| Snapshot saat Publish | Immutable Product Version |

---

# 27. Alternatives Considered

| Alternative | Decision | Reason |
| ------------ | ---------- | -------- |
| Saga Pattern | Tidak digunakan | Tidak ada transaksi lintas service |
| Event Sourcing | Tidak digunakan | BRD tidak mensyaratkan |
| CQRS Read Database | Belum digunakan | Volume data belum memerlukan |
| Kafka Workflow | Tidak digunakan | Di luar scope Product Catalog |

---

# 28. Technical Risks

| Risk | Mitigation |
| ------ | ------------ |
| Publish bersamaan | Optimistic Locking |
| Audit gagal | Rollback transaction |
| Duplicate Publish | Domain Validation |
| Update Published Product | Business Rule Validation |
| Long Transaction | Batasi transaction hanya pada Aggregate Product |

---

# 29. Recommendations

1. Gunakan `@Transactional` hanya pada **Application Service**, bukan pada Controller atau Repository.
2. Hindari memanggil Repository langsung dari Controller.
3. Seluruh workflow harus menghasilkan Audit Trail untuk operasi yang mengubah data.
4. Pisahkan Command dan Query Service apabila kebutuhan query berkembang (CQRS ringan).
5. Tambahkan OpenTelemetry Span pada setiap tahapan workflow untuk observability.

---

# 30. Workflow Decisions

Poin-poin berikut merupakan **Workflow Decisions** yang ditetapkan oleh arsitek. Hanya Publish Approval yang berpotensi bergantung pada keputusan bisnis; empat poin lainnya adalah keputusan teknis/workflow.

## 30.1 Publish Approval

### Keputusan

BRD saat ini **tidak mendefinisikan approval workflow**. Oleh karena itu implementasi Product Catalog **v1 menggunakan direct publish** — Publish dilakukan langsung oleh pengguna yang memiliki hak akses (Product Administrator atau Business User sesuai RBAC).

```text
Draft
   │
   ▼
Ready
   │
Publish
   │
   ▼
Published
```

### Rationale

- BRD tidak mendefinisikan proses maker-checker.
- Product Catalog hanya mengelola metadata produk.
- Approval merupakan business workflow yang seharusnya berada di Workflow Engine atau Business Process Service apabila dibutuhkan di masa depan.

### Future Extension

Apabila di masa depan organisasi membutuhkan maker-checker, workflow akan diimplementasikan sebagai **business process di Workflow Engine, bukan di dalam Product Aggregate**:

```text
Draft

↓

Ready

↓

Pending Approval

↓

Approved

↓

Published
```

Tanpa mengubah Aggregate Product.

**Status:** ✅ Resolved

---

## 30.2 Unarchive

### Keputusan

Archive bersifat final.

Tidak tersedia workflow Unarchive.

Untuk mengaktifkan kembali produk, pengguna harus membuat Draft baru dari Product yang bersangkutan dan melakukan Publish sehingga menghasilkan Product Version baru.

```text
Published

↓

Archived

↓

(No Transition)
```

### Rationale

- Menjaga lifecycle tetap sederhana.
- Menghindari kebingungan terhadap versi aktif.
- Konsisten dengan prinsip immutable version.

**Status:** ✅ Resolved

---

## 30.3 Locking Strategy

### Keputusan

Product Catalog hanya menggunakan **Optimistic Locking**.

Tidak menggunakan:

- Pessimistic Lock
- Distributed Lock
- Database Advisory Lock

Concurrency dikendalikan menggunakan kolom:

```text
version
```

Jika terjadi konflik update:

```http
409 Conflict
```

dengan error:

```json
{
  "code": "OPTIMISTIC_LOCK_CONFLICT",
  "message": "Product has been modified by another user."
}
```

### Rationale

- Beban write relatif rendah.
- Mendukung horizontal scalability.
- Menghindari lock database yang panjang.

**Status:** ✅ Resolved

---

## 30.4 Workflow SLA

### Keputusan

SLA teknis untuk Product Catalog.

| Workflow | Target |
|-----------|--------|
| Create Product | < 500 ms |
| Update Product | < 500 ms |
| Publish Product | < 2 detik |
| Archive Product | < 500 ms |
| Search Product | < 300 ms |
| Get Product Detail | < 300 ms |
| Get Version History | < 500 ms |

Target berlaku pada kondisi normal dan tidak termasuk latency jaringan eksternal.

### Rationale

- Mendukung pengalaman pengguna Back Office.
- Menjadi acuan Performance Test dan SRE.

**Status:** ✅ Resolved

---

## 30.5 Bulk Publish

### Keputusan

Bulk Publish **tidak didukung** pada versi pertama Product Catalog.

Publish dilakukan per Product.

```text
POST /products/{id}/publish
```

Tidak tersedia:

```text
POST /products/publish
```

atau

```text
POST /products/bulk-publish
```

### Rationale

- Mengurangi kompleksitas transaksi.
- Menghindari partial success.
- Mempermudah audit.
- Mempermudah rollback operasional.

Apabila kebutuhan bulk muncul di masa depan, implementasi direkomendasikan menggunakan asynchronous job.

**Status:** ✅ Resolved

---

# 31. Workflow Governance Summary

| Area | Decision |
|------|----------|
| Publish Approval | Tidak ada approval workflow di v1 (direct publish) |
| Publish Trigger | Manual oleh user yang memiliki hak akses |
| Future Approval | Business process di Workflow Engine, bukan di Product Aggregate |
| Unarchive | Tidak didukung |
| Locking | Optimistic Locking |
| Pessimistic Lock | Tidak digunakan |
| Distributed Lock | Tidak digunakan |
| Workflow SLA | Ditetapkan per operasi |
| Bulk Publish | Tidak didukung pada versi pertama |

---

# 32. Next Document

**TSD_07_VERSIONING.md**

Dokumen berikut akan membahas:

- Product Version Strategy
- Immutable Version Design
- Snapshot Strategy
- Version Numbering
- Historical Retrieval
- Backward Compatibility
- Database Implementation
- API Version Access
- Sequence Diagram
- Java Implementation
- SQL Design

```
