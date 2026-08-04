# Compliance Reference Guide

# Product Catalog Service

## Pulse Engine - Personal Accident Insurance Marketplace

| Document Information | Value |
| -------------------- | -------------------- |
| Document | Compliance Reference Guide |
| Service | Product Catalog Service |
| Version | 1.0 |
| Status | Draft |
| Owner | Compliance Team |
| Last Updated | 04 August 2026 |

---

# 1. Purpose

Dokumen ini merupakan panduan compliance khusus untuk **Product Catalog Service** yang mengimplementasikan persyaratan dari:

* **UU PDP No. 27/2022** - Perlindungan Data Pribadi
* **POJK No. 13/2017** - Penggunaan TI dalam penyelenggaraan usaha jasa keuangan
* **POJK No. 69/2016** - Perusahaan asuransi
* **ISO/IEC 27001:2022** - Information Security Management System
* **ISO/IEC 22301:2019** - Business Continuity Management System
* **ISO 31000:2018** - Risk Management

Dokumen ini merujuk ke:
* [Enterprise Standards & Compliance Framework](../docs/16. ENTERPRISE_STANDARDS.md)
* [Compliance Implementation Matrix](../docs/18. COMPLIANCE_MATRIX.md)

---

# 2. Product Catalog Service - Compliance Scope

## 2.1 Data Classification

Product Catalog Service menangani data dengan classification sebagai berikut:

| Data Type | Classification | Protection Level |
|-----------|---------------|------------------|
| Insurance Company Information | **Internal** | Access control, integrity checks |
| Product Information | **Internal** | Access control, integrity checks, backup |
| Product Configuration | **Confidential** | Encryption at rest & transit, RBAC, audit trail |
| Eligibility Configuration | **Confidential** | Encryption at rest & transit, RBAC, audit trail |
| Premium Configuration | **Confidential** | Encryption at rest & transit, RBAC, audit trail |
| Audit Trail | **Restricted** | End-to-end encryption, strict access control, immutable storage |
| Product Version History | **Confidential** | Encryption, access control, backup |

---

## 2.2 Compliance Requirements

### UU PDP No. 27/2022

| Requirement | Product Catalog Implementation | Status | Reference |
|-------------|------------------------------|--------|-----------|
| **Data Minimization** | | | |
| Collect only necessary data | Product Catalog hanya menyimpan metadata produk yang diperlukan | ✅ Implemented | BRD Section 2.1 |
| Purpose limitation | Data hanya digunakan untuk product listing dan configuration | ✅ Implemented | FSD_01_INSURANCE_COMPANY_MANAGEMENT.md |
| **Data Accuracy** | | | |
| Data validation | Validasi input di API level dan database constraints | ✅ Implemented | TSD_04_API.md |
| **Integrity & Confidentiality** | | | |
| Encryption at rest | Database encryption untuk configuration tables | 🔄 Planned | TSD_03_DATABASE.md |
| Encryption in transit | TLS 1.3 untuk semua API communications | 🔄 Planned | TSD_14_INTEGRATION.md |
| **Storage Limitation** | | | |
| Retention policy | Product version retention sesuai retention schedule | 🔄 Planned | TSD_07_VERSIONING.md |
| **Audit Trail** | | | |
| Immutable audit logs | Comprehensive audit trail untuk seluruh operasi CRUD | 🔄 Planned | FSD_05_VERSIONING_AND_AUDIT.md |
| Audit log retention | 7-year retention untuk audit logs | 🔄 Planned | COMPLIANCE_MATRIX Section 6 |

---

### POJK No. 13/2017 - Penggunaan TI

| Requirement | Product Catalog Implementation | Status | Reference |
|-------------|------------------------------|--------|-----------|
| **IT Risk Management** | | | |
| Risk assessment | Technology risk register untuk Product Catalog | 🔄 Planned | Enterprise Standards Section 6 |
| Risk mitigation | Control implementation untuk availability dan security | 🔄 Planned | TSD_13_PERFORMANCE.md |
| **Audit Trail** | | | |
| Transaction logging | Immutable audit trail untuk semua product changes | 🔄 Planned | FSD_05_VERSIONING_AND_AUDIT.md |
| Change tracking | Product versioning dengan full history | ✅ Implemented | BRD Section 2.1 |
| **Business Continuity** | | | |
| High availability | ≥ 99.9% availability target | 🔄 Planned | TSD_13_PERFORMANCE.md |
| Backup & recovery | Daily backup dengan automated recovery | 🔄 Planned | TSD_16_DEPLOYMENT.md |
| RTO/RPO | RTO: 2 hours, RPO: 1 hour | 🔄 Planned | Enterprise Standards Section 6.3 |

---

### ISO/IEC 27001:2022 - ISMS

| Control | Product Catalog Implementation | Status | Reference |
|---------|------------------------------|--------|-----------|
| **A.8 - Asset Management** | | | |
| A.8.1 | Inventory of products and configurations | ✅ Implemented | Product Catalog Service |
| A.8.2 | Information classification for product data | 🔄 Planned | TSD_09_SECURITY.md |
| **A.9 - Access Control** | | | |
| A.9.1 | RBAC implementation untuk product management | 🔄 Planned | TSD_09_SECURITY.md |
| A.9.2 | User access management via IAM | 🔄 Planned | TSD_14_INTEGRATION.md |
| **A.10 - Cryptography** | | | |
| A.10.1 | Encryption untuk sensitive configuration data | 🔄 Planned | TSD_09_SECURITY.md |
| **A.12 - Operations Security** | | | |
| A.12.1 | Operational procedures untuk product management | 🔄 Planned | TSD_15_CONFIGURATION.md |
| A.12.3 | Backup procedures | 🔄 Planned | TSD_16_DEPLOYMENT.md |
| A.12.4 | Logging and monitoring | 🔄 Planned | TSD_11_LOGGING.md, TSD_12_OBSERVABILITY.md |
| **A.14 - System Acquisition** | | | |
| A.14.1 | Security requirements dalam development | 🔄 Planned | TSD_09_SECURITY.md |
| **A.16 - Incident Management** | | | |
| A.16.1 | Incident response untuk product catalog | 🔄 Planned | Enterprise Standards Section 4.2 |
| **A.17 - Business Continuity** | | | |
| A.17.1 | Business continuity plan | 🔄 Planned | Enterprise Standards Section 6.3 |

---

### ISO/IEC 22301:2019 - BCMS

| Requirement | Product Catalog Implementation | Status | Reference |
|-------------|------------------------------|--------|-----------|
| **Business Impact Analysis** | | | |
| BIA for Product Catalog | Maximum Tolerable Downtime: 4 hours | 🔄 Planned | Enterprise Standards Section 6.3 |
| **Business Continuity Strategy** | | | |
| Recovery strategy | Warm site dengan asynchronous replication | 🔄 Planned | TSD_16_DEPLOYMENT.md |
| **Business Continuity Plans** | | | |
| Recovery procedures | Automated failover procedures | 🔄 Planned | TSD_16_DEPLOYMENT.md |
| **Testing & Exercises** | | | |
| BCP testing | Semi-annual disaster recovery testing | 🔄 Planned | Enterprise Standards Section 4.2 |

---

### ISO 31000:2018 - Risk Management

| Requirement | Product Catalog Implementation | Status | Reference |
|-------------|------------------------------|--------|-----------|
| **Risk Assessment** | | | |
| Risk identification | Technology risk register | 🔄 Planned | Enterprise Standards Section 6.1 |
| Risk analysis | Likelihood dan impact assessment | 🔄 Planned | TSD_13_PERFORMANCE.md |
| **Risk Treatment** | | | |
| Risk mitigation | Control implementation untuk identified risks | 🔄 Planned | TSD_13_PERFORMANCE.md |
| **Monitoring & Review** | | | |
| Risk monitoring | Continuous monitoring via observability | 🔄 Planned | TSD_12_OBSERVABILITY.md |

---

# 3. Security Controls Implementation

## 3.1 Preventive Controls

| Control | Implementation | Status | TSD Reference |
|---------|---------------|--------|---------------|
| **Input Validation** | API Gateway + Service-level validation | 🔄 Planned | TSD_09_SECURITY.md |
| **SQL Injection Prevention** | Parameterized queries, JPA | ✅ Implemented | TSD_03_DATABASE.md |
| **XSS Prevention** | Output encoding, CSP headers | 🔄 Planned | TSD_09_SECURITY.md |
| **CSRF Prevention** | CSRF tokens untuk state-changing operations | 🔄 Planned | TSD_09_SECURITY.md |
| **Rate Limiting** | API Gateway + Service-level rate limiting | 🔄 Planned | TSD_14_INTEGRATION.md |
| **Authentication** | OAuth 2.0, JWT validation | 🔄 Planned | TSD_09_SECURITY.md |
| **Authorization** | RBAC enforcement | 🔄 Planned | TSD_09_SECURITY.md |
| **Encryption** | TLS 1.3, AES-256 | 🔄 Planned | TSD_09_SECURITY.md |

## 3.2 Detective Controls

| Control | Implementation | Status | TSD Reference |
|---------|---------------|--------|---------------|
| **Audit Logging** | Comprehensive audit trail untuk seluruh operasi | 🔄 Planned | TSD_11_LOGGING.md |
| **Security Monitoring** | SIEM integration untuk security events | 🔄 Planned | TSD_12_OBSERVABILITY.md |
| **Anomaly Detection** | Behavioral analytics untuk detection | 🔄 Planned | TSD_12_OBSERVABILITY.md |
| **Log Analysis** | Real-time log analysis dan alerting | 🔄 Planned | TSD_12_OBSERVABILITY.md |

## 3.3 Corrective Controls

| Control | Implementation | Status | TSD Reference |
|---------|---------------|--------|---------------|
| **Incident Response** | Automated alerting + manual response | 🔄 Planned | Enterprise Standards Section 4.2 |
| **Backup & Recovery** | Regular backup dengan verified restore | 🔄 Planned | TSD_16_DEPLOYMENT.md |
| **Patch Management** | Security patch deployment procedures | 🔄 Planned | TSD_15_CONFIGURATION.md |
| **Access Revocation** | Automated deprovisioning | 🔄 Planned | TSD_09_SECURITY.md |

---

# 4. Data Protection Measures

## 4.1 Encryption Standards

### Data at Rest

| Data Type | Encryption Method | Key Management | Status |
|-----------|------------------|----------------|--------|
| Product Configuration | AES-256 | HSM / KMS | 🔄 Planned |
| Audit Trail | AES-256 | HSM / KMS | 🔄 Planned |
| PII Data | AES-256 | HSM / KMS | 🔄 Planned |

### Data in Transit

| Communication Type | Protocol | Cipher Suites | Status |
|-------------------|----------|---------------|--------|
| Internal Service Communication | mTLS | TLS 1.3 | 🔄 Planned |
| External API | TLS 1.3 | Strong ciphers only | 🔄 Planned |
| Database Connection | TLS | TLS 1.2+ | 🔄 Planned |

## 4.2 Access Control

### Authentication

| Component | Requirement | Implementation | Status |
|-----------|-------------|---------------|--------|
| User Authentication | OAuth 2.0 / OIDC | Keycloak integration | 🔄 Planned |
| Service Authentication | mTLS atau JWT | Client credentials | 🔄 Planned |
| MFA | Required untuk privileged access | TBD | 🔄 Planned |

### Authorization

| Aspect | Requirement | Implementation | Status |
|--------|-------------|---------------|--------|
| Model | RBAC | Role-based access control | 🔄 Planned |
| Principle | Least privilege | Granular permissions | 🔄 Planned |
| Review | Quarterly access review | Automated access review | 🔄 Planned |

---

# 5. Audit & Compliance Monitoring

## 5.1 Audit Trail Implementation

### Events to be Logged

| Event Category | Specific Events | Retention Period |
|----------------|-----------------|------------------|
| **Authentication** | Login, Logout, Failed login, MFA events | 7 years |
| **Authorization** | Permission changes, Role assignments | 7 years |
| **Data Access** | Read, Write, Delete on Product data | 10 years |
| **Business Transactions** | Product Created, Updated, Published, Archived | 10 years |
| **Configuration Changes** | Coverage, Benefit, Exclusion, Eligibility, Premium changes | 7 years |
| **System Events** | Deployments, Configuration changes | 7 years |

### Audit Log Format

```json
{
  "timestamp": "2026-08-04T10:30:00Z",
  "eventId": "uuid-v4",
  "eventType": "PRODUCT_PUBLISHED",
  "severity": "INFO",
  "actor": {
    "userId": "user-123",
    "serviceName": "product-catalog-service",
    "ipAddress": "192.168.1.1",
    "userAgent": "Mozilla/5.0..."
  },
  "action": {
    "operation": "UPDATE",
    "resource": "Product",
    "resourceId": "PROD-001",
    "changes": {
      "status": {
        "oldValue": "DRAFT",
        "newValue": "PUBLISHED"
      }
    }
  },
  "outcome": {
    "status": "SUCCESS",
    "message": "Product published successfully"
  },
  "context": {
    "traceId": "trace-456",
    "correlationId": "correlation-789",
    "businessKey": "BK-001",
    "productVersion": "1.0.0"
  },
  "compliance": {
    "dataClassification": "CONFIDENTIAL",
    "retentionPeriod": "10 years",
    "regulatoryReference": ["POJK 13/2017", "UU PDP"]
  }
}
```

## 5.2 Compliance Metrics

| Metric | Target | Measurement | Frequency |
|--------|--------|-------------|-----------|
| Audit Log Coverage | 100% | Log completeness | Continuous |
| Configuration Change Tracking | 100% | Change detection | Real-time |
| Product Version Retention | 100% | Version availability | Per change |
| Access Review Completion | 100% | Review completion | Quarterly |
| Security Incident Response | < 1 hour | Response time | Per incident |
| Vulnerability Remediation | Critical: < 24h | Scan results | Weekly |

---

# 6. Data Governance

## 6.1 Master Data Management

### Product Catalog as System of Record

| Entity | System of Record | Consumers | Sync Method |
|--------|-----------------|-----------|-------------|
| Insurance Company | Product Catalog | All services | API |
| Product | Product Catalog | Quote, Proposal, Checkout | API + Event |
| Product Configuration | Product Catalog | Eligibility, Premium | API + Event |
| Product Version | Product Catalog | All services | API |

### Data Quality Controls

| Control | Implementation | Status |
|---------|---------------|--------|
| Data validation at entry | API validation + database constraints | ✅ Implemented |
| Duplicate detection | Unique constraints on Company Code, Product Code | ✅ Implemented |
| Data standardization | Standard formats untuk codes dan enums | ✅ Implemented |
| Data quality audits | Quarterly data quality reviews | 🔄 Planned |
| Data cleansing | Automated data quality checks | 🔄 Planned |

## 6.2 Data Retention Policy

### Retention Schedule

| Data Type | Retention Period | Legal Basis | Disposal Method |
|-----------|-----------------|-------------|-----------------|
| Product Information | Permanent (archived) | Business requirement | Archival |
| Product Version History | 10 years | OJK regulation | Archival then deletion |
| Audit Trail | 7 years | UU PDP, OJK | Archival then secure deletion |
| Configuration History | 10 years | OJK regulation | Archival then deletion |

### Retention Management

* Automated archival processes untuk old versions
* Secure deletion dengan verification
* Legal hold untuk ongoing investigations
* Retention policy enforcement automation

---

# 7. Risk Management

## 7.1 Product Catalog Specific Risks

| Risk Category | Risk | Likelihood | Impact | Mitigation | Owner |
|---------------|------|-----------|--------|-----------|-------|
| **Availability** | Service downtime | Medium | High | HA architecture, Redis cache, monitoring | SRE |
| **Data Integrity** | Product data corruption | Low | High | Backup, replication, checksums | Engineering |
| **Security** | Unauthorized product changes | Low | High | RBAC, audit trail, approval workflow | Security |
| **Compliance** | Missing audit trail | Low | Critical | Comprehensive logging, immutable storage | Compliance |
| **Performance** | Slow response during peak | Medium | Medium | Caching, auto-scaling, CDN | Engineering |
| **Change Risk** | Incorrect product configuration | Medium | High | Approval workflow, testing, validation | Product |

## 7.2 Risk Assessment Matrix

| Risk | Mitigation Status | Monitoring |
|------|------------------|------------|
| Service downtime | 🔄 Planned | Metrics dashboard |
| Data corruption | 🔄 Planned | Integrity checks |
| Unauthorized changes | 🔄 Planned | Audit trail review |
| Missing audit trail | 🔄 Planned | Compliance dashboard |
| Performance degradation | 🔄 Planned | APM monitoring |
| Incorrect configuration | 🔄 Planned | Validation rules |

---

# 8. Compliance Checklist

## Pre-Production Checklist

### Security
- [ ] Data encryption at rest implemented
- [ ] TLS 1.3 enabled untuk all communications
- [ ] OAuth 2.0 / JWT authentication configured
- [ ] RBAC authorization implemented
- [ ] Input validation configured
- [ ] SQL injection prevention verified
- [ ] XSS prevention implemented
- [ ] CSRF protection enabled
- [ ] Rate limiting configured
- [ ] Security headers configured

### Audit & Compliance
- [ ] Audit trail operational
- [ ] Audit log format compliant
- [ ] Audit log retention configured (7 years)
- [ ] Audit log protection implemented
- [ ] Compliance monitoring enabled

### Data Protection
- [ ] Data classification applied
- [ ] PII data identified and protected
- [ ] Data retention policy configured
- [ ] Secure deletion procedures implemented
- [ ] Backup encryption enabled

### Business Continuity
- [ ] High availability configured (≥ 99.9%)
- [ ] Backup procedures tested
- [ ] Disaster recovery plan documented
- [ ] RTO/RPO defined (2h/1h)
- [ ] Failover tested

### Monitoring & Detection
- [ ] Security monitoring enabled
- [ ] SIEM integration configured
- [ ] Anomaly detection enabled
- [ ] Alerting configured
- [ ] Dashboard created

## Production Readiness

- [ ] All P0 compliance requirements implemented
- [ ] All P1 compliance requirements implemented
- [ ] Security testing completed
- [ ] Penetration testing passed
- [ ] Compliance review completed
- [ ] Security training completed
- [ ] Documentation approved
- [ ] Sign-off from Security Team
- [ ] Sign-off from Compliance Team
- [ ] Sign-off from Enterprise Architecture

---

# 9. Regulatory Reporting

## 9.1 Product Catalog Specific Reports

| Report | Regulation | Frequency | Deadline | Owner |
|--------|------------|-----------|----------|-------|
| Product Change Report | OJK | Monthly | End of month | Product Team |
| Product Version History | OJK | Quarterly | End of quarter | Product Team |
| Access Review Report | ISO 27001 | Quarterly | End of quarter | Security Team |
| Configuration Change Audit | POJK 13/2017 | Monthly | End of month | Engineering Team |
| Data Quality Report | ISO 9001 | Quarterly | End of quarter | Product Team |

## 9.2 Incident Reporting

### Incident Classification for Product Catalog

| Severity | Description | Response Time | Reporting Requirement |
|----------|-------------|---------------|----------------------|
| **P1 - Critical** | Service down, data breach, unauthorized product changes | 15 minutes | Immediate to C-level, Security Team |
| **P2 - High** | Major functionality impaired, data integrity issue | 30 minutes | Engineering Manager, Security Team |
| **P3 - Medium** | Minor functionality affected, performance degradation | 2 hours | Engineering Team |
| **P4 - Low** | Cosmetic issue, no data impact | 8 hours | Standard tracking |

### Incident Report Template

```markdown
# Product Catalog Incident Report

## Incident Information
- Incident ID:
- Date/Time:
- Severity:
- Reported by:
- Affected Systems:

## Description
- What happened:
- Products affected:
- Configuration affected:
- Data affected:

## Impact Assessment
- Business impact:
- Customer impact:
- Regulatory impact:

## Root Cause
- Root cause analysis:
- Contributing factors:

## Response Actions
- Containment:
- Investigation:
- Remediation:
- Recovery:

## Timeline
- Detection:
- Response:
- Resolution:

## Corrective Actions
- Immediate fixes:
- Preventive measures:
- Process improvements:

## Compliance Impact
- Regulatory reporting required:
- Audit trail review:
- Evidence preservation:
```

---

# 10. Compliance References

## 10.1 Regulatory Documents

1. **UU No. 27 Tahun 2022** - Perlindungan Data Pribadi
   - Chapter 2: Principles of Data Protection
   - Chapter 3: Rights of Data Subjects
   - Chapter 4: Obligations of Data Controllers

2. **POJK No. 13/2017** - Penggunaan TI
   - Section 4: IT Risk Management
   - Section 5: Audit Trail
   - Section 6: Business Continuity
   - Section 7: Incident Management

3. **POJK No. 69/2016** - Perusahaan Asuransi
   - Section 5: Data Security
   - Section 6: Policy Management

## 10.2 Technical Standards

1. **ISO/IEC 27001:2022** - ISMS
   - Annex A: Security controls
   - A.8: Asset Management
   - A.9: Access Control
   - A.10: Cryptography
   - A.12: Operations Security
   - A.14: System Acquisition
   - A.16: Incident Management
   - A.17: Business Continuity

2. **ISO/IEC 22301:2019** - BCMS
   - Business continuity policy
   - Business impact analysis
   - Business continuity plans
   - Testing and exercises

3. **ISO 31000:2018** - Risk Management
   - Risk assessment framework
   - Risk treatment
   - Monitoring and review

## 10.3 Internal Documentation

* [Enterprise Standards & Compliance Framework](../docs/16. ENTERPRISE_STANDARDS.md)
* [Compliance Implementation Matrix](../docs/18. COMPLIANCE_MATRIX.md)
* [Product Catalog FSD](FSD_PRODUCT_CATALOG/FSD_PRODUCT_CATALOG.md)
* [Product Catalog TSD](TSD_PRODUCT_CATALOG/TSD_PRODUCT_CATALOG.md)

---

# 11. Compliance Contacts

| Role | Responsibility | Contact |
|------|---------------|---------|
| **CISO** | Security policy, incident response | security@example.com |
| **DPO** | Data protection, privacy compliance | dpo@example.com |
| **CRO** | Risk management, regulatory compliance | risk@example.com |
| **CTO** | Technology governance, BCP | cto@example.com |
| **Compliance Officer** | Regulatory reporting, audit coordination | compliance@example.com |
| **Product Catalog Team** | Product Catalog compliance implementation | product-catalog@example.com |

---

**Document Approval**

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Compliance Officer | | | |
| CISO | | | |
| CRO | | | |
| CTO | | | |

---

**Last Updated**: 04 August 2026  
**Next Review**: 04 November 2026  
**Owner**: Compliance Team