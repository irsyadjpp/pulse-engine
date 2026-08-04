# TSD_18_NFR_MAPPING.md

> **Technical Specification Document (TSD)**  
> **Module:** Non-Functional Requirement (NFR) Mapping  
> **Project:** Pulse Engine – Product Catalog Service  
> **Version:** 1.0  
> **Status:** Draft

---

# 1. Purpose

Dokumen ini menjelaskan bagaimana setiap **Non-Functional Requirement (NFR)** pada BRD diterjemahkan menjadi implementasi teknis yang dapat diimplementasikan, diuji, dimonitor, dan dipelihara.

Dokumen ini memastikan bahwa seluruh kebutuhan non-fungsional memiliki:

- Solusi Teknis
- Implementasi
- Metode Verifikasi
- Monitoring
- Acceptance Criteria

---

# 2. Objectives

Dokumen ini bertujuan untuk:

- Memetakan seluruh NFR ke implementasi teknis
- Memastikan setiap NFR dapat diverifikasi
- Menjadi acuan QA, DevOps, SRE, dan Architect
- Mendukung Production Readiness Review

---

# 3. NFR Categories

Product Catalog mengelompokkan NFR menjadi:

| Category | Description |
|-----------|-------------|
| Availability | Ketersediaan layanan |
| Reliability | Keandalan sistem |
| Scalability | Kemampuan bertambah sesuai beban |
| Performance | Kecepatan layanan |
| Security | Perlindungan data dan akses |
| Maintainability | Kemudahan pemeliharaan |
| Observability | Monitoring & Troubleshooting |
| Recoverability | Pemulihan setelah kegagalan |
| Compatibility | Kompatibilitas API |
| Compliance | Kepatuhan terhadap standar organisasi |

---

# 4. NFR Mapping Framework

```mermaid
flowchart LR

BRD

NFR

TechnicalSolution

Implementation

Verification

Monitoring

BRD --> NFR

NFR --> TechnicalSolution

TechnicalSolution --> Implementation

Implementation --> Verification

Verification --> Monitoring
```

---

# 5. Availability

## Requirement

Service harus tersedia untuk seluruh consumer.

---

## Technical Solution

- Stateless Service
- Kubernetes Deployment
- Multi Replica
- Health Check
- Rolling Update

---

## Implementation

- Spring Boot Actuator
- Kubernetes Deployment
- Readiness Probe
- Liveness Probe

---

## Verification

- Chaos Test
- Failover Test
- Rolling Update Test

---

## Monitoring

- Uptime
- Health Endpoint
- Pod Status

---

## Acceptance Criteria

| Metric | Target |
|---------|--------|
| Availability | ≥ 99.9% |

---

# 6. Reliability

## Technical Solution

- Optimistic Locking
- Transaction Management
- Retry untuk transient failure
- Graceful Shutdown

---

## Verification

- Concurrency Test
- Failure Injection
- Recovery Test

---

## Monitoring

- Failed Request
- Exception Rate
- Transaction Failure

---

# 7. Performance

## Technical Solution

- Redis Cache
- Index Optimization
- DTO Projection
- Pagination
- Lazy Loading

---

## Verification

- Load Test
- Stress Test
- Spike Test

---

## Monitoring

- Response Time
- Cache Hit Ratio
- Slow Query

---

## Acceptance Criteria

| Metric | Target |
|---------|--------|
| Average Response | <150 ms |
| P95 | <300 ms |
| P99 | <500 ms |

---

# 8. Scalability

## Technical Solution

- Stateless Architecture
- Horizontal Pod Autoscaler
- Connection Pool
- Redis

---

## Verification

- Horizontal Scaling Test
- Auto Scaling Test

---

## Monitoring

- CPU
- Memory
- Replica Count

---

# 9. Security

## Technical Solution

- OAuth2
- JWT
- TLS
- RBAC
- Audit Trail

---

## Verification

- Authentication Test
- Authorization Test
- Vulnerability Scan

---

## Monitoring

- Login Failure
- Access Denied
- Invalid Token

---

# 10. Maintainability

## Technical Solution

- Hexagonal Architecture
- DDD
- SOLID
- Clean Architecture
- ArchUnit

---

## Verification

- Architecture Test
- Code Review
- SonarQube

---

## Monitoring

- Technical Debt
- Code Coverage
- Maintainability Rating

---

# 11. Observability

## Technical Solution

- Micrometer
- Prometheus
- Grafana
- OpenTelemetry
- Structured Logging

---

## Verification

- Metrics Validation
- Trace Validation
- Dashboard Validation

---

## Monitoring

- Metrics
- Logs
- Traces

---

# 12. Recoverability

## Technical Solution

- Database Backup
- Kubernetes Restart
- Immutable Deployment

---

## Verification

- Backup Restore Test
- Disaster Recovery Test

---

## Monitoring

- Backup Status
- Recovery Time

---

# 13. Compatibility

## Technical Solution

- REST API
- OpenAPI 3.1
- URI Versioning

---

## Verification

- Contract Test
- Consumer Test

---

## Monitoring

- Deprecated API Usage
- API Version Adoption

---

# 14. Compliance

## Technical Solution

- Audit Trail
- Immutable Version
- Structured Logging

---

## Verification

- Audit Review
- Security Review

---

## Monitoring

- Audit Completeness
- Security Event

---

# 15. Availability Mapping

| NFR | Technical Solution | Verification | Monitoring |
|------|--------------------|--------------|------------|
| Availability | Multi Replica | Failover Test | Uptime |
| Reliability | Optimistic Lock | Concurrency Test | Error Rate |
| Performance | Redis + Index | Load Test | Latency |
| Scalability | HPA | Scaling Test | CPU |
| Security | OAuth2 | Security Test | Access Log |

---

# 16. Performance Mapping

| Requirement | Solution |
|-------------|----------|
| Fast Search | Composite Index |
| Fast Detail | Redis |
| Low Latency | DTO Projection |
| Large Dataset | Pagination |
| High Throughput | Stateless Service |

---

# 17. Security Mapping

| Requirement | Solution |
|-------------|----------|
| Authentication | OAuth2 |
| Authorization | RBAC |
| API Security | JWT |
| Secure Transport | TLS |
| Audit | Audit Trail |

---

# 18. Maintainability Mapping

| Requirement | Solution |
|-------------|----------|
| Modular | Hexagonal |
| Domain Driven | DDD |
| Testability | Dependency Injection |
| Clean Code | SOLID |
| Architecture | ArchUnit |

---

# 19. Observability Mapping

| Requirement | Solution |
|-------------|----------|
| Monitoring | Prometheus |
| Dashboard | Grafana |
| Tracing | OpenTelemetry |
| Logging | Structured JSON |
| Health | Actuator |

---

# 20. Recovery Mapping

| Requirement | Solution |
|-------------|----------|
| Backup | PostgreSQL Backup |
| Restart | Kubernetes |
| Redeploy | Immutable Image |
| Rollback | Rolling Deployment |

---

# 21. Verification Matrix

| NFR | Verification Method |
|------|---------------------|
| Availability | Failover Test |
| Performance | Load Test |
| Reliability | Concurrency Test |
| Security | Penetration Test |
| Observability | Metrics Validation |
| Recoverability | Restore Test |
| Compatibility | Contract Test |
| Maintainability | Architecture Test |

---

# 22. Monitoring Matrix

| NFR | Metric |
|------|--------|
| Availability | Uptime |
| Reliability | Error Rate |
| Performance | Response Time |
| Scalability | CPU |
| Security | Failed Authentication |
| Cache | Hit Ratio |
| Database | Connection Pool |
| JVM | Heap Usage |

---

# 23. Production Readiness Checklist

| Item | Required |
|------|----------|
| Health Check | ✔ |
| Metrics | ✔ |
| Tracing | ✔ |
| Logging | ✔ |
| Backup | ✔ |
| Restore Procedure | ✔ |
| Rolling Update | ✔ |
| TLS | ✔ |
| OAuth2 | ✔ |
| Audit Trail | ✔ |

---

# 24. Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| Stateless Service | Scalability |
| Redis | Performance |
| Kubernetes | Availability |
| OpenTelemetry | Observability |
| OAuth2 | Security |
| Flyway | Database Consistency |

---

# 25. Alternatives Considered

| Alternative | Decision | Reason |
|------------|----------|--------|
| Session-based Service | Tidak dipilih | Tidak scalable |
| Database Cache | Tidak dipilih | Redis lebih fleksibel |
| VM Deployment | Tidak dipilih | Tidak cloud-native |
| Manual Monitoring | Tidak dipilih | Tidak proaktif |
| Tanpa Audit Trail | Tidak dipilih | Tidak memenuhi kebutuhan enterprise |

---

# 26. Technical Risks

| Risk | Mitigation |
|------|------------|
| Single Point of Failure | Multi Replica |
| Cache Failure | Database Fallback |
| Database Bottleneck | Index Optimization |
| Configuration Error | Fail Fast |
| High Traffic | HPA |
| Slow Query | Query Optimization |

---

# 27. Recommendations

1. Jadikan seluruh NFR sebagai bagian dari Definition of Done.
2. Validasi seluruh target performa menggunakan load test sebelum production.
3. Monitoring harus aktif sejak environment SIT.
4. Lakukan disaster recovery drill secara berkala.
5. Gunakan dashboard observability sebagai bagian dari operational readiness review.

---

# 28. Non-Functional Requirement Governance

Tujuan NFR Mapping bukan untuk menentukan nilai SLA, RTO, atau compliance, tetapi untuk **memetakan bagaimana aplikasi memenuhi NFR yang telah ditentukan**. Yang perlu dibedakan:

- **NFR Requirement** → berasal dari BRD atau kebijakan organisasi.
- **Technical Solution** → dijelaskan di TSD.
- **Nilai target (SLA, RTO, dll.)** → boleh berasal dari organisasi dan tidak perlu di-hardcode oleh Product Catalog.

## 28.1 Service Level Agreement (SLA)

### Keputusan

Product Catalog mendukung pencapaian SLA organisasi melalui:

- Stateless Architecture
- Horizontal Scalability
- Health Check
- Monitoring
- Graceful Shutdown
- Retry Strategy
- Caching

Target SLA resmi ditentukan oleh organisasi dan tidak menjadi bagian dari implementasi aplikasi.

### Rationale

SLA merupakan komitmen layanan organisasi, sedangkan aplikasi menyediakan kemampuan teknis untuk mencapainya.

**Status:** ✅ Resolved

---

## 28.2 Service Level Objective (SLO)

### Keputusan

Product Catalog menyediakan seluruh telemetry yang diperlukan untuk pengukuran SLO.

Metrik yang tersedia:

- Availability
- Response Time
- Error Rate
- Throughput
- JVM Metrics
- Database Metrics
- Redis Metrics

Nilai target SLO mengikuti kebijakan SRE organisasi.

### Rationale

SLO merupakan target operasional, bukan logika aplikasi.

**Status:** ✅ Resolved

---

## 28.3 Recovery Time Objective (RTO)

### Keputusan

Product Catalog mendukung proses pemulihan melalui:

- Stateless Service
- Immutable Container Image
- Externalized Configuration
- Automated Deployment
- Database Backup
- Health Probe

Nilai RTO ditentukan oleh organisasi.

### Rationale

RTO merupakan bagian dari Business Continuity Plan.

**Status:** ✅ Resolved

---

## 28.4 Recovery Point Objective (RPO)

### Keputusan

Product Catalog mendukung pencapaian RPO melalui:

- PostgreSQL Backup
- WAL Archive (jika digunakan)
- Point-in-Time Recovery (PITR)
- Disaster Recovery Procedure

Target RPO mengikuti kebijakan organisasi.

### Rationale

RPO merupakan kebijakan operasional database dan disaster recovery.

**Status:** ✅ Resolved

---

## 28.5 Capacity Planning

### Keputusan

Product Catalog tidak menetapkan target Peak Concurrent User.

Aplikasi dirancang agar dapat diskalakan secara horizontal tanpa perubahan kode.

Strategi yang digunakan:

- Stateless Service
- Connection Pool
- Redis Cache
- Database Indexing
- Pagination
- Horizontal Pod Autoscaler

### Rationale

Jumlah concurrent user merupakan hasil Capacity Planning organisasi.

**Status:** ✅ Resolved

---

## 28.6 Compliance

### Keputusan

Product Catalog dirancang agar dapat memenuhi persyaratan kepatuhan organisasi.

Capability yang disediakan meliputi:

- Audit Trail
- RBAC
- OAuth2
- JWT
- Structured Logging
- Encryption in Transit (TLS)
- Secret Management
- Data Masking
- Soft Delete
- Versioning

Standar kepatuhan yang berlaku ditentukan oleh organisasi, misalnya:

- **Regulasi OJK** (sesuai yurisdiksi Indonesia)
- **Undang-Undang Perlindungan Data Pribadi (UU PDP)** jika berlaku
- **Standar keamanan informasi organisasi** (misalnya ISO/IEC 27001 atau yang setara)

### Rationale

Compliance merupakan kebijakan organisasi, sedangkan aplikasi menyediakan kontrol teknis yang diperlukan.

**Status:** ✅ Resolved

---

# 29. NFR Governance Summary

| Area | Technical Solution |
|------|--------------------|
| Availability | Stateless + Horizontal Scaling |
| Reliability | Health Check + Retry + Monitoring |
| Performance | Redis + Index + Pagination |
| Scalability | Kubernetes + HPA |
| Security | OAuth2 + JWT + RBAC + TLS |
| Maintainability | Hexagonal Architecture + DDD |
| Observability | OpenTelemetry + Prometheus + Structured Logging |
| Recoverability | Backup + Health Check + Immutable Deployment |
| Compliance | Audit Trail + Security Controls |
| Capacity | Horizontal Scaling |

---

## 29.1 Organization Policy Ownership

Item berikut merupakan **Organization Policy** — tidak dapat diputuskan oleh Product Catalog, tetapi juga bukan *Requires Functional Clarification*.

| Item                 | Pemilik Keputusan              |
| -------------------- | ------------------------------ |
| SLA Resmi            | Business / SRE                 |
| SLO Resmi            | SRE                            |
| RTO                  | Business Continuity Team       |
| RPO                  | DBA / Business Continuity Team |
| Peak Concurrent User | Capacity Planning              |
| Compliance Standard  | Risk, Compliance & Security    |

---

# 30. Complete NFR Traceability Matrix

| BRD NFR | Technical Solution | Spring Component | Verification | Monitoring |
|----------|-------------------|------------------|--------------|------------|
| Availability | Kubernetes Replica | Deployment | Failover Test | Health Check |
| Reliability | Transaction + Lock | Spring Transaction | Concurrency Test | Error Rate |
| Performance | Redis + Index | Spring Cache | Load Test | Latency |
| Scalability | HPA | Kubernetes | Scaling Test | CPU |
| Security | OAuth2 + JWT | Spring Security | Security Test | Audit Log |
| Maintainability | Hexagonal | Package Structure | ArchUnit | SonarQube |
| Observability | Actuator | Micrometer | Metrics Test | Prometheus |
| Recoverability | Backup | PostgreSQL | Restore Test | Backup Monitoring |
| Compatibility | OpenAPI | Spring MVC | Contract Test | API Metrics |
| Compliance | Audit Trail | Audit Module | Audit Review | Audit Dashboard |

---

# 31. Traceability

| BRD | FSD | TSD | Verification | Test Case |
|-----|-----|-----|--------------|-----------|
| NFR Availability | TSD-16 | Kubernetes | Failover | TC-NFR-001 |
| NFR Performance | TSD-13 | Redis | Load Test | TC-NFR-002 |
| NFR Security | TSD-09 | Spring Security | Security Test | TC-NFR-003 |
| NFR Logging | TSD-11 | Logback | Log Validation | TC-NFR-004 |
| NFR Observability | TSD-12 | Micrometer | Metrics Validation | TC-NFR-005 |

---

# 32. Next Document

**TSD_19_TRACEABILITY_MATRIX.md**

Dokumen berikut akan menjadi dokumen penutup yang menghubungkan seluruh artefak proyek secara end-to-end:

- BRD → FSD → TSD
- Business Requirement → API
- API → Domain
- Domain → Database
- Database → Implementation
- Implementation → Test Case
- End-to-End Requirement Traceability Matrix (RTM)
- Coverage Analysis
- Gap Analysis
- Impact Analysis
- Production Readiness Mapping

Dokumen ini akan menjadi acuan utama bagi QA, Solution Architect, Technical Lead, Auditor, dan Project Manager untuk memastikan **100% requirement coverage**.