# Spring Boot Interview Prep Repository

## What This Is
Spring Boot interview preparation repo — 6 progressive levels from Core Spring (Mid-Level) to Microservices (Staff). Each file contains interview Q&A with runnable Spring Boot code.

## Tech Stack
- **Java 21**, **Spring Boot 3.3.5**, **Gradle 8.10.2**
- H2 in-memory database, Spring Security, Spring Data JPA, Spring AOP, Actuator
- JWT (jjwt), Resilience4j

## Build & Run
```bash
./gradlew compileJava          # compile all
./gradlew bootRun              # run Spring Boot app
./gradlew test                 # run tests
```

## Project Structure
```
src/main/java/interview/
  level1_core/          — Core Spring (Mid-Level): DI, IoC, bean lifecycle, annotations, profiles, configuration
  level2_web/           — Spring Web (Mid/Senior): REST, exception handling, validation, filters, content negotiation
  level3_data/          — Spring Data (Senior): JPA, transactions, N+1, caching, repositories, query methods
  level4_security/      — Spring Security (Senior): authentication, authorization, JWT, CORS/CSRF
  level5_advanced/      — Advanced (Senior/Staff): AOP, actuator, auto-configuration, events, testing
  level6_microservices/ — Microservices (Staff): resilience, discovery, config, tracing, messaging
```

## Conventions
- Each Java file covers one interview question with detailed comments and runnable code
- Package naming: `interview.<level>.<topic>`
- Spring components are annotated but may not all auto-scan (some are conceptual demos)

## Memory
On session start, read Claude memory for project status and progress tracking.
