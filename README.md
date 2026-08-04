# Pulse Engine

**Enterprise Insurance Marketplace Platform with Intelligent Underwriting**

[![Java](https://img.shields.io/badge/Java-25%20LTS-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-green)](https://spring.io/projects/spring-boot)
[![Quarkus](https://img.shields.io/badge/Quarkus-3.x-red)](https://quarkus.io/)
[![Kafka](https://img.shields.io/badge/Kafka-Event%20Driven-black)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-blue)](https://www.postgresql.org/)

Pulse Engine adalah platform enterprise untuk marketplace asuransi yang menggabungkan arsitektur modern berbasis Java dengan intelligent underwriting capabilities. Platform ini terdiri dari dua komponen utama:

- **Product Catalog Services**: Microservices berbasis Spring Boot untuk manajemen produk, quote, eligibility, dan pembayaran
- **Intelligence Engine**: Layanan event-driven berbasis Quarkus untuk underwriting intelligence dengan BPMN/DMN orchestration

---

## Architecture Overview

```
Pulse Engine Platform
├── Product Catalog Ecosystem (Spring Boot)
│   ├── Customer Domain (Customer, Beneficiary, Identity)
│   ├── Product Domain (Product Catalog, Eligibility, Premium, Recommendation)
│   ├── Sales Domain (Quote, Proposal, Checkout)
│   ├── Payment Domain (Payment, Verification, Reconciliation)
│   ├── Policy Domain (Policy Issuance, Management, Document)
│   └── Platform Domain (Notification, Workflow, Audit, Reporting, IAM)
│
└── Intelligence Engine (Quarkus)
    ├── Orchestrator (BPMN + DMN)
    │   ├── Checkout Workflow (BPMN)
    │   ├── Risk Assessment (DMN)
    │   └── External Service Integrations
    └── Processing Engine (7 Capabilities)
        ├── Observe - Normalize Event
        ├── Understand - Enrichment
        ├── Explain - DRG Reasoning
        ├── Decide - Rules Engine
        ├── Learn - Customer Learning
        ├── Persist - Database
        └── Publish - Kafka
```

---

## Key Features

### 🏢 Enterprise Architecture
- **Microservices**: Domain-driven design dengan separation of concerns
- **Event-Driven**: Apache Kafka untuk asynchronous communication
- **Workflow Orchestration**: BPMN 2.0 untuk business process management
- **Decision Engine**: DMN untuk business rules dan underwriting decisions
- **Cloud-Native**: Docker, Kubernetes, observability built-in

### 🔒 Compliance & Standards
- **UU PDP Compliance**: Undang-Undang Perlindungan Data Pribadi No. 27/2022
- **OJK Regulations**: POJK No. 13/2017, 69/2016 untuk industri asuransi
- **ISO 27001**: Information Security Management System
- **ISO 22301**: Business Continuity Management System
- **ITIL 4**: IT Service Management framework
- **PCI DSS**: Payment card security (jika applicable)

### 🎯 Business Capabilities
- **Product Catalog Management**: Multi-insurer product management dengan versioning
- **Quote & Proposal**: Real-time premium calculation dan proposal generation
- **Eligibility Assessment**: Automated customer eligibility validation
- **Payment Processing**: Integrated payment gateway dengan reconciliation
- **Policy Issuance**: Automated policy generation dan activation
- **Intelligent Underwriting**: AI-powered risk assessment dengan explainability

### 📊 Observability & Monitoring
- **Distributed Tracing**: OpenTelemetry untuk end-to-end tracing
- **Metrics**: Prometheus + Grafana dashboards
- **Logging**: Structured logging dengan Loki
- **Health Checks**: Comprehensive health monitoring
- **Audit Trail**: Immutable audit logs untuk compliance

---

## Technology Stack

### Backend Services
| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 25 LTS / 17 |
| **Framework** | Spring Boot | 4.0.7 |
| **Framework** | Quarkus | 3.x |
| **Workflow** | Kogito (BPMN + DMN) | Latest |
| **Messaging** | Apache Kafka | Latest |
| **Database** | PostgreSQL | 16+ |
| **Cache** | Redis | 7+ |
| **Security** | OAuth2 + JWT | RFC 6749/7519 |
| **API Doc** | OpenAPI 3.1 | - |

### Infrastructure
| Component | Technology |
|-----------|-----------|
| **Container** | Docker |
| **Orchestration** | Kubernetes 1.30+ |
| **Build** | Maven |
| **Monitoring** | Prometheus + Grafana |
| **Tracing** | OpenTelemetry |
| **Logs** | Loki |
| **Migration** | Flyway |

---

## Project Structure

```
pulse-engine/
├── apps/
│   ├── product/                    # Product Catalog Services (Spring Boot)
│   │   ├── src/main/java/com/irsyad/pulse/product/
│   │   │   ├── api/                # REST Controllers
│   │   │   ├── application/        # Application Services
│   │   │   ├── domain/             # Domain Layer (Entities, Services)
│   │   │   ├── infrastructure/     # Infrastructure Layer
│   │   │   └── shared/             # Shared DTOs, Events
│   │   ├── src/main/resources/
│   │   │   ├── db/migration/       # Flyway migrations
│   │   │   └── application.yaml
│   │   └── docs/
│   │       ├── FSD_PRODUCT_CATALOG/    # Functional Specifications
│   │       └── TSD_PRODUCT_CATALOG/    # Technical Specifications
│   │
│   ├── orchestrator/               # BPMN Orchestrator (Quarkus)
│   │   ├── src/main/java/com/irsyad/pulse/orchestrator/
│   │   │   ├── api/                # REST endpoints
│   │   │   ├── process/            # BPMN processes
│   │   │   ├── integration/        # External service clients
│   │   │   └── messaging/          # Kafka producers/consumers
│   │   ├── src/main/resources/
│   │   │   ├── processes/          # BPMN files
│   │   │   ├── decisions/          # DMN files
│   │   │   └── application.properties
│   │
│   └── engine/                     # Intelligence Engine (Quarkus)
│       ├── src/main/java/com/irsyad/pulse/engine/
│       │   ├── api/                # REST endpoints
│       │   ├── pipeline/           # 7-capability pipeline
│       │   ├── service/            # Business services
│       │   ├── kafka/              # Kafka consumers/producers
│       │   └── persistence/        # Repository layer
│       └── src/main/resources/
│           ├── application.properties
│
├── docker/                         # Docker compose files
├── mappings/                       # API mappings (OpenAPI)
├── postman/                        # Postman collections
├── docs/                           # Enterprise Documentation
│   ├── 00. PRODUCT_VISION.md
│   ├── 01. BUSINESS_ARCHITECTURE.md
│   ├── 02. DOMAIN_MODEL.md
│   ├── 03. CAPABILITY_MAP.md
│   ├── 04. SYSTEM_CONTEXT.md
│   ├── 05. CONTAINER_ARCHITECTURE.md
│   ├── 06. EVENT_STORMING.md
│   ├── 07-15. [SERVICE]_BRD.md     # Business Requirements Documents
│   ├── 16. ENTERPRISE_STANDARDS.md # Compliance, ITSM, ISO, OJK, UU PDP
│   └── 17. DOCUMENTATION_INDEX.md
│
└── README.md
```

---

## Quick Start

### Prerequisites

- **Java 17+** (for Orchestrator & Engine) / **Java 25** (for Spring Boot services)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **Kafka** (or use Docker Compose)

### 1. Clone Repository

```bash
git clone https://github.com/irsyadjpp/pulse-engine.git
cd pulse-engine
```

### 2. Start Infrastructure

```bash
docker compose up -d
```

Services yang berjalan:
- Kafka: `localhost:7000`
- PostgreSQL: `localhost:7002`
- Redis: `localhost:7001`
- Kafdrop UI: `http://localhost:9000`
- pgAdmin: `http://localhost:5050`

### 3. Build All Services

```bash
mvn clean install -DskipTests
```

### 4. Run Product Catalog Service (Spring Boot)

```bash
cd apps/product
mvn spring-boot:run
```

Service berjalan di: `http://localhost:8080`

### 5. Run Orchestrator (Quarkus BPMN)

```bash
cd apps/orchestrator
mvn quarkus:dev
```

Service berjalan di: `http://localhost:7021`

### 6. Run Engine (Quarkus Intelligence)

```bash
cd apps/engine
mvn quarkus:dev
```

Service berjalan di: `http://localhost:7020`

---

## API Documentation

### Product Catalog Service
- **OpenAPI Docs**: http://localhost:8080/swagger-ui.html
- **Health**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/prometheus

### Orchestrator
- **OpenAPI Docs**: http://localhost:7021/q/swagger-ui
- **Health**: http://localhost:7021/q/health
- **Processes**: http://localhost:7021/api/v1/processes

### Intelligence Engine
- **OpenAPI Docs**: http://localhost:7020/q/swagger-ui
- **Health**: http://localhost:7020/q/health
- **Metrics**: http://localhost:7020/q/metrics

---

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### Test Coverage
```bash
mvn clean test jacoco:report
# Report: target/site/jacoco/index.html
```

---

## Documentation

Pulse Engine memiliki dokumentasi enterprise yang komprehensif, mencakup arsitektur bisnis, requirements, specifications, dan compliance frameworks.

### Documentation Structure

```
docs/
├── 00. PRODUCT_VISION.md                 # Vision, Mission, Goals, Principles
├── 01. BUSINESS_ARCHITECTURE.md          # Business domains & capabilities
├── 02. DOMAIN_MODEL.md                   # Bounded contexts & aggregates
├── 03. CAPABILITY_MAP.md                 # Business capabilities hierarchy
├── 04. SYSTEM_CONTEXT.md                 # System boundary & external systems
├── 05. CONTAINER_ARCHITECTURE.md         # High-level containers
├── 06. EVENT_STORMING.md                 # Domain events & commands
├── 07-15. [SERVICE]_BRD.md               # Business Requirements Documents
├── 16. ENTERPRISE_STANDARDS.md           # Compliance & standards framework
└── 17. DOCUMENTATION_INDEX.md            # Complete documentation guide

apps/[service]/docs/
├── [SERVICE]_FSD/                         # Functional Specifications
│   ├── FSD_PRODUCT_CATALOG.md            # Overview
│   ├── FSD_01_[MODULE]_MANAGEMENT.md     # Detailed specs per module
│   └── ...
└── [SERVICE]_TSD/                         # Technical Specifications
    ├── TSD_PRODUCT_CATALOG.md            # Overview
    ├── TSD_01_ARCHITECTURE.md            # Technical architecture
    ├── TSD_02_DOMAIN_MODEL.md            # Domain implementation
    ├── TSD_03_DATABASE.md                # Database design
    ├── TSD_04_API.md                     # API specification
    └── ...
```

### Key Documentation

**Business Layer:**
- Product Vision - Strategic direction dan principles
- Business Architecture - Domain-driven business design
- Domain Model - Bounded contexts dan ubiquitous language
- Capability Map - Business capabilities dan ownership
- System Context - System boundary dan integrations
- Container Architecture - High-level system components
- Event Storming - Domain events dan workflows

**Requirements Layer:**
- BRD (Business Requirements Document) per service
  - Product Catalog, Quote, Eligibility, Premium
  - Proposal, Checkout, Payment, Policy, Notification

**Standards & Compliance:**
- **Enterprise Standards & Compliance Framework** meliputi:
  - UU PDP (Data Protection)
  - OJK Regulations (Insurance)
  - BSSN Regulations (Cybersecurity)
  - ISO 27001 (Information Security)
  - ISO 22301 (Business Continuity)
  - ISO 9001 (Quality Management)
  - ISO 31000 (Risk Management)
  - PCI DSS (Payment Security)
  - NIST Cybersecurity Framework
  - ITIL 4 (IT Service Management)

**Technical Specifications:**
- Technical Architecture (Hexagonal, DDD, Clean Architecture)
- Domain Model Implementation
- Database Design (ERD, Flyway, SQL DDL)
- API Specification (OpenAPI 3.1)
- Business Rule Implementation
- Workflow & Sequence Design
- Versioning Strategy
- Cache Strategy (Redis)
- Security Design
- Error Handling
- Logging Specification
- Observability (OpenTelemetry, Prometheus, Grafana)
- Performance Design
- Integration Design
- Configuration Management
- Deployment Architecture (Docker, Kubernetes)
- Testing Strategy
- NFR Mapping
- Traceability Matrix

Lihat [Documentation Index](docs/17. DOCUMENTATION_INDEX.md) untuk navigasi lengkap.

---

## Development

### Code Standards
- Follow Java coding conventions
- Use meaningful variable and method names
- Write unit tests untuk semua business logic
- Document public APIs dengan JavaDoc
- Follow DDD principles (Entities, Value Objects, Aggregates)

### Commit Convention
```
type(scope): description

Types:
- feat: New feature
- fix: Bug fix
- docs: Documentation changes
- refactor: Code refactoring
- test: Test additions or changes
- chore: Maintenance tasks
```

### Branch Strategy
- `main` - Production-ready code
- `develop` - Integration branch
- `feature/*` - New features
- `bugfix/*` - Bug fixes
- `release/*` - Release preparation

---

## Contributing

1. Fork repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

Pastikan:
- All tests pass
- Code coverage tidak menurun
- Documentation updated
- Commit message mengikuti convention

---

## Security

Untuk melaporkan security vulnerabilities, silakan hubungi security team melalui [SECURITY.md](SECURITY.md).

Pulse Engine berkomitmen untuk:
- Mengikuti best practices security (OWASP Top 10)
- Enkripsi data at rest dan in transit
- Regular security audits
- Vulnerability management
- Incident response plan

---

## Compliance

Pulse Engine dirancang untuk memenuhi:

- **UU No. 27 Tahun 2022** - Perlindungan Data Pribadi
- **POJK No. 13/2017** - Penggunaan TI dalam penyelenggaraan usaha jasa keuangan
- **POJK No. 69/2016** - Perusahaan asuransi
- **ISO/IEC 27001:2022** - Information Security Management System
- **ISO/IEC 22301:2019** - Business Continuity Management System
- **ITIL 4** - IT Service Management

Lihat [Enterprise Standards & Compliance Framework](docs/16. ENTERPRISE_STANDARDS.md) untuk detail lengkap.

---

## License

Proprietary - All rights reserved

---

## Support

Untuk pertanyaan atau support:
- **Documentation**: Lihat [docs/17. DOCUMENTATION_INDEX.md](docs/17. DOCUMENTATION_INDEX.md)
- **Issues**: Buat GitHub issue
- **Contact**: Enterprise Architecture Team

---

## Roadmap

### Phase 1: Foundation ✅
- [x] Business Architecture Documentation
- [x] Enterprise Standards & Compliance Framework
- [x] Documentation Structure
- [x] Product Catalog Service (Spring Boot)
- [x] Orchestrator (BPMN + DMN)
- [x] Intelligence Engine (7 Capabilities)

### Phase 2: Product Catalog Expansion 🔄
- [ ] Complete FSD/TSD for all services
- [ ] Customer Domain Services
- [ ] Payment Integration
- [ ] Policy Issuance
- [ ] Notification Service

### Phase 3: Advanced Features ⏳
- [ ] Health Insurance product support
- [ ] Travel Insurance product support
- [ ] Advanced analytics dan reporting
- [ ] Machine learning untuk fraud detection
- [ ] Multi-tenant architecture
- [ ] API Marketplace untuk partners

---

## Acknowledgments

Pulse Engine dibangun menggunakan teknologi dan framework terbaik:

- [Spring Boot](https://spring.io/projects/spring-boot) - Enterprise Java framework
- [Quarkus](https://quarkus.io/) - Kubernetes-native Java framework
- [Kogito](https://kogito.kie.org/) - Cloud-native business automation
- [Apache Kafka](https://kafka.apache.org/) - Distributed streaming platform
- [PostgreSQL](https://www.postgresql.org/) - Relational database
- [Redis](https://redis.io/) - In-memory data structure store
- [OpenTelemetry](https://opentelemetry.io/) - Observability framework

---

## Contact

**Pulse Engine Team**  
Enterprise Architecture Team  
Email: architecture@example.com  
GitHub: [@irsyadjpp](https://github.com/irsyadjpp)

---

**© 2026 Pulse Engine. All rights reserved.**

*Built with ❤️ using modern Java ecosystem*