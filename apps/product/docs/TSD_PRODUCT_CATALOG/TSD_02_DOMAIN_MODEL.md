# TSD_02_DOMAIN_MODEL.md

> **Technical Specification Document (TSD)**  
> **Module:** Domain Model  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft

---

# 1. Purpose

Dokumen ini mendefinisikan **Domain Model** Product Catalog Service berdasarkan prinsip **Domain Driven Design (DDD)**.

Tujuan utama Domain Model adalah memastikan bahwa seluruh Business Rule berada pada Domain Layer sehingga tidak bergantung pada framework, database, maupun teknologi tertentu.

Dokumen ini menjadi referensi utama bagi:

- Backend Engineer
- Solution Architect
- QA Engineer
- Technical Lead

---

# 2. Design Principles

Domain Model mengikuti prinsip berikut.

- Rich Domain Model
- Behavior over Data
- Persistence Ignorance
- Aggregate Consistency
- Explicit Business Rule
- Immutable Value Object
- No Framework Dependency

---

# 3. Ubiquitous Language

| Business Term | Domain Object |
| -------------- | --------------- |
| Insurance Company | Company Aggregate |
| Product | Product Aggregate |
| Coverage | Coverage Entity |
| Benefit | Benefit Entity |
| Exclusion | Exclusion Entity |
| Eligibility | Eligibility Entity |
| Premium Configuration | PremiumConfiguration Entity |
| Product Document | ProductDocument Entity |
| Product Version | ProductVersion Entity |
| Audit Trail | AuditHistory Aggregate |

---

# 4. Aggregate Overview

Product Catalog terdiri dari tiga Aggregate utama.

```text
InsuranceCompany

Product

ProductVersion
```

Audit dikelola sebagai aggregate terpisah.

> **Keputusan (Resolved):** ProductVersion merupakan **Aggregate terpisah** yang merepresentasikan immutable snapshot, bukan sekadar tabel snapshot. Lihat Section 27.

---

# 5. Aggregate Relationship

```mermaid
classDiagram

class InsuranceCompany

class Product

class ProductVersion

class AuditHistory

InsuranceCompany "1" --> "*" Product

Product "1" --> "*" ProductVersion

Product ..> AuditHistory
```

---

# 6. Aggregate Boundary

## InsuranceCompany Aggregate

Aggregate Root

```
InsuranceCompany
```

Entity

Tidak ada.

Value Object

- CompanyId
- CompanyCode
- CompanyName

---

## Product Aggregate

Aggregate Root

```
Product
```

Owned Entity

- Coverage
- Benefit
- Exclusion
- Eligibility
- PremiumConfiguration
- ProductDocument

ProductVersion bukan child entity.

ProductVersion merupakan aggregate terpisah.

---

## ProductVersion Aggregate

Aggregate Root

```
ProductVersion
```

Owned Entity

- Coverage
- Benefit
- Exclusion
- Eligibility
- PremiumConfiguration
- ProductDocument

Immutable.

Setelah dibuat:

- tidak pernah diupdate
- tidak pernah dihapus
- hanya dapat dibaca

Quote Service membaca ProductVersion, bukan Product Draft.

---

## Audit Aggregate

Aggregate Root

```
AuditHistory
```

Append Only.

Tidak dapat diubah.

---

# 7. Aggregate Responsibilities

## InsuranceCompany

Bertanggung jawab terhadap

- Company Status
- Company Code
- Company Name

Tidak mengetahui Product Configuration.

---

## Product

Bertanggung jawab terhadap

- Product Status
- Product Metadata
- Configuration
- Publish Validation
- Version Creation

---

## AuditHistory

Bertanggung jawab terhadap

- Audit Log
- Audit Metadata
- Change History

---

# 8. Aggregate Lifecycle

## Company

```text
Create

↓

Active

↓

Inactive
```

---

## Product

```text
Draft

↓

Published

↓

Archived
```

---

## Product Version

```text
V1

↓

V2

↓

V3

↓

V4
```

Published Version bersifat immutable.

---

# 9. Aggregate Diagram

```mermaid
classDiagram

class Product{

+ProductId

+ProductCode

+ProductName

+ProductStatus

+Version

}

class Coverage

class Benefit

class Exclusion

class Eligibility

class PremiumConfiguration

class ProductDocument

Product *-- Coverage

Product *-- Benefit

Product *-- Exclusion

Product *-- Eligibility

Product *-- PremiumConfiguration

Product *-- ProductDocument
```

---

# 10. Entity Design

## Product

Attributes

- ProductId
- ProductCode
- ProductName
- CompanyId
- Status
- CurrentVersion

Business Behavior

- update()
- publish()
- archive()
- createVersion()

---

## Coverage

Attributes

- CoverageId
- Code
- Name

Behavior

Tidak memiliki business rule mandiri.

---

## Benefit

Attributes

- BenefitId
- Code
- Description

---

## Exclusion

Attributes

- ExclusionId
- Description

---

## Eligibility

Attributes

- EligibilityConfiguration

---

## PremiumConfiguration

Attributes

- PremiumCode

---

## ProductDocument

Attributes

- DocumentType
- FileName
- URI

---

# 11. Value Objects

Seluruh Value Object bersifat immutable.

## CompanyId

```java
public record CompanyId(UUID value) {}
```

---

## ProductId

```java
public record ProductId(UUID value) {}
```

---

## ProductCode

```java
public record ProductCode(String value) {}
```

---

## CompanyCode

```java
public record CompanyCode(String value) {}
```

---

## ProductVersionNumber

```java
public record ProductVersionNumber(Integer value) {}
```

---

## AuditId

```java
public record AuditId(UUID value) {}
```

---

# 12. Domain Services

Domain Service digunakan apabila Business Rule melibatkan lebih dari satu Aggregate.

Pada Product Catalog saat ini hanya terdapat:

```text
ProductVersionService
```

Responsibility

- membuat snapshot
- increment version
- immutable version

---

# 13. Domain Events

Product Catalog mendefinisikan Domain Event internal berikut.

```text
CompanyCreated

CompanyUpdated

CompanyActivated

CompanyDeactivated

ProductCreated

ProductUpdated

ProductPublished

ProductArchived

ProductVersionCreated

ConfigurationUpdated
```

Catatan:

BRD tidak mendefinisikan publishing event ke Kafka.

Event di atas hanya Domain Event.

---

# 14. Business Invariants

Aggregate Product harus selalu memenuhi aturan berikut.

Invariant 1

```
Product selalu memiliki Company.
```

---

Invariant 2

```
Product Code unik
dalam satu Company.
```

---

Invariant 3

```
Published Product

↓

Immutable
```

---

Invariant 4

```
Archive Product

↓

Tidak dapat diubah.
```

---

Invariant 5

```
Coverage minimal satu
sebelum Publish.
```

---

Invariant 6

```
Benefit minimal satu
sebelum Publish.
```

---

Invariant 7

```
Eligibility wajib tersedia.
```

---

Invariant 8

```
Premium Configuration wajib tersedia.
```

---

# 15. Aggregate Behavior

```mermaid
stateDiagram-v2

[*] --> Draft

Draft --> Published

Published --> Archived

Archived --> [*]
```

---

# 16. Domain Validation

Business Rule hanya boleh berada pada Aggregate.

Contoh

```java
public void publish() {

    validateReadyForPublish();

    this.status = ProductStatus.PUBLISHED;

}
```

Tidak diperbolehkan:

```
Controller

↓

Business Rule
```

---

# 17. Aggregate Root Example

```java
public class Product {

    private ProductId id;

    private ProductCode productCode;

    private ProductStatus status;

    private ProductVersionNumber version;

    public void publish() {

        validateReadyForPublish();

        this.status = ProductStatus.PUBLISHED;

    }

}
```

---

# 18. Factory Pattern

Factory digunakan untuk memastikan Aggregate selalu valid saat dibuat.

```java
public final class ProductFactory {

    public Product create(
            CompanyId companyId,
            ProductCode code,
            String productName
    ) {

        return Product.create(

                ProductId.generate(),

                companyId,

                code,

                productName

        );

    }

}
```

---

# 19. Repository Interface

Repository berada pada Domain.

```java
public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(ProductId id);

    Optional<Product> findByProductCode(ProductCode code);

}
```

Implementasi berada pada Infrastructure.

---

# 20. Domain Package Structure

```text
domain

├── company
│
├── product
│   ├── aggregate
│   ├── entity
│   ├── event
│   ├── repository
│   ├── service
│   ├── valueobject
│   └── factory
│
├── version
│
├── audit
│
└── shared
```

---

# 21. Domain Dependency Rules

Diizinkan

```text
Aggregate

↓

Entity

↓

Value Object
```

---

Tidak diizinkan

```text
Aggregate

↓

Spring

↓

JPA

↓

Redis

↓

REST
```

---

# 22. Architectural Decisions

| Decision | Rationale |
| ---------- | ----------- |
| Rich Domain Model | Business Rule berada di Domain |
| Aggregate Root | Menjaga consistency boundary |
| Immutable Value Object | Thread-safe & mudah diuji |
| Factory Pattern | Valid object creation |
| Repository Pattern | Persistence abstraction |
| Domain Event | Memisahkan perubahan state dari reaksi internal |

---

# 23. Alternatives Considered

| Alternative | Decision | Reason |
| ------------- | ---------- | -------- |
| Anemic Domain Model | Tidak digunakan | Business Rule tersebar pada Service |
| Transaction Script | Tidak digunakan | Sulit dipelihara untuk domain yang berkembang |
| Generic CRUD Entity | Tidak digunakan | Tidak mencerminkan model bisnis |
| Active Record | Tidak digunakan | Mengikat Domain dengan persistence |

---

# 24. Trade-offs

| Decision | Benefit | Trade-off |
| ---------- | --------- | ----------- |
| Rich Domain | Business Rule terpusat | Lebih banyak class |
| Aggregate Boundary | Konsistensi tinggi | Perlu desain transaksi yang jelas |
| Immutable Value Object | Aman dan mudah diuji | Membutuhkan pembuatan objek baru saat berubah |
| Factory Pattern | Inisialisasi valid | Tambahan lapisan abstraksi |

---

# 25. Technical Risks

| Risk | Mitigation |
| ------ | ------------ |
| Aggregate terlalu besar | Pisahkan berdasarkan consistency boundary |
| Business Rule bocor ke Application | Code Review + ArchUnit |
| Circular Dependency | Enforce package dependency |
| Entity menjadi DTO | Pisahkan Domain dan API Model |
| Repository langsung dipanggil Controller | Gunakan Application Service sebagai satu-satunya orchestrator |

---

# 26. Recommendations

1. Gunakan **sealed interface** untuk Domain Event pada Java 25.
2. Hindari penggunaan Lombok pada Domain Model jika memungkinkan; prioritaskan konstruktor eksplisit dan `record` untuk Value Object.
3. Seluruh perubahan state harus melalui method Aggregate (`publish()`, `archive()`, `update()`), bukan melalui setter.
4. Jangan mengekspos Entity Domain langsung melalui REST API; gunakan DTO terpisah di layer Application/Interface.
5. Gunakan **ArchUnit** untuk memastikan Domain Layer tidak memiliki dependency terhadap Spring Framework maupun Infrastructure.

---

# 27. Functional Clarification — Resolved

Item berikut sebelumnya berstatus *Requires Functional Clarification* dan telah diputuskan berdasarkan DDD, enterprise insurance, Product Catalog sebagai Single Source of Truth, serta BRD/FSD/TSD.

| Item | Keputusan | Status |
| ------ | ---------- | -------- |
| Apakah ProductVersion merupakan Aggregate terpisah atau snapshot persistence? | **ProductVersion adalah Aggregate terpisah yang merepresentasikan immutable snapshot.** Mempunyai identity (`ProductVersionId`) dan business meaning. Setelah publish: tidak pernah diupdate, tidak pernah dihapus, hanya dapat dibaca. Quote Service membaca ProductVersion, bukan Product Draft. | **Resolved** |
| Mekanisme penyimpanan ProductDocument (Object Storage atau metadata saja) | **Product Catalog hanya menyimpan metadata dokumen.** File fisik disimpan di Object Storage (MinIO, S3, Azure Blob, GCS). Database menyimpan `document_name`, `document_type`, `mime_type`, `storage_key`, `checksum`, `file_size`, `uploaded_at` — bukan `bytea`/`blob`. | **Resolved** |
| Retention AuditHistory | **Audit bersifat append-only.** Tidak boleh dihapus maupun diupdate oleh aplikasi. Retention mengikuti kebijakan organisasi, default enterprise insurance: **minimum 7 tahun** atau sesuai regulasi OJK dan kebijakan perusahaan. | **Resolved** |
| Domain Event dipublikasikan keluar service atau hanya internal | **Domain Event selalu dibuat secara internal** (mis. `ProductPublished`, `ProductArchived`, `CoverageReplaced`). **Integration Event dipublikasikan keluar service hanya jika dibutuhkan** oleh consumer atau BRD diperluas, melalui REST atau Kafka. | **Resolved** |
| Apakah Company dapat memiliki Product tanpa status ACTIVE | **Tidak.** Product hanya boleh dibuat apabila Company berstatus **ACTIVE**. Company INACTIVE menolak pembuatan Product baru, namun ProductVersion yang sudah Published tetap valid (tidak otomatis hilang) karena Quote, Proposal, dan Historical Report masih membutuhkannya. | **Resolved** |

---

# 28. Compliance & Data Governance

## 28.1 Regulatory Compliance

Domain Model dirancang untuk mendukung compliance dengan:

* **UU PDP No. 27/2022** - Perlindungan Data Pribadi
  * Audit trail immutable untuk seluruh perubahan
  * Data retention policy (7-10 years)
  * Data minimization

* **POJK No. 13/2017** - Penggunaan TI
  * Immutable audit trail
  * Business continuity
  * IT risk management

* **ISO/IEC 27001:2022** - ISMS
  * Access control melalui aggregate boundaries
  * Data classification
  * Cryptographic controls

Lihat [Enterprise Standards & Compliance Framework](../../../docs/16. ENTERPRISE_STANDARDS.md) untuk detail lengkap.

---

## 28.2 Data Classification in Domain

| Aggregate | Data Classification | Protection |
|-----------|---------------------|------------|
| InsuranceCompany | Internal | Access control, audit |
| Product | Internal | Access control, audit, versioning |
| ProductVersion | Confidential | Encryption, immutable storage |
| AuditHistory | Restricted | Append-only, encryption, 7-year retention |
| EligibilityConfiguration | Confidential | Encryption, RBAC |
| PremiumConfiguration | Confidential | Encryption, RBAC |

---

## 28.3 Audit Trail Design

Setiap Aggregate yang menghasilkan perubahan bisnis harus menghasilkan Audit Event.

**Audit Principles:**
* Append-only (tidak boleh diupdate atau dihapus)
* Immutable
* Retained untuk 7 tahun (minimal)
* Berisi: who, what, when, where, why, how

**Audit Event Structure:**
```java
public record AuditEvent(
    UUID eventId,
    String aggregateType,
    UUID aggregateId,
    String action,
    Object beforeState,
    Object afterState,
    String performedBy,
    Instant performedAt,
    String reason
) {}
```

---

## 28.4 Data Retention in Domain

Retention policy diimplementasikan melalui:

1. **Immutable Aggregate:** ProductVersion tidak boleh dihapus
2. **Soft Delete:** Semua entity menggunakan soft delete
3. **Audit Trail:** Append-only dengan retention 7-10 tahun
4. **Archive Strategy:** Platform responsible untuk archival

**Retention Schedule:**
* ProductVersion: Permanent (archive setelah 10 tahun)
* AuditHistory: 7 tahun (UU PDP, OJK)
* Configuration History: 10 tahun (OJK)

---

## 28.5 Privacy by Design

Domain Model mengikuti privacy by design principles:

* **Data Minimization:** Hanya simpan data yang diperlukan
* **Purpose Limitation:** Data hanya digunakan untuk tujuan yang didefinisikan
* **Accuracy:** Validasi data pada aggregate
* **Storage Limitation:** Retention policy yang jelas
* **Integrity & Confidentiality:** Encryption, access control
* **Accountability:** Audit trail untuk seluruh perubahan

Lihat [Compliance Reference Guide](COMPLIANCE_REFERENCE.md) untuk implementasi detail.

---

# 27a. Business Rules Tambahan

Berdasarkan keputusan di atas, ditambahkan Business Rule berikut ke BRD/FSD/TSD.

| Rule ID | Business Rule |
| ------- | ------------- |
| BR-021 | Product hanya dapat dibuat apabila Insurance Company berstatus **ACTIVE**. |
| BR-022 | Perubahan Company menjadi **INACTIVE** tidak mengubah ProductVersion yang telah **Published**. |
| BR-023 | ProductDocument hanya menyimpan **metadata**. Binary document disimpan di **Object Storage**. |
| BR-024 | Audit History bersifat **append-only**. Tidak boleh diubah. Tidak boleh dihapus oleh aplikasi. |
| BR-025 | Publish Product harus membuat **Aggregate ProductVersion** yang **immutable**. |

---

# 28. Next Document

Dokumen selanjutnya:

**TSD_03_DATABASE.md**

Dokumen ini akan mencakup:

- Logical Data Model
- Physical Data Model
- ERD
- PostgreSQL Schema
- Flyway Migration Strategy
- Index Strategy
- Foreign Key Strategy
- Optimistic Locking
- Soft Delete
- Audit Columns
- SQL DDL
- Database Performance Design
- Partitioning & Index Recommendations
