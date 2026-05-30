package interview.level1_core.configuration;

/**
 * Q19. What are Spring Boot Starters?
 *
 * Starters are curated sets of dependencies that bring in everything you need
 * for a specific feature. One dependency instead of many.
 *
 * Without starter:
 *   implementation 'org.springframework:spring-web:6.x'
 *   implementation 'org.springframework:spring-webmvc:6.x'
 *   implementation 'com.fasterxml.jackson.core:jackson-databind:2.x'
 *   implementation 'org.apache.tomcat.embed:tomcat-embed-core:10.x'
 *   // + more transitive dependencies...
 *
 * With starter:
 *   implementation 'org.springframework.boot:spring-boot-starter-web'
 *   // That's it! Brings in everything above + auto-configuration
 *
 * Naming convention:
 *   Official:  spring-boot-starter-{feature}
 *   Community: {project}-spring-boot-starter
 *
 * Common starters:
 *   starter-web         → Tomcat, Spring MVC, Jackson, validation
 *   starter-data-jpa    → Hibernate, Spring Data JPA, HikariCP
 *   starter-security    → Spring Security, auto-config
 *   starter-test        → JUnit 5, Mockito, AssertJ, Spring Test
 *   starter-actuator    → Health, metrics, info, monitoring endpoints
 *   starter-validation  → Hibernate Validator (JSR-380)
 *   starter-cache       → Spring Cache abstraction
 *   starter-aop         → Spring AOP, AspectJ
 *   starter-mail        → JavaMail
 *   starter-websocket   → WebSocket support
 */
public class StartersExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Starters ===\n");

        System.out.println("A starter is ONE dependency that brings in EVERYTHING for a feature.\n");

        System.out.println("=== Most-Used Starters ===");
        System.out.println("| Starter                  | Includes                                   |");
        System.out.println("|-------------------------|--------------------------------------------|");
        System.out.println("| starter-web             | Tomcat, Spring MVC, Jackson, validation     |");
        System.out.println("| starter-data-jpa        | Hibernate, Spring Data JPA, HikariCP        |");
        System.out.println("| starter-security        | Spring Security filters, CSRF, login page   |");
        System.out.println("| starter-test            | JUnit 5, Mockito, AssertJ, MockMvc          |");
        System.out.println("| starter-actuator        | /health, /info, /metrics, /env endpoints    |");
        System.out.println("| starter-validation      | Hibernate Validator (@NotNull, @Size, etc.) |");
        System.out.println("| starter-cache           | Cache abstraction (@Cacheable, etc.)        |");
        System.out.println("| starter-aop             | AspectJ auto-proxy, @Aspect support         |");

        System.out.println("\n=== What a starter contains ===");
        System.out.println("1. Dependencies (curated, compatible versions)");
        System.out.println("2. Auto-configuration classes (@Conditional*)");
        System.out.println("3. Default properties (sensible defaults)");

        System.out.println("\n=== Creating custom starter ===");
        System.out.println("1. Create autoconfigure module: mylib-spring-boot-autoconfigure");
        System.out.println("2. Create starter module: mylib-spring-boot-starter (just dependencies)");
        System.out.println("3. Register in META-INF/spring/...AutoConfiguration.imports");
    }
}
