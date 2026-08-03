# FSD_10_TEST_SCENARIO.md

> **Functional Specification Document (FSD)**  
> **Module:** Test Scenario  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft  
> **Reference:** BRD-PC-001 (Business Requirements, Business Rules, Non Functional Requirements) :contentReference[oaicite:0]{index=0} :contentReference[oaicite:1]{index=1}

---

# 1. Purpose

Dokumen ini mendefinisikan seluruh skenario pengujian (Test Scenario) untuk Product Catalog Service.

Dokumen ini menjadi acuan bagi:

- QA Engineer
- System Integration Test (SIT)
- User Acceptance Test (UAT)
- Regression Test
- Automation Test

Seluruh test scenario disusun berdasarkan Business Requirement Document (BRD).

---

# 2. Objective

Testing bertujuan memastikan bahwa:

- seluruh Business Requirement berjalan sesuai BRD;
- seluruh Business Rule diterapkan dengan benar;
- Product Catalog dapat diintegrasikan dengan consumer;
- perubahan versi tidak merusak data historis;
- keamanan sistem berjalan sesuai standar.

---

# 3. Test Scope

## In Scope

- Company Management
- Product Management
- Product Configuration
- Product Query
- Versioning
- Audit Trail
- Security
- Integration
- Validation

## Out of Scope

- Premium Calculation
- Eligibility Validation
- Quote Engine
- Checkout
- Payment
- Policy Issuance
- Underwriting
- Claims

---

# 4. Test Strategy

| Test Level | Description |
| ------------ | ------------- |
| Unit Test | Domain & Business Rule |
| Integration Test | Database & REST API |
| API Test | REST Endpoint |
| Contract Test | Consumer Contract |
| Performance Test | Load & Stress |
| Security Test | Authentication & Authorization |
| UAT | Business Validation |

---

# 5. Company Management Test

## TC-COMP-001

### Scenario

Create Company berhasil.

### Precondition

Administrator telah login.

### Steps

1. Input Company Code.
2. Input Company Name.
3. Submit.

### Expected Result

Company berhasil dibuat.

---

## TC-COMP-002

### Scenario

Company Code kosong.

### Expected Result

HTTP 400.

Validation Error.

---

## TC-COMP-003

### Scenario

Duplicate Company Code.

### Expected Result

Request ditolak.

---

## TC-COMP-004

### Scenario

Deactivate Company.

### Expected Result

Status menjadi INACTIVE.

---

# 6. Product Management Test

## TC-PROD-001

Create Product.

Expected:

Product Draft berhasil dibuat.

---

## TC-PROD-002

Duplicate Product Code.

Expected:

Validation Error.

---

## TC-PROD-003

Product tanpa Company.

Expected:

Request ditolak.

---

## TC-PROD-004

Update Draft Product.

Expected:

Berhasil.

---

## TC-PROD-005

Update Published Product.

Expected:

Ditolak.

---

## TC-PROD-006

Archive Product.

Expected:

Status menjadi ARCHIVED.

---

# 7. Product Configuration Test

## TC-CONF-001

Tambah Coverage.

Expected:

Coverage tersimpan.

---

## TC-CONF-002

Tambah Benefit.

Expected:

Benefit tersimpan.

---

## TC-CONF-003

Tambah Eligibility.

Expected:

Eligibility tersimpan.

---

## TC-CONF-004

Tambah Premium Configuration.

Expected:

Configuration tersimpan.

---

## TC-CONF-005

Tambah Product Document.

Expected:

Metadata Document tersimpan.

---

# 8. Publish Validation Test

## TC-PUB-001

Publish tanpa Coverage.

Expected:

Publish gagal.

---

## TC-PUB-002

Publish tanpa Benefit.

Expected:

Publish gagal.

---

## TC-PUB-003

Publish tanpa Eligibility.

Expected:

Publish gagal.

---

## TC-PUB-004

Publish tanpa Premium Configuration.

Expected:

Publish gagal.

---

## TC-PUB-005

Publish Product lengkap.

Expected:

Status menjadi Published.

---

# 9. Product Query Test

## TC-QUERY-001

Search Product.

Expected:

Product ditemukan.

---

## TC-QUERY-002

Product Detail.

Expected:

Seluruh metadata ditampilkan.

---

## TC-QUERY-003

Version History.

Expected:

Semua versi muncul.

---

## TC-QUERY-004

Product tidak ditemukan.

Expected:

HTTP 404.

---

# 10. Versioning Test

## TC-VERSION-001

Update Published Product.

Expected:

Draft Version baru dibuat.

---

## TC-VERSION-002

Publish Draft Version.

Expected:

Version bertambah.

---

## TC-VERSION-003

Historical Version.

Expected:

Version lama tetap tersedia.

---

## TC-VERSION-004

Version Number.

Expected:

Incremental.

---

# 11. Audit Test

## TC-AUDIT-001

Create Product.

Expected:

Audit dibuat.

---

## TC-AUDIT-002

Update Product.

Expected:

Audit dibuat.

---

## TC-AUDIT-003

Publish Product.

Expected:

Audit dibuat.

---

## TC-AUDIT-004

Audit History.

Expected:

Histori tampil.

---

# 12. Security Test

## TC-SEC-001

Request tanpa JWT.

Expected:

401 Unauthorized.

---

## TC-SEC-002

JWT Expired.

Expected:

401 Unauthorized.

---

## TC-SEC-003

Role tidak sesuai.

Expected:

403 Forbidden.

---

## TC-SEC-004

Marketplace membaca Draft Product.

Expected:

403 atau data tidak ditampilkan sesuai kebijakan akses.

---

# 13. Integration Test

## TC-INT-001

Marketplace mengambil Product.

Expected:

Berhasil.

---

## TC-INT-002

Quote mengambil Product.

Expected:

Berhasil.

---

## TC-INT-003

Proposal mengambil Product Version.

Expected:

Berhasil.

---

## TC-INT-004

Checkout mengambil Product Version.

Expected:

Berhasil.

---

## TC-INT-005

Reporting mengambil Product.

Expected:

Berhasil.

---

# 14. Validation Test

## TC-VAL-001

Mandatory Field kosong.

Expected:

Validation Error.

---

## TC-VAL-002

Duplicate Product Code.

Expected:

Validation Error.

---

## TC-VAL-003

Publish Product belum lengkap.

Expected:

Business Validation gagal.

---

## TC-VAL-004

Invalid Status Transition.

Expected:

Request ditolak.

---

# 15. API Test

| ID | Endpoint | Expected |
| ---- | ---------- | ---------- |
| API-001 | POST /companies | 201 |
| API-002 | PUT /companies/{id} | 200 |
| API-003 | POST /products | 201 |
| API-004 | PUT /products/{id} | 200 |
| API-005 | POST /products/{id}/publish | 200 |
| API-006 | GET /products | 200 |
| API-007 | GET /products/{id} | 200 |
| API-008 | GET /products/{id}/versions | 200 |
| API-009 | GET /products/{id}/audit | 200 |

---

# 16. Performance Test

| Test | Expected Result |
| ------ | ----------------- |
| Search Product | < 300 ms |
| Product Detail | < 300 ms |
| Version History | < 500 ms |
| Audit History | < 500 ms |

Mengikuti target Non Functional Requirement pada BRD. :contentReference[oaicite:2]{index=2}

---

# 17. Concurrency Test

## TC-CON-001

Dua administrator mengubah Draft Product yang sama.

Expected:

Optimistic Locking mendeteksi konflik.

---

## TC-CON-002

Dua administrator melakukan Publish bersamaan.

Expected:

Hanya satu transaksi berhasil.

---

# 18. Soft Delete Test

## TC-DEL-001

Archive Product.

Expected:

Data masih tersedia di database.

---

## TC-DEL-002

Query Published Product.

Expected:

Archived Product tidak muncul.

---

# 19. Regression Test

Regression wajib dilakukan pada perubahan:

- Company
- Product
- Configuration
- Version
- Security
- Query
- Audit

---

# 20. Requirement Traceability Matrix

| BRD | Test Scenario |
| ------ | --------------- |
| BR-01 | Company Test |
| BR-02 | Product Test |
| BR-03 | Configuration Test |
| BR-04 | Query Test |
| BR-05 | Publish Test |
| BR-06 | Version Test |
| BR-07 | Audit Test |
| BR-08 | Validation Test |
| NFR | Performance Test |
| NFR Security | Security Test |

---

# 21. Test Data

## Company

| Field | Value |
| -------- | ------- |
| Company Code | PRU |
| Company Name | Prudential Indonesia |

---

## Product

| Field | Value |
| -------- | ------- |
| Product Code | PA001 |
| Product Name | Personal Accident Basic |
| Status | DRAFT |

---

## User

| Role | Username |
| ------ | ---------- |
| Product Administrator | admin |
| Business User | business |
| Read Only | readonly |
| Marketplace | marketplace |

---

# 22. Entry Criteria

Testing dapat dimulai apabila:

- Build berhasil.
- Database Migration selesai.
- API tersedia.
- Test Data tersedia.
- Authentication aktif.

---

# 23. Exit Criteria

Testing dinyatakan selesai apabila:

- Seluruh Critical Test Case lulus.
- Tidak ada Critical Defect.
- Seluruh Business Rule tervalidasi.
- UAT disetujui Business Owner.

---

# 24. Open Items / Business Clarification

| ID | Question |
| ---- | ---------- |
| OI-01 | Apakah UAT memerlukan skenario negative test tambahan di luar BRD? |
| OI-02 | Berapa target jumlah concurrent user untuk Performance Test? BRD tidak mendefinisikannya. |
| OI-03 | Apakah Security Penetration Test menjadi bagian dari ruang lingkup proyek ini? |
| OI-04 | Apakah diperlukan Contract Test untuk seluruh consumer atau hanya Marketplace? |
| OI-05 | Apakah Audit Report juga menjadi bagian dari UAT? |

---

# 25. Architecture Notes

## Testing Pyramid

```text
                UAT

          Contract Test

      Integration Test

         Repository Test

           API Test

           Unit Test
```

## Test Responsibility

| Test Type | Owner |
| ------------ | ------- |
| Unit Test | Backend Developer |
| Repository Test | Backend Developer |
| API Test | Backend Developer / QA |
| Integration Test | QA |
| Contract Test | Backend Developer |
| Performance Test | QA / DevOps |
| Security Test | Security Team |
| UAT | Business User |

Seluruh Business Rule yang terdapat pada Product Aggregate harus memiliki **Unit Test**, sedangkan seluruh REST API wajib memiliki **Integration Test** dan **API Test**. Dengan pendekatan ini, setiap requirement pada BRD memiliki minimal satu skenario pengujian yang dapat ditelusuri (traceable) hingga tahap UAT.
