# Contributing to Pulse Engine

Terima kasih atas minat Anda untuk berkontribusi pada Pulse Engine. Dokumen ini memberikan pedoman dan aturan untuk berkontribusi pada project ini.

---

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Testing Requirements](#testing-requirements)
- [Documentation](#documentation)
- [Pull Request Process](#pull-request-process)
- [Commit Convention](#commit-convention)
- [Branching Strategy](#branching-strategy)
- [Code Review](#code-review)
- [Community](#community)

---

## Code of Conduct

Dengan berpartisipasi dalam project ini, Anda diharapkan untuk mematuhi Code of Conduct kami:

- Bersikap profesional dan menghormati sesama contributor
- Memberikan feedback yang konstruktif
- Menerima kritik dengan baik
- Fokus pada yang terbaik untuk project
- Menunjukkan empati terhadap anggota community

---

## Getting Started

### 1. Fork Repository

Fork repository Pulse Engine ke akun GitHub Anda.

### 2. Clone Repository

```bash
git clone https://github.com/<your-username>/pulse-engine.git
cd pulse-engine
```

### 3. Add Upstream Remote

```bash
git remote add upstream https://github.com/irsyadjpp/pulse-engine.git
```

### 4. Sync dengan Upstream

```bash
git fetch upstream
git rebase upstream/main
```

---

## Development Setup

### Prerequisites

- **Java 17+** (Orchestrator & Engine) / **Java 25 LTS** (Spring Boot services)
- **Maven 3.9+**
- **Docker & Docker Compose**
- **Git**
- **IDE**: IntelliJ IDEA / Eclipse / VS Code

### Setup Steps

#### 1. Start Infrastructure

```bash
docker compose up -d
```

Services yang dijalankan:
- Kafka: `localhost:7000`
- PostgreSQL: `localhost:7002`
- Redis: `localhost:7001`
- Kafdrop UI: `http://localhost:9000`
- pgAdmin: `http://localhost:5050`

#### 2. Build Project

```bash
mvn clean install -DskipTests
```

#### 3. Setup IDE

Import sebagai Maven project dan pastikan:
- Java SDK terinstal sesuai requirement
- Lombok plugin aktif
- Maven wrapper digunakan

---

## Development Workflow

### 1. Create Branch

```bash
git checkout -b feature/your-feature-name
```

### 2. Make Changes

- Implementasikan fitur atau bug fix
- Follow coding standards
- Add tests untuk changes Anda
- Update dokumentasi jika diperlukan

### 3. Run Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify

# Test dengan coverage
mvn clean test jacoco:report
```

### 4. Commit Changes

```bash
git add .
git commit -m "feat(scope): your descriptive message"
```

### 5. Push Changes

```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

Buat Pull Request di GitHub dengan:
- Clear title dan description
- Reference ke issue number (jika ada)
- Checklist untuk testing dan documentation

---

## Coding Standards

### Java Code Style

- Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use 4 spaces untuk indentation (no tabs)
- Maximum line length: 120 characters
- Use meaningful variable dan method names
- Write JavaDoc untuk public APIs

### Package Structure

```
com.irsyad.pulse.[service].[layer]
```

Contoh:
```
com.irsyad.pulse.product.api
com.irsyad.pulse.product.domain
com.irsyad.pulse.product.application
```

### Naming Conventions

| Type | Convention | Example |
| ---- | ---------- | ------- |
| Class | PascalCase | `ProductCatalogService` |
| Method | camelCase | `createProduct()` |
| Variable | camelCase | `productCode` |
| Constant | UPPER_SNAKE_CASE | `MAX_RETRY_ATTEMPTS` |
| Package | lowercase | `com.irsyad.pulse` |
| Interface | PascalCase + prefix | `IProductRepository` |

### Architecture Principles

- **Domain-Driven Design**: Business logic berada di domain layer
- **Hexagonal Architecture**: Separation between domain dan infrastructure
- **SOLID Principles**: Single responsibility, Open/closed, Liskov substitution, Interface segregation, Dependency inversion
- **Immutability**: Use immutable objects dimana memungkinkan
- **Dependency Injection**: Constructor injection preferred

### Best Practices

- Write small, focused methods (single responsibility)
- Use interfaces untuk abstraksi
- Avoid magic numbers - use constants
- Handle exceptions properly (don't swallow exceptions)
- Use meaningful exception messages
- Log appropriately (INFO, WARN, ERROR)
- Avoid duplicate code (DRY principle)

---

## Testing Requirements

### Test Coverage

- Minimum **80% code coverage** untuk new code
- All business logic harus memiliki unit tests
- Critical paths harus memiliki integration tests

### Test Structure

```
src/test/java/
├── [service]/
│   ├── unit/                    # Unit tests
│   │   ├── domain/
│   │   ├── application/
│   │   └── infrastructure/
│   └── integration/             # Integration tests
│       ├── [Feature]IT.java
│       └── [Component]IT.java
```

### Testing Guidelines

- Use descriptive test method names
- Follow Arrange-Act-Assert (AAA) pattern
- Use meaningful test data
- Mock external dependencies
- Test edge cases dan error scenarios
- Avoid test interdependencies

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductCatalogServiceTest

# Run with coverage report
mvn clean test jacoco:report

# Skip tests
mvn install -DskipTests
```

---

## Documentation

### Documentation Requirements

Semua perubahan harus didokumentasikan dengan baik:

#### 1. Code Documentation

- JavaDoc untuk public classes dan methods
- Inline comments untuk complex logic
- README updates untuk significant changes

#### 2. Business Documentation

- Update BRD jika ada perubahan requirements
- Update FSD jika ada perubahan functional behavior
- Update TSD jika ada perubahan technical design

#### 3. API Documentation

- Update OpenAPI/Swagger annotations
- Provide examples untuk new endpoints
- Document request/response formats

### Documentation Structure

```
docs/
├── [XX]. [DOCUMENT_TYPE].md
apps/[service]/docs/
├── [SERVICE]_FSD/
└── [SERVICE]_TSD/
```

---

## Pull Request Process

### Before Submitting PR

- [ ] Code follows style guidelines
- [ ] All tests pass locally
- [ ] Code coverage >= 80%
- [ ] Documentation updated
- [ ] Commit messages follow convention
- [ ] No merge conflicts
- [ ] Self-review completed

### PR Description Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Related Issues
Closes #123
Related to #456

## Testing
Describe tests that you ran to verify your changes

## Checklist
- [ ] My code follows the style guidelines
- [ ] I have performed a self-review
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] Any dependent changes have been merged and published
```

### Review Process

1. **Automated Checks**: CI/CD pipeline runs tests, linting, coverage
2. **Peer Review**: Minimum 1 approval dari maintainer
3. **Architecture Review**: For significant changes
4. **Security Review**: For security-related changes
5. **Merge**: Squash and merge ke branch target

---

## Commit Convention

Kami menggunakan [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
type(scope): description

[optional body]

[optional footer(s)]
```

### Types

| Type | Description |
| ---- | ----------- |
| **feat** | New feature |
| **fix** | Bug fix |
| **docs** | Documentation changes |
| **refactor** | Code refactoring (no feature changes) |
| **test** | Test additions or changes |
| **chore** | Maintenance tasks (dependencies, build, etc.) |
| **perf** | Performance improvements |
| **style** | Code style changes (formatting, missing semicolons, etc.) |
| **ci** | CI/CD changes |

### Scopes

| Scope | Description |
| ----- | ----------- |
| **product** | Product Catalog Service |
| **orchestrator** | Orchestrator Service |
| **engine** | Intelligence Engine |
| **docs** | Documentation |
| **infra** | Infrastructure |
| **test** | Testing |

### Examples

```bash
feat(product): add product versioning support

- Implement product versioning dengan immutable snapshots
- Add API endpoints untuk version management
- Update database schema dengan version table

Closes #123

---

fix(engine): resolve memory leak in pipeline

- Fix object pooling in enrichment service
- Add proper cleanup in pipeline execution

Fixes #456

---

docs(README): update quick start guide

- Add instructions untuk running individual services
- Update API documentation links
```

---

## Branching Strategy

Kami menggunakan [Git Flow](https://nvie.com/posts/a-successful-git-branching-model/) branching model:

### Branch Types

| Branch | Purpose | Protected |
| ------ | ------- | --------- |
| `main` | Production-ready code | Yes |
| `develop` | Integration branch | Yes |
| `feature/*` | New features | No |
| `bugfix/*` | Bug fixes | No |
| `release/*` | Release preparation | No |
| `hotfix/*` | Critical production fixes | No |

### Branch Naming

```bash
feature/PRO-123-add-product-search
bugfix/PRO-456-fix-eligibility-calculation
release/v1.2.0
hotfix/PRO-789-critical-security-patch
```

### Workflow

```bash
# Start new feature
git checkout -b feature/PRO-123-add-product-search develop

# Work on feature
git add .
git commit -m "feat(product): implement product search API"

# Keep updated with develop
git fetch upstream
git rebase upstream/develop

# Push dan create PR
git push origin feature/PRO-123-add-product-search
# Create PR ke develop branch
```

---

## Code Review

### Reviewer Guidelines

- Review untuk functionality, security, performance
- Check code quality dan adherence to standards
- Verify tests coverage
- Ensure documentation updated
- Provide constructive feedback

### Author Guidelines

- Respond to comments promptly
- Make requested changes
- Update PR description dengan resolution notes
- Squash commits before merge (if requested)

### Review Checklist

- [ ] Code functionality correct
- [ ] Security considerations addressed
- [ ] Performance acceptable
- [ ] Tests added/updated
- [ ] Documentation updated
- [ ] No unnecessary changes
- [ ] Commit messages clear

---

## Community

### Communication Channels

- **GitHub Issues**: Bug reports dan feature requests
- **GitHub Discussions**: Questions dan community discussions
- **Pull Requests**: Code reviews dan collaboration

### Getting Help

- Check [Documentation Index](docs/17. DOCUMENTATION_INDEX.md)
- Search existing [GitHub Issues](https://github.com/irsyadjpp/pulse-engine/issues)
- Create new issue dengan detailed description

### Reporting Bugs

Buat GitHub issue dengan:
- Clear title dan description
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)
- Logs atau error messages

### Suggesting Features

Buat GitHub issue dengan:
- Clear use case
- Proposed solution
- Alternatives considered
- Impact assessment

---

## Recognition

Contributors akan diakui dalam:
- Release notes untuk significant contributions
- Contributors section di README
- Project documentation

---

## Questions?

Jika Anda memiliki pertanyaan regarding contributing:
- Buat [GitHub Discussion](https://github.com/irsyadjpp/pulse-engine/discussions)
- Contact: architecture@example.com

---

**Happy Contributing! 🚀**

Terima kasih atas kontribusi Anda dalam membuat Pulse Engine lebih baik.