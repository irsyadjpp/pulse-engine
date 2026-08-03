# Business Requirements Document (BRD)

# Product Catalog Service

**Project:** Pulse Engine – Personal Accident Insurance Marketplace

| Document Information | Value                      |
| -------------------- | -------------------------- |
| Document ID          | BRD-PC-001                 |
| Version              | 1.0                        |
| Status               | Draft                      |
| Project              | Pulse Engine               |
| Module               | Product Catalog            |
| Document Owner       | Business Product Owner     |
| Prepared By          | Business Analyst           |
| Reviewed By          | Solution Architect         |
| Approved By          | Product Steering Committee |
| Last Updated         | 03 August 2026             |

---

# BAB 1. Introduction

## 1.1 Purpose

Dokumen ini mendefinisikan kebutuhan bisnis untuk **Product Catalog Service** sebagai layanan master data yang mengelola seluruh produk Personal Accident Insurance pada Pulse Engine.

Product Catalog menjadi **Single Source of Truth** bagi seluruh layanan yang membutuhkan informasi produk, termasuk Quote, Proposal, Checkout, Policy Issuance, Reporting, dan Marketplace Frontend.

Dokumen ini digunakan sebagai acuan bagi:

* Business Owner
* Product Owner
* Business Analyst
* Solution Architect
* Development Team
* QA Team
* Integration Team

---

## 1.2 Background

Pulse Engine merupakan platform marketplace yang memungkinkan pelanggan membandingkan dan membeli produk Personal Accident Insurance dari berbagai perusahaan asuransi.

Masing-masing perusahaan memiliki:

* nama produk yang berbeda
* manfaat yang berbeda
* coverage yang berbeda
* aturan eligibility yang berbeda
* struktur premi yang berbeda
* periode pertanggungan yang berbeda
* pengecualian polis yang berbeda

Tanpa katalog produk terpusat akan terjadi:

* duplikasi data
* inkonsistensi informasi
* sulit melakukan onboarding insurer baru
* sulit melakukan audit perubahan produk
* tingginya biaya pemeliharaan

Untuk mengatasi permasalahan tersebut diperlukan Product Catalog Service.

---

## 1.3 Business Problem

Permasalahan bisnis yang ingin diselesaikan:

* Tidak terdapat repository produk yang terpusat.
* Setiap aplikasi menyimpan data produk sendiri.
* Sulit menambahkan perusahaan asuransi baru.
* Perubahan produk memerlukan perubahan di banyak sistem.
* Tidak terdapat versioning produk.
* Tidak tersedia histori perubahan produk.

---

## 1.4 Business Objectives

Product Catalog harus mampu:

* Menjadi master data seluruh produk Personal Accident Insurance.
* Mendukung banyak perusahaan asuransi.
* Mendukung versioning produk.
* Menyediakan data produk secara konsisten.
* Mendukung proses onboarding insurer secara cepat.
* Menjadi dasar seluruh proses penjualan.

---

# BAB 2. Business Scope

## 2.1 In Scope

### Insurance Company Management

* Registrasi insurer
* Aktivasi insurer
* Nonaktifkan insurer

### Product Management

* Create Product
* Update Product
* Publish Product
* Archive Product
* Product Version

### Product Configuration

* Benefit
* Coverage
* Exclusion
* Eligibility
* Premium Configuration
* Product Document

### Product Query

* Product Search
* Product Detail
* Product Listing

---

## 2.2 Out of Scope

* Quote
* Proposal
* Checkout
* Payment
* Policy Issuance
* Claim
* Underwriting
* Campaign
* Notification

---

# BAB 3. Current Business Process (AS-IS)

Saat ini informasi produk biasanya berasal dari berbagai sumber seperti spreadsheet, portal insurer, atau konfigurasi di masing-masing aplikasi.

Karakteristik proses saat ini:

* Tidak ada master data.
* Tidak ada standar struktur produk.
* Sulit melakukan sinkronisasi.
* Tidak ada histori perubahan.
* Tidak mendukung multi-version.
* Integrasi antar aplikasi tinggi (tight coupling).

---

# BAB 4. Future Business Process (TO-BE)

```text
Product Administrator
        │
        ▼
Create Insurance Company
        │
        ▼
Create Product
        │
        ▼
Configure Coverage
        │
        ▼
Configure Benefits
        │
        ▼
Configure Exclusions
        │
        ▼
Configure Eligibility
        │
        ▼
Configure Premium
        │
        ▼
Submit for Approval
        │
        ▼
Publish Product
        │
        ▼
Available for Marketplace
```

---

# BAB 5. Stakeholder Analysis

| Stakeholder           | Responsibility                    |
| --------------------- | --------------------------------- |
| Product Owner         | Menentukan produk yang dipasarkan |
| Product Administrator | Mengelola katalog produk          |
| Insurance Partner     | Menyediakan informasi produk      |
| Marketplace Frontend  | Menampilkan katalog               |
| Quote Service         | Mengambil data produk             |
| Proposal Service      | Mengambil snapshot produk         |
| Checkout Service      | Menggunakan referensi produk      |
| Reporting Team        | Analisis produk                   |
| Customer Service      | Membantu informasi produk         |

---

# BAB 6. Business Requirements

## BR-01

Sistem harus mendukung pengelolaan perusahaan asuransi.

---

## BR-02

Sistem harus mendukung pembuatan produk baru.

---

## BR-03

Sistem harus mendukung perubahan produk.

---

## BR-04

Sistem harus mendukung publish produk.

---

## BR-05

Sistem harus mendukung penghentian produk tanpa menghapus histori.

---

## BR-06

Setiap produk harus memiliki informasi manfaat.

---

## BR-07

Setiap produk harus memiliki informasi pengecualian.

---

## BR-08

Setiap produk harus memiliki konfigurasi coverage.

---

## BR-09

Setiap produk harus memiliki konfigurasi eligibility.

---

## BR-10

Setiap produk harus memiliki konfigurasi premi.

---

## BR-11

Sistem harus menyediakan pencarian produk.

---

## BR-12

Sistem harus menyediakan detail produk.

---

## BR-13

Setiap perubahan produk harus menghasilkan versi baru.

---

## BR-14

Sistem harus menyediakan histori perubahan produk.

---

## BR-15

Seluruh layanan marketplace harus menggunakan Product Catalog sebagai sumber data resmi.

---

# BAB 7. Business Rules

| Rule ID | Business Rule                                                                      |
| ------- | ---------------------------------------------------------------------------------- |
| BR-001  | Hanya produk berstatus Published yang boleh digunakan oleh marketplace.            |
| BR-002  | Produk Draft tidak boleh terlihat oleh customer.                                   |
| BR-003  | Produk Inactive tidak boleh digunakan untuk Quote baru.                            |
| BR-004  | Produk Published tidak boleh diubah langsung.                                      |
| BR-005  | Setiap perubahan produk menghasilkan versi baru.                                   |
| BR-006  | Satu produk hanya dimiliki oleh satu perusahaan asuransi.                          |
| BR-007  | Satu perusahaan asuransi dapat memiliki banyak produk.                             |
| BR-008  | Setiap produk minimal memiliki satu Benefit.                                       |
| BR-009  | Setiap produk minimal memiliki satu Coverage.                                      |
| BR-010  | Eligibility wajib dikonfigurasi sebelum produk dipublikasikan.                     |
| BR-011  | Premium Configuration wajib tersedia sebelum produk dipublikasikan.                |
| BR-012  | Produk yang sudah digunakan dalam Quote tetap menggunakan versi saat Quote dibuat. |

---

# BAB 8. Business Data Requirements

## Master Data

### Insurance Company

* Company Code
* Company Name
* Logo
* Contact Information
* Status

---

### Product

* Product Code
* Product Name
* Product Category
* Product Version
* Product Status
* Effective Date
* Expiry Date

---

### Coverage

* Coverage Amount
* Currency

---

### Benefit

* Benefit Name
* Description
* Maximum Limit

---

### Exclusion

* Exclusion Description

---

### Eligibility

* Minimum Age
* Maximum Age
* Occupation Class
* Nationality
* Residency

---

### Premium Configuration

* Coverage Band
* Age Band
* Occupation Class
* Base Premium

---

# BAB 9. Reporting Requirements

## Operational Report

* Active Product
* Inactive Product
* Draft Product
* Published Product

---

## Business Report

* Product per Insurance Company
* Product Growth
* Product Usage
* Product Version History

---

## Audit Report

* Product Changes
* Publish History
* Version History
* User Activity

---

# BAB 10. Non Functional Requirements

| Category     | Requirement        |
| ------------ | ------------------ |
| Availability | 99.9%              |
| Performance  | <300 ms            |
| Scalability  | Horizontal Scaling |
| Security     | OAuth2 / JWT       |
| Encryption   | TLS 1.3            |
| Audit Trail  | Mandatory          |
| Versioning   | Mandatory          |
| Soft Delete  | Mandatory          |
| Backup       | Daily              |
| Caching      | Redis              |

---

# BAB 11. Assumptions, Constraints and Risks

## Assumptions

* Product Administrator memiliki hak untuk mengelola katalog.
* Seluruh perusahaan asuransi mengikuti standar data Product Catalog.
* Product Catalog menjadi referensi tunggal untuk seluruh consumer.

---

## Constraints

* Product Catalog hanya menyimpan metadata produk.
* Perhitungan premi dilakukan oleh Premium Engine.
* Validasi eligibility dilakukan oleh Eligibility Engine.
* Product Catalog tidak melakukan proses transaksi.

---

## Risks

| Risk                                      | Mitigation                          |
| ----------------------------------------- | ----------------------------------- |
| Produk berubah saat transaksi berlangsung | Menggunakan versioning dan snapshot |
| Data produk tidak lengkap                 | Validasi sebelum publish            |
| Banyak perubahan produk                   | Approval workflow dan audit trail   |
| Downtime Product Catalog                  | Redis cache dan high availability   |

---

# BAB 12. Success Criteria

Implementasi Product Catalog dianggap berhasil apabila:

* Seluruh produk Personal Accident dikelola melalui Product Catalog.
* Seluruh consumer menggunakan Product Catalog sebagai sumber data resmi.
* Onboarding perusahaan asuransi baru dapat dilakukan tanpa perubahan aplikasi consumer.
* Setiap perubahan produk memiliki histori yang lengkap.
* Produk yang telah digunakan pada Quote, Proposal, atau Checkout tetap dapat direferensikan melalui mekanisme versioning.
* Informasi produk yang ditampilkan kepada pelanggan selalu konsisten pada seluruh kanal marketplace.
