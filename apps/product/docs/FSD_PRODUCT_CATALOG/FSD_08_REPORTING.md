# FSD_08_REPORTING.md

> **Functional Specification Document (FSD)**  
> **Module:** Reporting  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft  
> **Reference:** BRD-PC-001 (Business Objectives, Success Criteria, Business Data Requirements, Reporting Consumer, Audit Trail, Versioning) :contentReference[oaicite:0]{index=0} :contentReference[oaicite:1]{index=1} :contentReference[oaicite:2]{index=2} :contentReference[oaicite:3]{index=3}

---

# 1. Purpose

Dokumen ini mendefinisikan spesifikasi fungsional **Reporting Module** pada Product Catalog Service.

Reporting bertujuan menyediakan data operasional dan historis mengenai Product Catalog untuk kebutuhan monitoring, analisis bisnis, audit, dan pengambilan keputusan.

Reporting **tidak** melakukan analisis bisnis, dashboard, maupun ETL. Product Catalog hanya menyediakan API untuk memperoleh data.

---

# 2. Objective

Reporting bertujuan menyediakan informasi mengenai:

- Insurance Company
- Product
- Product Status
- Product Version
- Product Configuration
- Audit History

kepada consumer Reporting.

---

# 3. Business Scope

## In Scope

- Product Report
- Company Report
- Version Report
- Product Status Report
- Product Configuration Report
- Audit Report

## Out of Scope

- Premium Report
- Quote Report
- Proposal Report
- Checkout Report
- Payment Report
- Policy Report
- Claim Report

---

# 4. Reporting Principles

Reporting mengikuti prinsip berikut:

- Read Only
- No Data Modification
- REST API
- JSON
- Historical Data Supported
- Version Aware
- Pagination Mandatory

---

# 5. Report Categories

```text
Reporting

├── Company Report
├── Product Report
├── Product Version Report
├── Product Status Report
├── Configuration Report
└── Audit Report
```

---

# 6. Company Report

## Description

Menampilkan daftar seluruh Insurance Company yang terdaftar pada Product Catalog.

---

## Data

- Company Code
- Company Name
- Status
- Created Date
- Updated Date

---

## API

```
GET /api/v1/reports/companies
```

---

# 7. Product Report

## Description

Menampilkan seluruh Product yang dimiliki seluruh Insurance Company.

---

## Data

- Product Code
- Product Name
- Company
- Category
- Status
- Current Version
- Effective Date

---

## API

```
GET /api/v1/reports/products
```

---

# 8. Product Status Report

## Description

Menampilkan distribusi Product berdasarkan status.

---

## Status

- Draft
- Published
- Archived

---

## API

```
GET /api/v1/reports/products/status
```

---

# 9. Product Version Report

## Description

Menampilkan histori seluruh Product Version.

---

## Data

- Product Code
- Version
- Status
- Published Date
- Created By

---

## API

```
GET /api/v1/reports/product-versions
```

---

# 10. Product Configuration Report

## Description

Menampilkan konfigurasi produk.

---

## Data

- Coverage
- Benefit
- Exclusion
- Eligibility
- Premium Configuration
- Product Document

---

## API

```
GET /api/v1/reports/product-configurations
```

---

# 11. Audit Report

## Description

Menampilkan histori perubahan Product.

---

## Data

- Action
- User
- Timestamp
- Version
- Entity
- Before
- After

---

## API

```
GET /api/v1/reports/audit
```

---

# 12. Reporting Consumer

| Consumer | Purpose |
| ------------ | ---------------------------- |
| Reporting Service | Dashboard |
| Product Administrator | Monitoring |
| Business User | Operational Analysis |
| Internal Audit | Audit Review |

---

# 13. Search Criteria

## Company Report

- Company Code
- Company Name
- Status

---

## Product Report

- Company
- Product Code
- Product Name
- Status
- Version

---

## Audit Report

- User
- Action
- Entity
- Date
- Version

---

# 14. Pagination

Seluruh endpoint Reporting wajib mendukung:

- page
- size

Contoh

```
GET /reports/products?page=0&size=20
```

---

# 15. Sorting

Reporting mendukung sorting.

Contoh

```
sort=productCode

sort=createdAt

sort=company

sort=status
```

---

# 16. Filtering

Filtering yang direkomendasikan.

## Company

- Status

---

## Product

- Company
- Status
- Version

---

## Audit

- User
- Action
- Date

---

# 17. Response Example

## Product Report

```json
{
  "content": [
    {
      "companyCode": "PRU",
      "productCode": "PA001",
      "productName": "Personal Accident Basic",
      "status": "PUBLISHED",
      "version": 3
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 120
}
```

---

## Audit Report

```json
{
  "content": [
    {
      "entity": "PRODUCT",
      "action": "PUBLISH",
      "version": 3,
      "createdBy": "admin",
      "createdAt": "2026-08-01T09:30:00Z"
    }
  ]
}
```

---

# 18. Sequence Diagram

## Product Report

```mermaid
sequenceDiagram
    actor Reporting
    participant ReportAPI[Report API]
    participant Repository
    participant DB[(PostgreSQL)]
    Reporting->>ReportAPI: GET Product Report
    ReportAPI->>Repository: Query Product
    Repository->>DB: SELECT
    DB-->>Repository: Result
    Repository-->>ReportAPI: Product Report
    ReportAPI-->>Reporting: JSON
```

---

## Audit Report

```mermaid
sequenceDiagram
    actor Auditor
    participant ReportAPI[Report API]
    participant AuditRepository[Audit Repository]
    participant DB[(PostgreSQL)]
    Auditor->>ReportAPI: GET Audit Report
    ReportAPI->>AuditRepository: Query Audit
    AuditRepository->>DB: SELECT
    DB-->>AuditRepository: Result
    AuditRepository-->>ReportAPI: Audit Report
    ReportAPI-->>Auditor: JSON
```

---

# 19. Performance Requirement

| Requirement | Value |
| ------------ | -------- |
| Response Time | < 500 ms |
| Pagination | Mandatory |
| Sorting | Supported |
| Filtering | Supported |
| Cache | Redis |
| Compression | GZIP |

Mengikuti Non Functional Requirement BRD. :contentReference[oaicite:4]{index=4}

---

# 20. Security

Authentication

- OAuth2
- JWT

Authorization

| Role | Permission |
| ------ | ------------ |
| Product Administrator | Full Reporting |
| Business User | Operational Report |
| Reporting Service | Full Read |
| Marketplace | Tidak Diizinkan |

---

# 21. Error Response

## Report Not Found

```json
{
  "code": "REPORT_NOT_FOUND",
  "message": "Report not found."
}
```

---

## Unauthorized

```json
{
  "code": "UNAUTHORIZED",
  "message": "Authentication required."
}
```

---

## Forbidden

```json
{
  "code": "FORBIDDEN",
  "message": "Access denied."
}
```

---

# 22. Acceptance Criteria

| ID | Scenario | Expected Result |
| ---- | ---------- | ---------------- |
| AC-01 | Company Report | Berhasil |
| AC-02 | Product Report | Berhasil |
| AC-03 | Product Version Report | Berhasil |
| AC-04 | Product Status Report | Berhasil |
| AC-05 | Audit Report | Berhasil |
| AC-06 | Pagination | Berjalan |
| AC-07 | Sorting | Berjalan |
| AC-08 | Filtering | Berjalan |
| AC-09 | JWT Invalid | HTTP 401 |
| AC-10 | Marketplace mengakses Report | HTTP 403 |

---

# 23. Requirement Traceability Matrix

| BRD | Functional Requirement |
| ------ | ------------------------ |
| BR-13 | Product Version Report |
| BR-14 | Audit Report |
| BR-15 | Product Metadata Report |
| NFR Audit | Audit Reporting |
| NFR Versioning | Version Reporting |

---

# 24. Reporting Decisions

Selama penyusunan FSD dilakukan beberapa keputusan desain untuk memastikan Product Catalog tetap berperan sebagai System of Record dan tidak mengambil tanggung jawab Reporting Service.

Prinsip utama:

> **Product Catalog adalah System of Record (Source of Truth), bukan Reporting Engine.**

Artinya Product Catalog menyediakan **data**, sedangkan proses **agregasi, dashboard, export, dan scheduled report** menjadi tanggung jawab Reporting Service.

| ID    | Decision                                                                                                                       | Status   |
| ----- | ------------------------------------------------------------------------------------------------------------------------------ | -------- |
| RD-01 | Product Catalog hanya menyediakan REST API, sedangkan proses **export (CSV, Excel, PDF)** dilakukan oleh Reporting Service      | Approved |
| RD-02 | Product Catalog tidak menetapkan batas maksimum periode pelaporan; pembatasan dilakukan melalui pagination, page size, timeout, atau kebijakan consumer | Approved |
| RD-03 | Seluruh **agregasi** data (misalnya jumlah Product per Company) merupakan tanggung jawab Reporting Service                      | Approved |
| RD-04 | Product Catalog tidak menyediakan **Scheduled Report**; Reporting Service bertanggung jawab atas penjadwalan, pembuatan, dan distribusi laporan | Approved |
| RD-05 | Product **Archived** tetap tersedia untuk kebutuhan audit dan histori, sedangkan Operational Report dapat memfilter status sesuai kebutuhan consumer | Approved |

## 24.1 Reporting Principle

```text
Product Catalog menyediakan data operasional melalui REST API sebagai System of Record.

Reporting Service bertanggung jawab atas:
- Agregasi data
- Dashboard
- Export (CSV, Excel, PDF)
- Scheduled Report
- Business Analytics
- Data Presentation

Product Catalog tidak menyimpan hasil agregasi maupun laporan yang telah dihasilkan.
```

Prinsip ini konsisten dengan seluruh BRD, FSD, dan TSD yang telah disusun, menjaga **separation of concerns**, serta mencegah Product Catalog berkembang menjadi layanan reporting yang berada di luar ruang lingkup bisnisnya.

---

# 25. Architecture Notes

## Reporting Responsibility

Product Catalog **bukan Reporting Engine**.

Tanggung jawab Product Catalog adalah menyediakan data melalui REST API.

Pemrosesan berikut dilakukan oleh Reporting Service atau Data Platform:

- Dashboard
- Business Intelligence
- ETL
- Data Warehouse
- Data Visualization
- Scheduled Report
- Export Report

Dengan pemisahan ini, Product Catalog tetap fokus sebagai **Operational System (OLTP)**, sedangkan analisis dan pelaporan kompleks berada pada **Reporting Platform (OLAP)**.

## Integration Flow

```text
Product Catalog

↓

REST API

↓

Reporting Service

↓

Dashboard / BI / Data Warehouse
```

Tidak ada database sharing antara Product Catalog dan Reporting Service.

Seluruh data diperoleh melalui API sehingga Product Catalog tetap menjadi **Single Source of Truth** untuk metadata produk sesuai Business Objective dan Success Criteria pada BRD. :contentReference[oaicite:5]{index=5} :contentReference[oaicite:6]{index=6}
