# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

eHub is a Spring Boot 4.0.2 application providing REST services via Apache CXF and an admin UI via Apache Wicket. The project uses Java 17, Maven for builds, and Oracle database (with HSQLDB for testing).

**Key Technologies:**
- Spring Boot 4.0.2 with Spring Data JPA
- Apache CXF 4.2.0 (JAX-RS REST services)
- Apache Wicket 10.8.0 (Admin UI)
- Log4j2 (not Spring Boot's default logging)
- Lombok for code generation

## Build Commands

**Build the entire project:**
```bash
mvn clean install
```

**Run the main application:**
```bash
# Using Maven profile
mvn -pl local -Pserver

# Or directly with Java
java -jar local/target/ehub-local.jar
```

**Run the mock server:**
```bash
java -jar local-mock/target/ehub-local-mock.jar
```

**Run tests:**
```bash
# Unit tests (all modules)
mvn test

# Unit tests (specific module)
mvn test -pl <module-name>

# Integration tests (local module)
mvn verify -pl local -Pintegration-test

# Integration tests (local-mock module)
mvn verify -pl local-mock -Pintegration-test

# Run a single test class
mvn test -Dtest=ClassName

# Run a single test method
mvn test -Dtest=ClassName#methodName
```

## Architecture

This is a Maven multi-module project with clear separation of concerns:

**Module Dependencies:**
```
common (base layer)
  ↓
local-core (business logic/API implementation)
  ↓
local (Spring Boot application assembly + Wicket UI)

client (parallel, for consuming the API)
local-mock (parallel, mock server for testing)
test-data (test utilities)
```

**Module Responsibilities:**

1. **common** (`ehub-common`)
   - Domain models and JPA entities
   - Shared exceptions (`EhubRuntimeException`, `EhubWebApplicationException`, etc.)
   - REST API interfaces
   - Shared utilities and configuration
   - Produces a test-jar for use by other modules

2. **local-core** (`ehub-local-core`)
   - JAX-RS resource implementations (REST controllers)
   - Business logic and service layer
   - CXF server configuration
   - Exception mappers for REST API
   - Located in `com.axiell.ehub.core` package

3. **local** (`ehub-local`)
   - Main Spring Boot application (`com.axiell.ehub.local.EhubApplication`)
   - Apache Wicket UI components (panels, forms, pages)
   - JPA repositories
   - Database configuration
   - Executable JAR with embedded server
   - Component scanning: `com.axiell.ehub.common`, `com.axiell.ehub.core`, `com.axiell.ehub.local`

4. **client** (`ehub-client`)
   - JAX-RS client library for consuming eHub REST services
   - Used by tests and external consumers

5. **local-mock** (`ehub-local-mock`)
   - Mock implementation for testing (`com.axiell.ehub.local.EhubMockApplication`)
   - Uses HSQLDB in-memory database
   - Standalone executable

6. **test-data** (`ehub-test-data`)
   - Test data utilities and fixtures

## API Endpoints

When the application runs on default settings:

- **Base URL:** `http://localhost:16518/api/v5.0/`
- **OpenAPI JSON:** `http://localhost:16518/api/openapi.json`
- **Swagger UI:** `http://localhost:16518/api/api-docs?url=/api/openapi.json`
- **CXF Services:** `http://localhost:16518/api/services`
- **Actuator Metrics:** `http://localhost:16518/actuator/metrics`
- **CXF Metrics:** `http://localhost:16518/actuator/metrics/cxf.server.requests`

## Testing Conventions

- Unit test files: `*Test.java`
- Integration test files: `*IT.java` or `*IT_*.java`
- Integration tests run with the `integration-test` profile using Maven Failsafe plugin
- Common module provides test utilities via test-jar classifier

## Important Technical Details

**JDK Options:**
The project requires specific JVM options for module access (defined in `jdk.java.options` property). These are automatically applied when running via Maven profiles.

**Logging:**
- Uses Log4j2 (NOT Spring Boot's default Logback)
- All Spring Boot logging starters are explicitly excluded
- Configuration files: `log4j2.xml` and `log4j2-prod.xml` in common/src/main/resources

**Database:**
- Production: Oracle JDBC (ojdbc17, ucp17)
- Testing: HSQLDB (in-memory)
- JPA repositories are in the `local` module under `com.axiell.ehub.local`

**Component Scanning:**
The main application scans three package roots:
- `com.axiell.ehub.common`
- `com.axiell.ehub.core`
- `com.axiell.ehub.local`

## Common Development Workflows

**Adding a new REST endpoint:**
1. Define interface in `common` module (if needed)
2. Implement JAX-RS resource in `local-core` module under `com.axiell.ehub.core.controller`
3. Add service/business logic in `local-core`
4. Register in CXF configuration if not auto-discovered
5. Add integration tests in `local` module

**Adding a new Wicket UI component:**
1. Create panel/page in `local/src/main/java/com/axiell/ehub/local`
2. Place associated HTML template in same package (Wicket convention)
3. Wire with Spring beans as needed

**Working with database:**
1. JPA entities are in `common` and `local` modules
2. Repositories are in `local` module
3. Use HSQLDB for testing (automatically configured)

## Maven Profiles

- `server` - Run the application (local module only)
- `integration-test` - Run integration tests (local and local-mock modules)
- `native` - GraalVM native image compilation
- `nativeTest` - GraalVM native image testing
