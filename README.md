# Spring Boot Interview Prep -- From Core to Microservices

**Complete Spring Boot interview preparation repository — organized by experience level, from DI fundamentals to microservices patterns.**

> **77 Java files** | **6 progressive levels** | **Spring Boot 3.3.5** | **100 interview questions with code**

```
Level 1  [==========]  Core Spring         Mid-Level       20 questions   20 files
Level 2  [==========]  Spring Web & REST   Mid/Senior      20 questions   17 files
Level 3  [==========]  Spring Data & JPA   Senior          15 questions   15 files
Level 4  [==========]  Spring Security     Senior          15 questions    7 files
Level 5  [==========]  Advanced Spring     Senior/Staff    15 questions    9 files
Level 6  [==========]  Microservices       Staff           15 questions    7 files
```

---

## Tech Stack

| Component | Version |
|-----------|---------|
| Java | 21 |
| Spring Boot | 3.3.5 |
| Spring Framework | 6.1.x (managed by Boot) |
| Hibernate | 6.5.3 |
| Gradle | 8.10.2 (wrapper included) |
| Tomcat (embedded) | 10.1.31 |
| Spring Security | 6.3.x |
| Swagger (springdoc) | 2.6.0 |
| Database | H2 (default) / PostgreSQL 16 (Docker) |

---

## What Each Level Contains

| Level | Type | REST APIs? | How to test |
|-------|------|-----------|-------------|
| **Level 1: Core Spring** | Standalone `main()` demos | No | Run individual files via Gradle or IntelliJ |
| **Level 2: Spring Web** | Live REST controllers | **Yes** — `/api/users/**` | `./gradlew bootRun` + curl / Swagger |
| **Level 3: Spring Data** | Live REST controllers + JPA entities | **Yes** — `/api/products/**`, `/api/orders/**` | `./gradlew bootRun` + curl / Swagger |
| **Level 4: Spring Security** | Live JWT auth + secured endpoints | **Yes** — `/api/auth/**`, `/api/secure/**` | `./gradlew bootRun` + curl / Swagger |
| **Level 5: Advanced** | Live AOP + Actuator + conceptual | **Partial** — `/actuator/**` + AOP in console logs | `./gradlew bootRun` + curl / Swagger |
| **Level 6: Microservices** | Conceptual (interview Q&A) | **No** — read & discuss | Run standalone `main()` or read code |

**One single `./gradlew bootRun` exposes all REST APIs (Levels 2-5).** Level 1 and 6 are standalone demos.

---

## Database Schema

All levels share the **same database** and **same schema**. When you run `./gradlew bootRun`, the following tables are created automatically:

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    users     │     │   products   │     │    orders    │     │ order_items  │
├──────────────┤     ├──────────────┤     ├──────────────┤     ├──────────────┤
│ id (PK)      │     │ id (PK)      │     │ id (PK)      │     │ id (PK)      │
│ name         │     │ name         │     │ user_id (FK) │────→│ order_id (FK)│
│ email        │     │ price        │     │ status       │     │ product_id(FK│
│ age          │     │ category     │     │ total_amount │     │ quantity     │
└──────────────┘     │ description  │     │ created_at   │     │ unit_price   │
                     │ created_at   │     └──────────────┘     └──────────────┘
                     │ updated_at   │
                     └──────────────┘
```

**Relationships:**
- `orders.user_id` → `users.id` (Many-to-One)
- `order_items.order_id` → `orders.id` (Many-to-One)
- `order_items.product_id` → `products.id` (Many-to-One)

**Both H2 and PostgreSQL use the same schema.** Hibernate auto-creates tables on startup (`ddl-auto=create-drop`).

| Database | Schema behavior |
|----------|----------------|
| H2 (default) | Tables created on startup, **destroyed on shutdown** (in-memory) |
| PostgreSQL (Docker) | Tables created on startup, **destroyed on shutdown** (`create-drop`). Change to `update` in `application-postgres.properties` to persist across restarts. |

**To persist PostgreSQL data across restarts:**

Edit `src/main/resources/application-postgres.properties`:
```properties
# Change from:
spring.jpa.hibernate.ddl-auto=create-drop
# To:
spring.jpa.hibernate.ddl-auto=update
```

**Sample data** (3 users, 5 products, 2 orders) is auto-loaded on every startup via `DataInitializer.java` and `Level3DataInitializer.java`.

---

## Quick Start

### Prerequisites

- **JDK 21+**
- **Gradle 8.10+** (wrapper included)

### Clone & Build

```bash
git clone <repo-url>
cd Spring_Boot_Interview

# Compile all files
./gradlew compileJava
```

---

### Swagger UI (API Documentation)

Once the app is running, open in your browser:

```
http://localhost:8080/swagger-ui.html
```

Swagger UI shows all available endpoints grouped by tags (Users, Products, Orders, Authentication, Secured Endpoints). You can:

1. **Try any endpoint** directly from the browser — click "Try it out" → "Execute"
2. **Authenticate for secured endpoints:**
   - First, expand **Authentication** → **POST /api/auth/login** → Try it out
   - Use `{"username":"admin","password":"admin123"}` → Execute → copy the `token` value
   - Click the **Authorize** button (top-right lock icon) → paste the token → Authorize
   - Now all secured endpoints will include the JWT token automatically

| URL | Description |
|-----|-------------|
| http://localhost:8080/swagger-ui.html | Swagger UI (interactive) |
| http://localhost:8080/v3/api-docs | OpenAPI 3.0 JSON spec |

---

### Two Ways to Run

This project has **two types** of runnable code:

| Type | How to run | Levels |
|------|-----------|--------|
| **Standalone demos** (`main()` methods) | `./gradlew -PmainClass=<class> run` or IntelliJ Run | Level 1 (Core Spring) |
| **Spring Boot app** (REST endpoints) | `./gradlew bootRun` + curl | Levels 2-6 (Web, Data, Security, etc.) |

---

### 1. Running Standalone Demos (Level 1)

Level 1 files are self-contained with their own `main()` method. No web server needed.

```bash
# Using Gradle
./gradlew -PmainClass=interview.level1_core.di.IoCAndDIDemo run
./gradlew -PmainClass=interview.level1_core.bean_lifecycle.BeanScopes run
./gradlew -PmainClass=interview.level1_core.profiles.ProfilesDemo run
./gradlew -PmainClass=interview.level1_core.bean_lifecycle.BeanLifecycleDemo run
```

**From IntelliJ IDEA:** Right-click any file with a `main()` method → **Run**

---

### 2. Running the Spring Boot App (Levels 2-6)

**Main class:** `src/main/java/interview/SpringBootInterviewApplication.java`

```bash
# Start the web server on port 8080
./gradlew bootRun
```

You should see:
```
Started SpringBootInterviewApplication in X seconds
=== Sample data loaded: 3 users ===
```

**From IntelliJ IDEA:** Right-click `SpringBootInterviewApplication.java` → **Run**

**Stop the app:** `Ctrl+C` in the terminal

---

### 3. Testing REST APIs with curl

Open a **second terminal** while the app is running:

```bash
# --- Basic CRUD ---

# GET all users (3 pre-loaded)
curl -s http://localhost:8080/api/users | jq

# GET user by ID
curl -s http://localhost:8080/api/users/1 | jq

# POST create user (returns 201 Created)
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@test.com","age":35}' | jq

# PUT update user
curl -s -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Karan Updated","email":"karan@test.com"}' | jq

# DELETE user (returns 204 No Content)
curl -s -X DELETE http://localhost:8080/api/users/1 -w "\nHTTP Status: %{http_code}\n"

# --- Error handling ---

# 404 Not Found (ProblemDetail / RFC 7807)
curl -s http://localhost:8080/api/users/999 | jq

# 400 Validation errors (empty name, invalid email)
curl -s -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"invalid"}' | jq

# --- Pagination & Sorting ---
curl -s "http://localhost:8080/api/users/search?page=0&size=2" | jq
curl -s "http://localhost:8080/api/users/search?sort=name,asc" | jq

# --- Level 3: Products (JPA/Repository demos) ---
curl -s http://localhost:8080/api/products | jq
curl -s http://localhost:8080/api/products/1 | jq
curl -s "http://localhost:8080/api/products/search?category=ELECTRONICS" | jq
curl -s "http://localhost:8080/api/products/price-range?min=10&max=100" | jq
curl -s http://localhost:8080/api/products/most-expensive | jq
curl -s http://localhost:8080/api/products/stats | jq
curl -s -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Keyboard","price":49.99,"category":"ELECTRONICS"}' | jq

# --- Level 3: Orders (JOIN FETCH / N+1 demo) ---
curl -s http://localhost:8080/api/orders | jq
curl -s http://localhost:8080/api/orders/1 | jq

# --- Level 4: Security (JWT Authentication) ---

# Login as admin → get JWT token
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq

# Login as user
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | jq

# Bad credentials → 401
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrong"}' | jq

# Save admin token to variable (for subsequent requests)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")

# Access secured endpoint with token
curl -s http://localhost:8080/api/secure/profile -H "Authorization: Bearer $TOKEN" | jq

# Admin-only endpoint
curl -s http://localhost:8080/api/secure/admin -H "Authorization: Bearer $TOKEN" | jq

# Current user info
curl -s http://localhost:8080/api/secure/me -H "Authorization: Bearer $TOKEN" | jq

# No token → 403
curl -s -w "\nHTTP %{http_code}\n" http://localhost:8080/api/secure/profile

# User accessing admin endpoint → 403
USER_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
curl -s -w "\nHTTP %{http_code}\n" http://localhost:8080/api/secure/admin \
  -H "Authorization: Bearer $USER_TOKEN"

# --- Level 5: Actuator & Monitoring ---
curl -s http://localhost:8080/actuator | jq                  # list all endpoints
curl -s http://localhost:8080/actuator/health | jq           # health (with custom indicator)
curl -s http://localhost:8080/actuator/appinfo | jq          # custom endpoint
curl -s http://localhost:8080/actuator/metrics | jq          # available metrics
curl -s http://localhost:8080/actuator/metrics/jvm.memory.used | jq  # specific metric
curl -s http://localhost:8080/actuator/beans | jq            # all Spring beans
curl -s http://localhost:8080/actuator/mappings | jq         # all REST endpoints
curl -s http://localhost:8080/actuator/env | jq              # environment properties
curl -s http://localhost:8080/actuator/conditions | jq       # auto-config report
```

### Run Tests

```bash
./gradlew test
```

---

## Database

The project supports **two databases** — H2 for quick testing, PostgreSQL for production-like setup.

---

### Option 1: H2 (default — no setup needed)

H2 is an embedded in-memory database. No installation, no Docker, no setup.

#### Step 1: Start the app

```bash
./gradlew bootRun
```

That's it. H2 starts automatically inside the app.

#### Connection Details

| Setting | Value |
|---------|-------|
| Database | H2 (embedded, in-memory) |
| JDBC URL | `jdbc:h2:mem:interviewdb` |
| Username | `sa` |
| Password | *(empty — leave blank)* |
| Driver | `org.h2.Driver` |
| DDL mode | `create-drop` (recreated on every restart) |

#### Browse via H2 Console (browser)

While the app is running, open in your browser:

```
http://localhost:8080/h2-console
```

1. **JDBC URL:** `jdbc:h2:mem:interviewdb`
2. **User Name:** `sa`
3. **Password:** *(leave empty)*
4. Click **Connect**

You can browse tables (`USERS`, `PRODUCTS`, `ORDERS`, `ORDER_ITEMS`) and run SQL.

#### Connect via DB Client (IntelliJ / DBeaver)

| Setting | Value |
|---------|-------|
| JDBC URL | `jdbc:h2:mem:interviewdb;DB_CLOSE_DELAY=-1` |
| Username | `sa` |
| Password | *(empty)* |
| Driver | `org.h2.Driver` |

> **Important:** H2 is in-memory — the database only exists while `bootRun` is active. When you stop the app, all data is gone. Sample data (3 users, 5 products, 2 orders) is auto-loaded on every startup.

---

### Option 2: PostgreSQL (via Docker)

PostgreSQL runs in a Docker container. Data persists across app restarts.

**Prerequisite:** Docker Desktop must be running.

#### Step 1: Start PostgreSQL

```bash
cd /Users/kanojik/Documents/Karan/Autodesk_AI_Development/Spring_Boot_Interview

# Start PostgreSQL container (runs in background)
docker compose up -d

# Verify it's running
docker compose ps
```

You should see:
```
NAME                  STATUS
interview-postgres    running    0.0.0.0:5432->5432/tcp
```

#### Step 2: Start the app with postgres profile

```bash
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

You should see:
```
Started SpringBootInterviewApplication in X seconds
=== Sample data loaded: 3 users ===
=== Level 3 data loaded: 5 products, 2 orders ===
```

#### Step 3: Test with curl

```bash
curl -s http://localhost:8080/api/users | jq
curl -s http://localhost:8080/api/products | jq
curl -s http://localhost:8080/api/orders | jq
```

#### Connection Details

| Setting | Value |
|---------|-------|
| Database | PostgreSQL 16 (Docker) |
| Host | `localhost` |
| Port | `5432` |
| Database Name | `interviewdb` |
| Username | `interview` |
| Password | `interview123` |
| JDBC URL | `jdbc:postgresql://localhost:5432/interviewdb` |
| Container | `interview-postgres` |
| DDL mode | `create-drop` |

#### Connect via DB Client (IntelliJ / DBeaver / pgAdmin)

| Setting | Value |
|---------|-------|
| Host | `localhost` |
| Port | `5432` |
| Database | `interviewdb` |
| Username | `interview` |
| Password | `interview123` |
| JDBC URL | `jdbc:postgresql://localhost:5432/interviewdb` |

#### Connect via psql (command line)

```bash
# Connect to PostgreSQL inside the Docker container
docker exec -it interview-postgres psql -U interview -d interviewdb

# List all tables
\dt

# Query data
SELECT * FROM users;
SELECT * FROM products;
SELECT * FROM orders;

# Query orders with items (JOIN)
SELECT o.id AS order_id, u.name AS customer, p.name AS product,
       oi.quantity, oi.unit_price, o.total_amount, o.status
FROM orders o
JOIN users u ON o.user_id = u.id
JOIN order_items oi ON oi.order_id = o.id
JOIN products p ON oi.product_id = p.id
ORDER BY o.id;

# Exit psql
\q
```

#### Stop PostgreSQL

```bash
# Stop container (data is preserved in Docker volume)
docker compose down

# Stop container AND delete all data
docker compose down -v

# Restart fresh
docker compose up -d
```

#### Switch between H2 and PostgreSQL

```bash
# H2 (default — no flag needed)
./gradlew bootRun

# PostgreSQL (add profile flag)
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

All endpoints, curl commands, and behavior are identical — only the underlying database changes.

---

## Troubleshooting

### Port 8080 already in use

```
Web server failed to start. Port 8080 was already in use.
```

**Find and kill the process using port 8080:**

```bash
# Find the process
lsof -i :8080

# Kill it (replace <PID> with the actual PID from above)
kill <PID>

# Or force kill
kill -9 <PID>

# Then restart
./gradlew bootRun
```

**Or change the port** (add to `application.properties` or command line):

```bash
./gradlew bootRun --args='--server.port=9090'
```

Then use `http://localhost:9090/api/users` in your curl commands.

### Gradle build fails

```bash
# Clean build
./gradlew clean compileJava

# Check Java version (must be 21+)
java -version
```

### curl returns empty or error

- Make sure the app is running (`Started SpringBootInterviewApplication` in console)
- Check you're using the right port (`http://localhost:8080`)
- Install `jq` for pretty JSON: `brew install jq` (optional — curl works without it)

### H2 Console not loading

- Make sure app is running
- URL: http://localhost:8080/h2-console
- JDBC URL must be exactly: `jdbc:h2:mem:interviewdb`
- Username: `sa`, Password: *(leave empty)*

---

## Repository Structure

```
src/main/java/interview/
|
|-- level1_core/                          # Core Spring -- Mid-Level
|   |-- di/                              # Dependency Injection, IoC container
|   |-- bean_lifecycle/                  # Bean scopes, lifecycle callbacks
|   |-- annotations/                     # @Component, @Service, @Repository, @Controller
|   |-- configuration/                   # @Configuration, @Bean, @Import, @PropertySource
|   |-- profiles/                        # @Profile, environment-specific beans
|
|-- level2_web/                           # Spring Web -- Mid/Senior
|   |-- rest/                            # @RestController, @RequestMapping, ResponseEntity
|   |-- exception_handling/              # @ControllerAdvice, @ExceptionHandler, ProblemDetail
|   |-- validation/                      # @Valid, custom validators, BindingResult
|   |-- filters/                         # Filters, interceptors, request lifecycle
|   |-- content_negotiation/             # JSON/XML, HttpMessageConverter
|
|-- level3_data/                          # Spring Data -- Senior
|   |-- jpa/                             # Entity mapping, relationships, inheritance
|   |-- transactions/                    # @Transactional, propagation, isolation
|   |-- repositories/                    # JpaRepository, custom queries, Specifications
|   |-- caching/                         # @Cacheable, @CacheEvict, cache providers
|   |-- query_methods/                   # Derived queries, @Query, projections
|
|-- level4_security/                      # Spring Security -- Senior
|   |-- authentication/                  # AuthenticationManager, UserDetailsService
|   |-- authorization/                   # Role-based, method security, @PreAuthorize
|   |-- jwt/                             # JWT token generation, validation, filters
|   |-- cors_csrf/                       # CORS configuration, CSRF protection
|
|-- level5_advanced/                      # Advanced Spring -- Senior/Staff
|   |-- aop/                             # @Aspect, pointcuts, advice types
|   |-- actuator/                        # Health, metrics, custom endpoints
|   |-- auto_configuration/             # @Conditional, spring.factories, starters
|   |-- events/                          # ApplicationEvent, @EventListener, async events
|   |-- testing/                         # @SpringBootTest, @WebMvcTest, @DataJpaTest
|
|-- level6_microservices/                 # Microservices -- Staff
|   |-- resilience/                      # Circuit breaker, retry, rate limiter, bulkhead
|   |-- discovery/                       # Service discovery concepts, Eureka, Consul
|   |-- config/                          # Centralized config, Config Server, refresh
|   |-- tracing/                         # Distributed tracing, Micrometer, correlation IDs
|   |-- messaging/                       # Kafka, RabbitMQ, event-driven architecture
```

---

## Level 1: Core Spring & Spring Boot Basics (Mid-Level) -- 20 Questions

> If you don't understand IoC, DI, and Bean lifecycle, nothing else in Spring will make sense. Start here.

---

### Q1. What is Inversion of Control (IoC) and Dependency Injection (DI)?

**Answer:**

**IoC (Inversion of Control):** A design principle where the control of object creation and lifecycle is transferred from the application code to a framework/container. Instead of your code creating dependencies with `new`, the Spring container creates and manages them.

**DI (Dependency Injection):** The mechanism Spring uses to implement IoC. Dependencies are "injected" into a class rather than the class creating them itself.

Three types of DI:
1. **Constructor Injection** (recommended) — dependencies provided via constructor
2. **Setter Injection** — dependencies provided via setter methods
3. **Field Injection** (`@Autowired` on field) — convenient but not recommended (hard to test)

**Code:**
- [`level1_core/di/IoCAndDIDemo.java`](src/main/java/interview/level1_core/di/IoCAndDIDemo.java)

---

### Q2. What is the difference between @Component, @Service, @Repository, and @Controller?

**Answer:**

All four are stereotype annotations that mark a class for Spring component scanning:

| Annotation | Purpose | Extra behavior |
|-----------|---------|---------------|
| `@Component` | Generic Spring-managed bean | None |
| `@Service` | Business logic layer | None (semantic only) |
| `@Repository` | Data access layer | Exception translation (SQL → DataAccessException) |
| `@Controller` | Web controller (MVC) | Returns views |
| `@RestController` | REST API controller | = @Controller + @ResponseBody |

`@Service`, `@Repository`, `@Controller` are all specializations of `@Component`.

**Code:**
- [`level1_core/annotations/StereotypeAnnotations.java`](src/main/java/interview/level1_core/annotations/StereotypeAnnotations.java)

---

### Q3. What are the different bean scopes in Spring?

**Answer:**

| Scope | Description | When to use |
|-------|------------|-------------|
| `singleton` (default) | One instance per Spring container | Stateless services, repositories |
| `prototype` | New instance every time requested | Stateful beans, builders |
| `request` | One instance per HTTP request | Request-scoped data (web only) |
| `session` | One instance per HTTP session | User session data (web only) |
| `application` | One instance per ServletContext | Shared across servlets |

**Code:**
- [`level1_core/bean_lifecycle/BeanScopes.java`](src/main/java/interview/level1_core/bean_lifecycle/BeanScopes.java)

---

### Q4. What is the Bean lifecycle in Spring?

**Answer:**

1. **Instantiation** — Spring creates the bean (constructor)
2. **Populate properties** — DI happens (setter/field injection)
3. **BeanNameAware** — `setBeanName()` called
4. **BeanFactoryAware** — `setBeanFactory()` called
5. **ApplicationContextAware** — `setApplicationContext()` called
6. **BeanPostProcessor.postProcessBeforeInitialization()**
7. **@PostConstruct** / `InitializingBean.afterPropertiesSet()` / `init-method`
8. **BeanPostProcessor.postProcessAfterInitialization()**
9. **Bean is ready for use**
10. **@PreDestroy** / `DisposableBean.destroy()` / `destroy-method`

**Code:**
- [`level1_core/bean_lifecycle/BeanLifecycleDemo.java`](src/main/java/interview/level1_core/bean_lifecycle/BeanLifecycleDemo.java)

---

### Q5. What is the difference between @Bean and @Component?

**Answer:**

| Feature | @Component | @Bean |
|---------|-----------|------|
| Target | Class-level annotation | Method-level (inside @Configuration) |
| Discovery | Component scanning | Explicit declaration |
| Use case | Your own classes | Third-party library classes you can't annotate |
| Customization | Limited | Full control over instantiation |

Use `@Component` for classes you own. Use `@Bean` for classes you don't own (e.g., `RestTemplate`, `ObjectMapper`).

**Code:**
- [`level1_core/annotations/BeanVsComponent.java`](src/main/java/interview/level1_core/annotations/BeanVsComponent.java)

---

### Q6. What is @Autowired and how does it work?

**Answer:**

`@Autowired` tells Spring to inject a dependency. Resolution order:
1. **By type** — finds a bean of the matching type
2. **By qualifier** — if multiple beans of same type, uses `@Qualifier("name")`
3. **By name** — if no qualifier, matches by variable name

Since Spring 4.3: If a class has only ONE constructor, `@Autowired` is optional.

**Code:**
- [`level1_core/di/AutowiredDemo.java`](src/main/java/interview/level1_core/di/AutowiredDemo.java)

---

### Q7. What is the difference between constructor and field injection?

**Answer:**

| Feature | Constructor Injection | Field Injection |
|---------|---------------------|----------------|
| Testability | Easy (pass mocks via constructor) | Hard (need reflection) |
| Immutability | Fields can be `final` | Cannot be `final` |
| Required deps | Enforced at compile time | NPE at runtime |
| Circular deps | Fails fast (compile/startup) | May hide circular deps |
| Spring recommendation | Recommended | Discouraged |

**Code:**
- [`level1_core/di/ConstructorVsFieldInjection.java`](src/main/java/interview/level1_core/di/ConstructorVsFieldInjection.java)

---

### Q8. What is @Configuration and how does it differ from @Component?

**Answer:**

`@Configuration` is a special `@Component` that uses **CGLIB proxying** to ensure `@Bean` methods return singleton instances. When one `@Bean` method calls another, the proxy intercepts and returns the existing singleton — not a new instance.

With plain `@Component`, calling a `@Bean` method directly would create a new instance each time (no proxying).

**Code:**
- [`level1_core/configuration/ConfigurationVsComponent.java`](src/main/java/interview/level1_core/configuration/ConfigurationVsComponent.java)

---

### Q9. What are Spring Profiles and how do you use them?

**Answer:**

Profiles allow you to register beans conditionally based on the active environment (dev, staging, prod).

- `@Profile("dev")` — bean is only created when "dev" profile is active
- Activate via: `spring.profiles.active=dev` or `SPRING_PROFILES_ACTIVE=dev`
- Profile-specific properties: `application-dev.properties`, `application-prod.properties`

**Code:**
- [`level1_core/profiles/ProfilesDemo.java`](src/main/java/interview/level1_core/profiles/ProfilesDemo.java)

---

### Q10. What is Spring Boot auto-configuration?

**Answer:**

Auto-configuration automatically configures Spring beans based on:
1. **Classpath dependencies** — if `spring-boot-starter-web` is present, configures `DispatcherServlet`, `Tomcat`, etc.
2. **Properties** — reads `application.properties` for customization
3. **Existing beans** — backs off if you define your own bean

How it works:
- `@SpringBootApplication` includes `@EnableAutoConfiguration`
- Spring reads `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- Each auto-config class has `@Conditional*` annotations to decide if it applies

**Code:**
- [`level1_core/configuration/AutoConfigurationDemo.java`](src/main/java/interview/level1_core/configuration/AutoConfigurationDemo.java)

---

### Q11. What is the difference between application.properties and application.yml?

**Answer:**

Both serve the same purpose — configure Spring Boot. Differences:

| Feature | .properties | .yml |
|---------|------------|------|
| Format | key=value | YAML hierarchy |
| Multi-profile | Separate files | `---` separator in one file |
| Lists | Indexed: `list[0]=a` | `- a` syntax |
| Readability | Flat, repetitive prefixes | Nested, cleaner for deep config |

Spring Boot loads both. `.properties` takes precedence if both exist.

**Code:**
- [`level1_core/configuration/PropertiesDemo.java`](src/main/java/interview/level1_core/configuration/PropertiesDemo.java)

---

### Q12. What is @Value and @ConfigurationProperties?

**Answer:**

| Feature | @Value | @ConfigurationProperties |
|---------|--------|------------------------|
| Granularity | Single property | Group of related properties |
| Type safety | No (String, needs parsing) | Yes (bound to typed POJO) |
| Validation | No | Yes (@Validated) |
| Use case | One-off values | Structured config (db, mail, etc.) |

**Code:**
- [`level1_core/configuration/ValueVsConfigProperties.java`](src/main/java/interview/level1_core/configuration/ValueVsConfigProperties.java)

---

### Q13. What is the difference between @Primary and @Qualifier?

**Answer:**

When multiple beans of the same type exist:
- `@Primary` — marks one bean as the default (used when no qualifier specified)
- `@Qualifier("name")` — explicitly selects a specific bean by name

`@Qualifier` takes precedence over `@Primary`.

**Code:**
- [`level1_core/di/PrimaryVsQualifier.java`](src/main/java/interview/level1_core/di/PrimaryVsQualifier.java)

---

### Q14. What happens when Spring encounters a circular dependency?

**Answer:**

- **Constructor injection:** Fails at startup with `BeanCurrentlyInCreationException` (good — fails fast)
- **Setter/field injection:** Spring resolves it using a **three-level cache** (early reference injection)
- **Spring Boot 2.6+:** Circular dependencies are **prohibited by default** (`spring.main.allow-circular-references=false`)

Best practice: Refactor to eliminate circular dependencies (extract shared logic to a third bean).

**Code:**
- [`level1_core/di/CircularDependencyDemo.java`](src/main/java/interview/level1_core/di/CircularDependencyDemo.java)

---

### Q15. What is @Lazy and when should you use it?

**Answer:**

By default, Spring creates all singleton beans at startup (eager initialization). `@Lazy` defers creation until the bean is first requested.

Use when:
- Bean has expensive initialization (DB connection, heavy computation)
- Bean is rarely used
- Breaking circular dependencies (temporary fix)

**Code:**
- [`level1_core/bean_lifecycle/LazyInitialization.java`](src/main/java/interview/level1_core/bean_lifecycle/LazyInitialization.java)

---

### Q16. What is the Spring ApplicationContext?

**Answer:**

`ApplicationContext` is the central interface for the Spring IoC container. It extends `BeanFactory` with additional features:

| Feature | BeanFactory | ApplicationContext |
|---------|------------|-------------------|
| Bean creation | Lazy (on demand) | Eager (at startup) |
| AOP | No | Yes |
| Event publishing | No | Yes |
| i18n | No | Yes |
| Environment | No | Yes |

Common implementations: `AnnotationConfigApplicationContext`, `ClassPathXmlApplicationContext`, `WebApplicationContext`

**Code:**
- [`level1_core/di/ApplicationContextDemo.java`](src/main/java/interview/level1_core/di/ApplicationContextDemo.java)

---

### Q17. What is @Conditional and how does Spring Boot use it?

**Answer:**

`@Conditional` creates beans only when a condition is met. Spring Boot provides many variants:

| Annotation | Condition |
|-----------|-----------|
| `@ConditionalOnClass` | Class is on classpath |
| `@ConditionalOnMissingBean` | No existing bean of this type |
| `@ConditionalOnProperty` | Property has specific value |
| `@ConditionalOnMissingClass` | Class is NOT on classpath |
| `@ConditionalOnBean` | Another bean exists |
| `@ConditionalOnWebApplication` | Running as web app |

This is the backbone of Spring Boot auto-configuration.

**Code:**
- [`level1_core/configuration/ConditionalDemo.java`](src/main/java/interview/level1_core/configuration/ConditionalDemo.java)

---

### Q18. What is the difference between @SpringBootApplication and its component annotations?

**Answer:**

`@SpringBootApplication` = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`

| Annotation | Purpose |
|-----------|---------|
| `@Configuration` | This class defines @Bean methods |
| `@EnableAutoConfiguration` | Enable auto-configuration based on classpath |
| `@ComponentScan` | Scan for @Component classes in this package and below |

**Code:**
- [`level1_core/annotations/SpringBootApplicationBreakdown.java`](src/main/java/interview/level1_core/annotations/SpringBootApplicationBreakdown.java)

---

### Q19. What are Spring Boot Starters?

**Answer:**

Starters are curated dependency sets that bring in everything you need for a feature:

| Starter | What it includes |
|---------|-----------------|
| `spring-boot-starter-web` | Tomcat, Spring MVC, Jackson |
| `spring-boot-starter-data-jpa` | Hibernate, Spring Data JPA, HikariCP |
| `spring-boot-starter-security` | Spring Security, auto-config |
| `spring-boot-starter-test` | JUnit 5, Mockito, AssertJ, Spring Test |
| `spring-boot-starter-actuator` | Health, metrics, info endpoints |

Starters follow naming convention: `spring-boot-starter-{feature}`

**Code:**
- [`level1_core/configuration/StartersExplained.java`](src/main/java/interview/level1_core/configuration/StartersExplained.java)

---

### Q20. How does Spring Boot externalized configuration priority work?

**Answer:**

Spring Boot loads configuration from multiple sources (highest to lowest priority):

1. Command-line arguments (`--server.port=9090`)
2. JNDI attributes
3. System properties (`-Dserver.port=9090`)
4. OS environment variables (`SERVER_PORT=9090`)
5. Profile-specific properties (`application-{profile}.properties`)
6. Application properties (`application.properties` / `.yml`)
7. `@PropertySource` annotations
8. Default properties (`SpringApplication.setDefaultProperties`)

Higher priority overrides lower.

**Code:**
- [`level1_core/configuration/ConfigPriorityDemo.java`](src/main/java/interview/level1_core/configuration/ConfigPriorityDemo.java)

---
