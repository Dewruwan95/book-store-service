# Book Store REST Service

A fully functional **Book Store** REST API built with **Spring Boot 3.5.8** and **Java 21**, featuring CRUD operations for Books, Authors, Customers, and Purchases. The project includes **OpenAPI/Swagger UI**, **input validation**, **custom exception handling**, **logging**, **tests with JaCoCo coverage**, and optional **SonarQube** analysis.

> **Intentional flexibility**: The specification leaves room for assumptions. This implementation embraces sensible defaults (relationships, validations, lifecycle states) while keeping the code modular so you can extend it.

---

## Table of Contents
- [Prerequisites](#prerequisites)
- [Tech Stack](#tech-stack)
- [Architecture & Assumptions](#architecture--assumptions)
- [Entity Model & Relationships](#entity-model--relationships)
- [API Endpoints](#api-endpoints)
- [Validation & Errors](#validation--errors)
- [Security (Basic Auth, optional)](#security-basic-auth-optional)
- [OpenAPI/Swagger UI](#openapiswapper-ui)
- [Persistence & Profiles (H2/MySQL)](#persistence--profiles-h2mysql)
- [Run the Service](#run-the-service)
- [Build & Run JAR](#build--run-jar)
- [Docker (optional)](#docker-optional)
- [Testing & Coverage (JUnit, JaCoCo)](#testing--coverage-junit-jacoco)
- [Static Analysis (SonarQube)](#static-analysis-sonarqube)
- [Logging](#logging)
- [Project Structure](#project-structure)
- [Sample Requests](#sample-requests)
- [Troubleshooting](#troubleshooting)

---

## Prerequisites
- **Java 21** (JDK 21). Verify:
  ```bash
  java -version
  # openjdk version "21..."
  ```
- **Maven 3.9+**. Verify:
  ```bash
  mvn -v
  ```
- **Git** (optional)
- **Docker** (optional, for MySQL/SonarQube)

---

## Tech Stack
- **Spring Boot 3.5.8**
  - Spring Web (REST)
  - Spring Data JPA (persistence)
  - Spring Validation (jakarta.validation)
- **Database**: H2 (in-memory) by default; **MySQL** via profile
- **OpenAPI/Swagger**: `springdoc-openapi` for interactive API docs
- **Testing**: JUnit 5, Mockito
- **Code Coverage**: JaCoCo
- **Static Analysis**: SonarQube
- **Logging**: SLF4J + Logback
- **Mapping**: MapStruct (optional) or ModelMapper (optional)

---

## Architecture & Assumptions
- **Layered architecture**:
  - `controller` → REST endpoints (DTO I/O)
  - `service` → business logic, transactions
  - `repository` → Spring Data JPA
  - `model/entity` → JPA entities
  - `dto` → API contracts
  - `mapper` → DTO↔Entity conversions (MapStruct)
  - `exception` → custom exceptions & global handler
- **Common assumptions**:
  - A **Book** may exist **without** an Author initially.
  - An **Author** may exist **without** Books.
  - A **Customer** can purchase any available Book; purchases are immutable records.
  - **PurchasedBook** is a **join entity** holding `bookId`, `customerId`, `quantity`, `priceAtPurchase`, `purchasedAt`.
  - **Soft rules**: Book `price` must be non-negative; `quantity` ≥ 1; emails must be valid; names trimmed & length-checked.

---

## Entity Model & Relationships

### Entities
- **Author**
  - `id`, `firstName`, `lastName`, `bio` (optional), `createdAt`
- **Book**
  - `id`, `title`, `isbn` (unique), `description` (optional), `price`, `publishedAt`, `author` (nullable, Many-to-One)
- **Customer**
  - `id`, `firstName`, `lastName`, `email` (unique), `createdAt`
- **PurchasedBook** (purchase line)
  - `id`, `customer` (Many-to-One), `book` (Many-to-One), `quantity`, `priceAtPurchase`, `purchasedAt`

### Relationships
- **Author → Book**: One-to-Many (Author has many Books)
- **Book → Author**: Many-to-One (nullable)
- **Customer ↔ Book**: Many-to-Many via **PurchasedBook** (explicit join entity)

### Diagram (conceptual)
```
Author (1) ───< Book (many)

Customer (1) ───< PurchasedBook (many) >─── (many) Book
```

---

## API Endpoints
Base path: `/api`

### Authors
- `GET /api/authors` — list all
- `GET /api/authors/{id}` — get by id
- `POST /api/authors` — create (optionally with books)
- `PUT /api/authors/{id}` — update
- `DELETE /api/authors/{id}` — delete (fails with 409 if books prevent deletion depending on business rule)

### Books
- `GET /api/books` — list all, filter by `authorId`, `title`, `isbn`
- `GET /api/books/{id}` — get by id
- `POST /api/books` — create (can set `authorId` or omit)
- `PUT /api/books/{id}` — update
- `DELETE /api/books/{id}` — delete
- `PUT /api/books/{id}/author/{authorId}` — attach to an existing author

### Customers
- `GET /api/customers` — list all
- `GET /api/customers/{id}` — get by id
- `POST /api/customers` — create
- `PUT /api/customers/{id}` — update
- `DELETE /api/customers/{id}` — delete
- `GET /api/customers/{id}/purchases` — list purchases with book details

### Purchases
- `POST /api/customers/{id}/purchases` — create purchase lines
  - Request payload includes one or more book lines `{bookId, quantity}`; service copies current `price` into `priceAtPurchase`.

**Status codes**: `200 OK`, `201 Created`, `204 No Content`, `400 Bad Request`, `404 Not Found`, `409 Conflict`.

---

## Validation & Errors
- **Validation**: `jakarta.validation` annotations on DTOs, e.g. `@NotBlank`, `@Email`, `@Positive`, `@Size`, `@PastOrPresent`.
- **Global exception handler**: `@ControllerAdvice` returning `ResponseEntity<ErrorResponse>` with `timestamp`, `status`, `error`, `message`, `path`.
- **Custom exceptions**: `EntityNotFoundException`, `DuplicateResourceException`, `InvalidOperationException` (e.g., deleting an Author with existing Books depending on policy).

---

## Security (Basic Auth, optional)
- **Optional**: Enable **Basic Auth** to protect non-public endpoints.
- Expose `/home`, Swagger, and H2 console as public.
- Use roles: `ADMIN`, `MANAGER`, `USER` with `@PreAuthorize` on endpoints as needed.
- For Swagger UI, prefer **per-controller `@SecurityRequirement`** instead of a global requirement to avoid repeated browser login prompts.

---

## OpenAPI/Swagger UI
- Swagger UI available at: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`
- Use the **Authorize** button to send Basic Auth if security is enabled.

---

## Persistence & Profiles (H2/MySQL)

### Default (H2 in-memory)
`src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:bookstore;DB_CLOSE_DELAY=-1;MODE=MySQL
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

# Swagger UI tuning (optional)
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    path: /swagger-ui/index.html
```

### MySQL profile
`src/main/resources/application-mysql.yml`
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookstore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: ${DB_USER:root}
    password: ${DB_PASS:root}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```
Run with profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
# or
java -jar target/bookstore-*.jar --spring.profiles.active=mysql
```

---

## Run the Service
```bash
# 1) Clean & build
mvn clean package

# 2) Run with H2 (default)
mvn spring-boot:run
# or
java -jar target/bookstore-*.jar

# 3) Verify
curl http://localhost:8080/actuator/health
open http://localhost:8080/swagger-ui/index.html
```

---

## Build & Run JAR
```bash
mvn -q -DskipTests=false clean package
java -jar target/bookstore-*.jar
```

---

## Docker (optional)
### MySQL via Docker
```bash
docker run --name mysql-bookstore -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=bookstore -d mysql:8.0
```
Update `application-mysql.yml` credentials if needed.

### SonarQube via Docker
```bash
docker run -d --name sonar -p 9000:9000 sonarqube:latest
# Open http://localhost:9000 (default admin/admin)
```

---

## Testing & Coverage (JUnit, JaCoCo)
- **Unit tests**: services, mappers, validators
- **Slice tests**: `@WebMvcTest` for controllers, `@DataJpaTest` for repositories
- **Integration tests**: with H2, boot context

Add JaCoCo to `pom.xml`:
```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.11</version>
  <executions>
    <execution>
      <goals>
        <goal>prepare-agent</goal>
        <goal>report</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```
Run:
```bash
mvn clean verify
# coverage report at target/site/jacoco/index.html
```

---

## Static Analysis (SonarQube)
Add to `pom.xml` (or use `sonar-project.properties`). Then run:
```bash
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<your-token>
```
Configure Quality Profiles/Gates in SonarQube UI. Check issues and coverage.

---

## Logging
- Uses **SLF4J** with **Logback**.
- Log pattern includes timestamp, level, logger, thread, and message.
- Optionally add request correlation via MDC.

`src/main/resources/logback-spring.xml` (example):
```xml
<configuration>
  <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"/>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>${LOG_PATTERN}</pattern>
    </encoder>
  </appender>
  <root level="INFO">
    <appender-ref ref="STDOUT"/>
  </root>
</configuration>
```

---

## Project Structure
```
src
├── main
│   ├── java/com/application/bookstore_security
│   │   ├── BookstoreApplication.java
│   │   ├── config
│   │   │   ├── OpenApiConfig.java
│   │   │   └── SecurityConfig.java (optional if secured)
│   │   ├── controller
│   │   ├── dto
│   │   ├── exception
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── ...
│   │   ├── mapper
│   │   ├── model
│   │   ├── repository
│   │   └── service
│   └── resources
│       ├── application.yml
│       ├── application-mysql.yml
│       ├── logback-spring.xml
│       └── db (optional migrations if using Flyway/Liquibase)
└── test
    └── java/com/application/bookstore_security
        ├── controller
        ├── service
        ├── repository
        └── integration
```

---

## Sample Requests

### Create Author
```bash
curl -X POST http://localhost:8080/api/authors \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "George",
    "lastName": "Orwell",
    "bio": "English novelist"
  }'
```

### Create Book (without author)
```bash
curl -X POST http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -d '{
    "title": "1984",
    "isbn": "9780451524935",
    "price": 14.99,
    "publishedAt": "1949-06-08"
  }'
```

### Attach Book to Author
```bash
curl -X PUT http://localhost:8080/api/books/1/author/1
```

### Create Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H 'Content-Type: application/json' \
  -d '{
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice@example.com"
  }'
```

### Purchase Books
```bash
curl -X POST http://localhost:8080/api/customers/1/purchases \
  -H 'Content-Type: application/json' \
  -d '{
    "lines": [
      { "bookId": 1, "quantity": 2 },
      { "bookId": 3, "quantity": 1 }
    ]
  }'
```

---

## Troubleshooting
- **Swagger UI shows login popup repeatedly**: Ensure Swagger endpoints are permitted and avoid global `@OpenAPIDefinition(security=...)`. Use per-controller `@SecurityRequirement` and, optionally, set `HttpStatusEntryPoint(401)` to suppress browser prompts.
- **H2 console fails to load**: Allow frames/CSRF exceptions:
  ```java
  http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
  http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
  ```
- **MySQL connection errors**: Check `DB_USER`, `DB_PASS`, port `3306`, and JDBC URL; ensure container started if using Docker.
- **Validation errors (400)**: Inspect response payload for field messages; adjust DTO annotations accordingly.

---

## Coding Standards
- Consistent package names: `com.application.bookstore_security.*`
- Class & method naming: nouns for classes, verbs for methods
- DTOs are **immutable** where possible; use builders
- Prefer constructor injection in services
- Keep controllers thin; business logic in services

---

## License
This project is provided as a reference implementation. Use and modify as needed.

