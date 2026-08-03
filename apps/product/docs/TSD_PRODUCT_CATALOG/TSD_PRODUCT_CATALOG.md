# TSD_PRODUCT_CATALOG.md

> **Technical Specification Document (TSD)**  
> **Project:** Pulse Engine  
> **Service:** Product Catalog Service  
> **Document Version:** 1.0  
> **Document Status:** Draft  
> **Technology Stack:** Java 25, Spring Boot 4.0.7, PostgreSQL, Redis, OAuth2, JWT  
> **Architecture:** Domain Driven Design (DDD), Hexagonal Architecture, Clean Architecture

---

# Document Information

| Item | Value |
| ------ | ------- |
| Project | Pulse Engine |
| Module | Product Catalog Service |
| Document Type | Technical Specification Document |
| Audience | Solution Architect, Backend Developer, Frontend Developer, QA Engineer, DevOps Engineer, Security Engineer, SRE |
| Related Documents | BRD, FSD, Architecture Decision Records (ADR) |
| Status | Draft |

---

# 1. Purpose

Dokumen ini merupakan **Technical Specification Document (TSD)** untuk Product Catalog Service.

Dokumen ini menjadi acuan implementasi teknis bagi seluruh engineering team dalam membangun Product Catalog Service secara konsisten sesuai Business Requirement Document (BRD) dan Functional Specification Document (FSD).

Dokumen ini tidak mendefinisikan kebutuhan bisnis baru maupun memperluas ruang lingkup yang telah ditetapkan pada BRD.

---

# 2. Goals

Tujuan dokumen ini adalah:

- Menjadi acuan implementasi backend.
- Menjadi referensi API bagi frontend dan consumer service.
- Menjadi referensi database engineer.
- Menjadi referensi DevOps deployment.
- Menjadi referensi QA dalam penyusunan test case.
- Menjadi referensi Security Engineer.
- Menjadi referensi Solution Architect untuk menjaga konsistensi implementasi.

---

# 3. Scope

Technical Specification ini mencakup implementasi teknis untuk:

- Insurance Company Management
- Product Management
- Product Configuration
- Product Query
- Product Versioning
- Audit Trail
- Security
- Validation
- Reporting API
- Integration API

---

# 4. Out of Scope

Dokumen ini **tidak** mencakup implementasi berikut karena berada di luar ruang lingkup Product Catalog Service.

- Premium Calculation
- Eligibility Engine
- Quote Engine
- Proposal Engine
- Checkout Process
- Payment
- Policy Issuance
- Underwriting
- Claims
- Campaign
- Notification

---

# 5. Document Reference

| Document | Description |
| ------------ | ---------------------------- |
| BRD | Business Requirement Document |
| FSD | Functional Specification Document |
| ADR | Architecture Decision Records |
| Coding Standard | Organization Technical Standard |

Prioritas referensi adalah:

```
BRD

↓

FSD

↓

ADR

↓

Technical Standard
```

Apabila terjadi konflik maka BRD menjadi referensi utama.

---

# 6. Target Technology

| Technology | Version |
| ------------ | ---------- |
| Java | 25 |
| Spring Boot | 4.0.7 |
| Spring Framework | 7 (dikelola Spring Boot 4.0.7) |
| Build Tool | Maven (Maven Wrapper) |
| PostgreSQL | 16+ (driver 42.7.11) |
| Redis | 7+ (Lettuce) |
| Flyway | 11.14.1 |
| OAuth2 | RFC 6749 |
| JWT | RFC 7519 |
| OpenAPI | 3.1 (springdoc-openapi 3.0.3) |
| Lombok | 1.18.46 |
| Testcontainers | 2.0.5 |
| Docker | Latest |
| Kubernetes | 1.30+ |
| Prometheus | Latest |
| OpenTelemetry | Latest |

---

# 7. Architecture Principles

Implementasi wajib mengikuti prinsip berikut.

- Domain Driven Design
- Hexagonal Architecture
- Clean Architecture
- SOLID
- Stateless Service
- Immutable DTO
- Constructor Injection
- Repository Pattern
- Separation of Concerns
- Single Responsibility Principle

---

# 8. Architectural Decisions

| Decision | Rationale |
| ----------- | ----------- |
| Hexagonal Architecture | Memisahkan domain dari teknologi |
| DDD | Menjaga Business Rule tetap berada pada Domain |
| REST API | Mendukung integrasi antar service |
| Spring Boot 4.0.7 | Framework aplikasi production-grade, modular, dan terkelola |
| Spring Data JPA | Persistence dengan repository pattern |
| Spring Security + OAuth2 Resource Server | Enterprise authentication & authorization |
| Spring Cache + Redis | Optimasi performa query |
| Flyway | Versioning database |
| OAuth2 + JWT | Enterprise authentication |
| Optimistic Locking | Mencegah lost update |
| Soft Delete | Menjaga histori data |
| Audit Trail | Kepatuhan terhadap audit |
| Testcontainers | Integration testing dengan container nyata |

---

# 9. High-Level Architecture

```mermaid
flowchart LR

subgraph Consumer

Marketplace

Quote Service

Proposal Service

Checkout Service

Reporting

end

Gateway

ProductCatalog

Redis

PostgreSQL

Marketplace --> Gateway

Quote Service --> Gateway

Proposal Service --> Gateway

Checkout Service --> Gateway

Reporting --> Gateway

Gateway --> ProductCatalog

ProductCatalog --> Redis

ProductCatalog --> PostgreSQL
```

---

# 10. System Responsibilities

## Product Catalog bertanggung jawab terhadap

- Insurance Company
- Product
- Coverage
- Benefit
- Exclusion
- Eligibility Configuration
- Premium Configuration
- Product Document
- Product Version
- Audit Trail

---

## Product Catalog tidak bertanggung jawab terhadap

- Premium Calculation
- Eligibility Decision
- Quote
- Proposal
- Checkout
- Payment
- Policy
- Underwriting

---

# 11. Design Principles

Seluruh implementasi mengikuti prinsip berikut.

## High Cohesion

Setiap module hanya memiliki satu tanggung jawab.

---

## Low Coupling

Tidak ada dependency langsung antar service melalui database.

---

## API First

Seluruh consumer menggunakan REST API.

---

## Domain Centric

Business Rule hanya berada pada Domain Layer.

---

## Immutable Version

Published Product tidak dapat dimodifikasi.

---

## Single Source of Truth

Seluruh metadata Product berasal dari Product Catalog.

---

# 12. System Context

```mermaid
flowchart LR

Marketplace

Quote

Proposal

Checkout

Reporting

ProductCatalog

Marketplace --> ProductCatalog

Quote --> ProductCatalog

Proposal --> ProductCatalog

Checkout --> ProductCatalog

Reporting --> ProductCatalog
```

---

# 13. Technical Objectives

Product Catalog harus memenuhi target berikut.

| Objective | Target |
| ------------ | -------- |
| Availability | ≥ 99.9% |
| Stateless | Yes |
| Horizontal Scaling | Yes |
| Audit | Mandatory |
| Versioning | Mandatory |
| Soft Delete | Mandatory |
| Cache | Redis |
| Authentication | OAuth2 |
| Authorization | JWT |
| Database Migration | Flyway |

---

# 14. Design Constraints

Implementasi harus memenuhi batasan berikut.

- Tidak boleh melakukan database sharing.
- Tidak boleh melakukan Premium Calculation.
- Tidak boleh melakukan Eligibility Validation.
- Tidak boleh menyimpan state pada memory aplikasi.
- Tidak boleh mengubah Published Product.
- Tidak boleh menghapus histori Product.

---

# 15. Assumptions

Dokumen ini hanya menggunakan asumsi apabila informasi tidak tersedia pada BRD maupun FSD.

Daftar asumsi akan dicatat secara eksplisit pada dokumen teknis terkait.

Apabila tidak dapat diturunkan dari BRD/FSD maka akan diberi status:

```
Requires Functional Clarification
```

---

# 16. Technical Risks

| Risk | Mitigation |
| -------- | ------------ |
| Duplicate Product | Unique Constraint |
| Concurrent Update | Optimistic Locking |
| Lost Audit | Append-only Audit Trail |
| Cache Stale | Cache Invalidation |
| Unauthorized Access | OAuth2 + JWT |
| Database Failure | PostgreSQL HA |
| High Read Traffic | Redis Cache |

---

# 17. Document Structure

Technical Specification dipecah menjadi beberapa dokumen agar lebih mudah dipelihara.

| Document | Description |
| ------------ | ---------------------------- |
| TSD_PRODUCT_CATALOG | Overview |
| TSD_01_ARCHITECTURE | Technical Architecture |
| TSD_02_DOMAIN_MODEL | Domain Model |
| TSD_03_DATABASE | Database Design |
| TSD_04_API | REST API Specification |
| TSD_05_BUSINESS_RULE_IMPLEMENTATION | Business Rule Mapping |
| TSD_06_WORKFLOW | Workflow & Sequence |
| TSD_07_VERSIONING | Versioning Strategy |
| TSD_08_CACHE | Redis Strategy |
| TSD_09_SECURITY | Security Design |
| TSD_10_ERROR_HANDLING | Exception Strategy |
| TSD_11_LOGGING | Logging Specification |
| TSD_12_OBSERVABILITY | Monitoring & Tracing |
| TSD_13_PERFORMANCE | Performance Design |
| TSD_14_INTEGRATION | Integration Design |
| TSD_15_CONFIGURATION | Configuration Management |
| TSD_16_DEPLOYMENT | Deployment Architecture |
| TSD_17_TESTING | Testing Strategy |
| TSD_18_NFR_MAPPING | NFR Implementation |
| TSD_19_TRACEABILITY | Requirement Traceability |
| APPENDIX | Reference |

---

# 18. Technical Deliverables

Dokumen Technical Specification ini akan menghasilkan artefak implementasi berikut.

- Architecture Diagram
- Component Diagram
- Deployment Diagram
- Domain Model
- ERD
- Flyway Migration
- OpenAPI Specification
- Package Structure
- Java Code Skeleton
- SQL DDL
- Sequence Diagram
- State Machine
- Security Model
- Cache Strategy
- Deployment Architecture
- Testing Strategy
- Traceability Matrix

---

# 19. Traceability Principle

Seluruh implementasi harus dapat ditelusuri hingga BRD.

```text
BRD

↓

FSD

↓

REST API

↓

Application Service

↓

Domain Model

↓

Repository

↓

Database

↓

Test Case
```

Tidak boleh ada implementasi yang tidak memiliki referensi requirement.

---

# 20. Requires Functional Clarification

Beberapa implementasi teknis memerlukan klarifikasi apabila tidak tersedia pada BRD/FSD.

Contoh:

- Retention period Audit Trail.
- API rate limit.
- Identity Provider yang digunakan.
- Backup retention policy.
- Kubernetes resource sizing.
- Timeout standar antar service.
- Secret Management platform.
- Monitoring threshold.
- Disaster Recovery Objective (RTO/RPO).

Implementasi terhadap area tersebut **tidak boleh diasumsikan** tanpa keputusan dari Business Owner atau Enterprise Architecture.

---

# 21. Next Documents

Dokumen berikutnya yang menjadi dasar implementasi adalah:

1. **TSD_01_ARCHITECTURE.md** — Technical Architecture, Hexagonal Architecture, Dependency Rules, Package Structure, Module Structure, Component Diagram, Deployment Diagram.
2. **TSD_02_DOMAIN_MODEL.md** — DDD Aggregate, Entity, Value Object, Domain Service, Domain Event, Aggregate Boundary, Lifecycle.
3. **TSD_03_DATABASE.md** — ERD, Physical Model, Flyway, SQL DDL, Index Strategy, Optimistic Locking, Soft Delete.

Dokumen-dokumen tersebut akan menjadi dasar implementasi seluruh source code Product Catalog Service.
