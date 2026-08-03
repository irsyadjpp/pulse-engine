# FSD_04_PRODUCT_QUERY.md

> **Functional Specification Document (FSD)**  
> **Module:** Product Query  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft  
> **Reference:** BRD-PC-001 (BR-11, BR-12, BR-15) :contentReference[oaicite:0]{index=0}

---

# 1. Purpose

Dokumen ini mendefinisikan spesifikasi fungsional **Product Query Module** yang bertanggung jawab menyediakan layanan pembacaan (read-only) terhadap metadata produk Personal Accident Insurance.

Modul ini merupakan entry point bagi seluruh consumer untuk memperoleh informasi produk tanpa melakukan perubahan data.

Seluruh akses dilakukan melalui REST API. Consumer **tidak diperbolehkan** mengakses database Product Catalog secara langsung.

---

# 2. Objective

Product Query bertujuan menyediakan layanan:

- Product Search
- Product Listing
- Product Detail
- Product Version History

kepada seluruh consumer Product Catalog.

---

# 3. Business Scope

## In Scope

- Search Product
- Product Listing
- Product Detail
- Product Version History

## Consumer

- Marketplace
- Quote Service
- Proposal Service
- Checkout Service
- Reporting

## Out of Scope

- Create Product
- Update Product
- Publish Product
- Archive Product
- Premium Calculation
- Eligibility Validation

---

# 4. Business Overview

Product Query merupakan **Read API** pada Product Catalog.

Semua consumer memperoleh metadata produk melalui API ini.

Product Query menjadi implementasi Business Requirement berikut:

- BR-11 Product Search
- BR-12 Product Detail
- BR-15 Product Catalog sebagai Single Source of Truth

:contentReference[oaicite:1]{index=1}

---

# 5. Actor

| Actor | Permission |
| -------- | ------------ |
| Marketplace | Read Published Product |
| Quote Service | Read Published Product |
| Proposal Service | Read Product Snapshot |
| Checkout Service | Read Product Reference |
| Reporting | Read All Product Metadata |
| Product Administrator | Read All Product |

---

# 6. Functional Requirement

---

## FR-04-01 Product Search

### Description

Consumer dapat mencari produk berdasarkan kriteria tertentu.

---

### Main Flow

1. Consumer mengirim request pencarian.
2. Sistem melakukan validasi parameter.
3. Sistem mengambil data dari Product Repository.
4. Sistem hanya mengembalikan data sesuai hak akses.
5. Sistem mengirim hasil pencarian.

---

### Input

Parameter yang dapat digunakan:

- Company
- Product Code
- Product Name
- Product Category
- Product Status
- Effective Date

**Catatan**

BRD hanya mendefinisikan adanya Product Search tanpa menentukan parameter pencarian.

Parameter di atas merupakan **Assumption** untuk mendukung kebutuhan operasional dan perlu dikonfirmasi oleh Business Owner.

---

### Output

Daftar produk.

---

## FR-04-02 Product Listing

### Description

Mengembalikan daftar produk menggunakan pagination.

---

### Main Flow

Consumer

↓

Request Product List

↓

Validation

↓

Repository

↓

Response

---

### Response

- Product ID
- Product Code
- Product Name
- Company
- Status
- Version

---

## FR-04-03 Product Detail

### Description

Mengembalikan seluruh metadata suatu produk.

---

### Data Returned

Product

Coverage

Benefit

Exclusion

Eligibility

Premium Configuration

Product Document

Version

Status

Company

---

### Validation

Jika Product tidak ditemukan maka sistem mengembalikan HTTP 404.

---

## FR-04-04 Version History

### Description

Mengembalikan seluruh histori versi produk.

---

### Response

- Version Number
- Status
- Effective Date
- Published Date
- Created By
- Created At

---

# 7. Business Rules

| Rule | Description |
| ------ | ------------- |
| BR-001 | Hanya Published Product yang boleh digunakan Marketplace |
| BR-002 | Draft Product tidak boleh terlihat oleh Customer |
| BR-003 | Product Inactive tidak digunakan untuk Quote baru |
| BR-012 | Quote menggunakan Product Version saat Quote dibuat |
| BR-015 | Product Catalog menjadi sumber data resmi seluruh marketplace |

:contentReference[oaicite:2]{index=2}

---

# 8. Query Behaviour

## Marketplace

Hanya dapat melihat

- Published Product

---

## Quote Service

Hanya mengambil

- Published Product

---

## Proposal Service

Mengambil

- Published Product
- Historical Version

karena Proposal membutuhkan snapshot produk.

---

## Checkout Service

Mengambil

Product Version tertentu yang direferensikan oleh Proposal atau Checkout Request.

---

## Reporting

Dapat membaca

- Draft
- Published
- Archived

karena kebutuhan analisis.

**Assumption:** BRD tidak mendefinisikan hak akses Reporting terhadap seluruh status produk. Perilaku ini perlu divalidasi bersama Business Owner.

---

# 9. REST API

---

## Search Product

```
GET /api/v1/products
```

---

### Query Parameter

| Parameter | Type |
| ----------- | ------ |
| page | Integer |
| size | Integer |
| companyCode | String |
| productCode | String |
| productName | String |
| category | String |
| status | String |

---

## Product Detail

```
GET /api/v1/products/{productId}
```

---

## Product Version History

```
GET /api/v1/products/{productId}/versions
```

---

## Product By Version

```
GET /api/v1/products/{productId}/versions/{version}
```

---

# 10. Response Example

## Product Listing

```json
{
  "content": [
    {
      "productId": "UUID",
      "productCode": "PA001",
      "productName": "Personal Accident Basic",
      "companyCode": "PRU",
      "status": "PUBLISHED",
      "version": 3
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 125,
  "totalPages": 7
}
```

---

## Product Detail

```json
{
  "productId": "UUID",
  "productCode": "PA001",
  "productName": "Personal Accident Basic",
  "company": {
    "companyCode": "PRU",
    "companyName": "Prudential Indonesia"
  },
  "status": "PUBLISHED",
  "version": 3,
  "coverage": [],
  "benefits": [],
  "exclusions": [],
  "eligibility": {},
  "premiumConfiguration": [],
  "documents": []
}
```

---

## Version History

```json
{
  "productId": "UUID",
  "versions": [
    {
      "version": 1,
      "status": "ARCHIVED"
    },
    {
      "version": 2,
      "status": "ARCHIVED"
    },
    {
      "version": 3,
      "status": "PUBLISHED"
    }
  ]
}
```

---

# 11. Error Response

## Product Not Found

```json
{
  "code": "PRODUCT_NOT_FOUND",
  "message": "Product not found."
}
```

---

## Invalid Parameter

```json
{
  "code": "INVALID_QUERY_PARAMETER",
  "message": "Invalid query parameter."
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

# 12. Sequence Diagram

## Product Search

```mermaid
sequenceDiagram

actor Consumer

participant API

participant Query Service

participant Repository

database Database

Consumer->>API: GET /products

API->>Query Service: Search Product

Query Service->>Repository: Find Product

Repository->>Database: SELECT

Database-->>Repository: Result

Repository-->>Query Service: Product List

Query Service-->>API: Response

API-->>Consumer: Product List
```

---

## Product Detail

```mermaid
sequenceDiagram

actor Marketplace

participant API

participant Query Service

participant Repository

database Database

Marketplace->>API: GET Product Detail

API->>Query Service: Find Product

Query Service->>Repository: Query Aggregate

Repository->>Database: SELECT

Database-->>Repository: Product Aggregate

Repository-->>Query Service: Product

Query Service-->>API: Response

API-->>Marketplace: Product Detail
```

---

# 13. Performance Requirement

| Requirement | Value |
| ------------ | ------- |
| Response Time | < 300 ms |
| Pagination | Mandatory |
| Sorting | Supported |
| Filtering | Supported |
| Cache | Redis |
| Compression | GZIP |

Requirement mengikuti NFR pada BRD. Redis digunakan untuk optimasi pembacaan metadata produk. :contentReference[oaicite:3]{index=3}

---

# 14. Security

Authentication

- OAuth2
- JWT

Authorization

| Role | Permission |
| ------ | ------------ |
| Product Administrator | Read All |
| Business User | Read All |
| Marketplace | Read Published |
| Quote Service | Read Published |
| Proposal Service | Read Published + Version |
| Checkout Service | Read Product Version |
| Reporting | Read Metadata |

---

# 15. Acceptance Criteria

| ID | Scenario | Expected Result |
| ---- | ---------- | ---------------- |
| AC-01 | Search Product | Data ditemukan |
| AC-02 | Search tanpa filter | Mengembalikan seluruh data sesuai hak akses |
| AC-03 | Product Detail | Detail produk ditampilkan |
| AC-04 | Product tidak ditemukan | HTTP 404 |
| AC-05 | Version History | Semua versi ditampilkan |
| AC-06 | Marketplace meminta Draft Product | Ditolak / tidak ditampilkan |
| AC-07 | Quote meminta Published Product | Berhasil |
| AC-08 | Proposal meminta Historical Version | Berhasil |
| AC-09 | Pagination | Berjalan sesuai parameter |
| AC-10 | Sorting | Berjalan sesuai parameter |

---

# 16. Requirement Traceability Matrix

| BRD | Functional Requirement |
| ------ | ------------------------ |
| BR-11 | Product Search |
| BR-12 | Product Detail |
| BR-13 | Version History |
| BR-14 | Historical Query |
| BR-15 | Product Query API |

---

# 17. Open Items / Business Clarification

| ID | Question |
| ---- | ---------- |
| OI-01 | Apakah Product Search mendukung full-text search atau exact match? |
| OI-02 | Apakah pencarian harus mendukung multi-keyword? |
| OI-03 | Apakah Product Listing memiliki default sorting? |
| OI-04 | Apakah Reporting dapat mengakses seluruh Product Version termasuk Draft? |
| OI-05 | Apakah API mendukung pencarian berdasarkan Effective Date Range? |
| OI-06 | Apakah Marketplace memerlukan endpoint khusus untuk katalog publik atau menggunakan endpoint Product Search yang sama? |

---

# 18. Architecture Notes

Product Query merupakan **Read Side** pada Product Catalog dan bersifat **stateless**.

Seluruh consumer wajib mengakses Product Catalog melalui REST API.

Tidak diperbolehkan melakukan database sharing antar service.

Untuk memenuhi target performa (<300 ms), implementasi direkomendasikan menggunakan:

- Redis Cache untuk Product Detail dan Product Listing.
- Pagination pada seluruh endpoint listing.
- Database Index pada `company_code`, `product_code`, `product_name`, `status`, `effective_date`, dan `version`.
- Optimized Read Model (CQRS Read Side) apabila volume data meningkat.

Dengan pendekatan ini, Product Catalog tetap menjadi **Single Source of Truth** sekaligus mampu melayani kebutuhan Marketplace, Quote Service, Proposal Service, Checkout Service, dan Reporting secara konsisten.
