# Changelog

Semua perubahan penting pada Pulse Engine akan didokumentasikan dalam file ini.

Format ini didasarkan pada [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
dan project ini mengikuti [Semantic Versioning](https://semver.org/lang/id/).

---

## [Unreleased]

### Added
- Enterprise Standards & Compliance Framework documentation
  - UU PDP compliance requirements
  - OJK regulations alignment
  - ISO 27001, 22301, 9001, 31000 standards
  - ITIL 4 framework
  - Data governance framework
  - Risk management framework
  - Audit & compliance framework
- Documentation Index & Navigation guide
- Comprehensive BRD for all services
- FSD and TSD structure for Product Catalog Service

### Changed
- Updated README.md with complete documentation structure
- Aligned documentation with enterprise standards

### Security
- Added SECURITY.md dengan security policy dan vulnerability reporting

---

## [1.0.0] - 2026-08-04

### Added

#### Business Architecture Documentation
- Product Vision dengan mission, goals, dan principles
- Business Architecture dengan domain dan capability map
- Domain Model dengan bounded contexts dan aggregates
- Capability Map dengan business capabilities hierarchy
- System Context dengan actors dan external systems
- Container Architecture dengan high-level components
- Event Storming dengan domain events dan commands

#### Business Requirements Documents (BRD)
- Product Catalog BRD
- Quote BRD
- Eligibility BRD
- Premium BRD
- Proposal BRD
- Checkout BRD
- Payment BRD
- Policy BRD
- Notification BRD

#### Technical Specifications (Product Catalog Service)
- FSD_PRODUCT_CATALOG.md - Functional Specification overview
- TSD_PRODUCT_CATALOG.md - Technical Specification overview
- 10 detailed FSD modules
- 19 detailed TSD aspects

#### Intelligence Engine (Quarkus)
- BPMN Orchestrator untuk checkout workflow
- DMN Decision Engine untuk risk assessment
- 7-capability pipeline (Observe, Understand, Explain, Decide, Learn, Persist, Publish)
- Kafka integration untuk event-driven architecture
- PostgreSQL persistence dengan Flyway migrations
- Customer learning dan insight generation

#### Infrastructure
- Docker Compose configuration
- Kafka cluster setup
- PostgreSQL dengan schemas
- Redis untuk caching
- Kafdrop UI untuk Kafka monitoring
- pgAdmin untuk database management

#### Documentation
- README.md dengan project overview
- CONTRIBUTING.md dengan contribution guidelines
- SECURITY.md dengan security policy
- Enterprise Standards & Compliance Framework
- Documentation Index

### Technical Details

#### Technology Stack
- Java 25 LTS (Spring Boot services)
- Java 17 (Quarkus services)
- Spring Boot 4.0.7
- Quarkus 3.x dengan Kogito
- Apache Kafka untuk messaging
- PostgreSQL 16+ untuk persistence
- Redis 7+ untuk caching
- OAuth2 + JWT untuk security
- OpenAPI 3.1 untuk API documentation

#### Architecture Patterns
- Domain-Driven Design (DDD)
- Hexagonal Architecture
- Clean Architecture
- Event-Driven Architecture
- Microservices
- CQRS (untuk specific use cases)
- BPMN untuk workflow orchestration
- DMN untuk decision automation

#### Key Features
- Product Catalog Management dengan versioning
- Quote dan Proposal generation
- Eligibility assessment
- Premium calculation
- Checkout processing
- Payment integration
- Policy issuance
- Intelligent underwriting dengan explainability
- Comprehensive audit trail
- Distributed tracing
- Real-time monitoring

---

## [0.1.0] - 2026-07-15

### Added
- Initial project structure
- Basic README.md
- Docker Compose untuk infrastructure
- Kafka topics configuration
- Database schemas (orchestrator dan pulse_engine)
- Basic BPMN process untuk checkout
- DMN decision table untuk risk assessment
- Proof of concept untuk Intelligence Engine

---

## Versioning Policy

Project ini mengikuti [Semantic Versioning](https://semver.org/lang/id/):

- **MAJOR** version untuk incompatible API changes
- **MINOR** version untuk functionality additions dalam backward-compatible manner
- **PATCH** version untuk backward-compatible bug fixes

### Version Examples

- `1.0.0` - Initial stable release
- `1.1.0` - Add new features (backward compatible)
- `1.1.1` - Bug fixes
- `2.0.0` - Breaking changes

---

## Release History

| Version | Date | Description |
|---------|------|-------------|
| 1.0.0 | 2026-08-04 | Initial enterprise release dengan complete documentation |
| 0.1.0 | 2026-07-15 | Initial proof of concept |

---

## Migration Guides

### From 0.x to 1.0

Jika Anda menggunakan version 0.x, please ikuti migration guide:

1. Update dependencies ke versions terbaru
2. Review breaking changes dalam API
3. Update configuration files
4. Test dalam staging environment sebelum production

---

## Support

Untuk pertanyaan regarding specific versions:
- Check [Documentation Index](docs/17. DOCUMENTATION_INDEX.md)
- Create [GitHub Issue](https://github.com/irsyadjpp/pulse-engine/issues)
- Contact: architecture@example.com

---

**Note**: Versi 1.0.0 merupakan initial enterprise release dengan documentation lengkap untuk compliance dengan regulasi Indonesia dan standar internasional.