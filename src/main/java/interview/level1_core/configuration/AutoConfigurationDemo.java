package interview.level1_core.configuration;

/**
 * Q10. What is Spring Boot auto-configuration?
 *
 * Auto-configuration automatically sets up Spring beans based on what's on your classpath.
 *
 * How it works:
 *   1. @SpringBootApplication includes @EnableAutoConfiguration
 *   2. Spring reads META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 *      (or legacy: META-INF/spring.factories)
 *   3. Each auto-config class has @Conditional annotations:
 *      - @ConditionalOnClass(DataSource.class) → only if JDBC driver on classpath
 *      - @ConditionalOnMissingBean → backs off if you define your own
 *   4. Auto-config applies only when conditions are met
 *
 * Example: If spring-boot-starter-web is in your dependencies:
 *   → Tomcat auto-configured (embedded server)
 *   → DispatcherServlet auto-configured
 *   → Jackson auto-configured (JSON)
 *   → Error handling auto-configured (/error endpoint)
 *
 * Key principle: "Convention over Configuration"
 *   Spring Boot provides sensible defaults, you only override what you need.
 *
 * Debugging auto-config:
 *   --debug flag or logging.level.org.springframework.boot.autoconfigure=DEBUG
 *   Shows: "Positive matches" (applied) and "Negative matches" (skipped)
 *
 * Auto-config report:
 *   Run: ./gradlew bootRun --args='--debug'
 *   Or: management.endpoint.conditions.enabled=true (actuator)
 */
public class AutoConfigurationDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Auto-Configuration ===\n");

        System.out.println("=== How it works ===");
        System.out.println("1. @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan");
        System.out.println("2. @EnableAutoConfiguration triggers reading of auto-config imports file");
        System.out.println("3. Each auto-config class checks @Conditional* annotations");
        System.out.println("4. If conditions met → beans are created automatically\n");

        System.out.println("=== Example: DataSource auto-config ===");
        System.out.println("Conditions checked:");
        System.out.println("  @ConditionalOnClass(DataSource.class)     → JDBC driver on classpath?");
        System.out.println("  @ConditionalOnMissingBean(DataSource.class) → No custom DataSource defined?");
        System.out.println("  If YES to both → auto-configures HikariCP DataSource\n");

        System.out.println("=== Common auto-configurations ===");
        System.out.println("| Starter               | What's auto-configured                    |");
        System.out.println("|----------------------|-------------------------------------------|");
        System.out.println("| starter-web          | Tomcat, DispatcherServlet, Jackson, /error |");
        System.out.println("| starter-data-jpa     | Hibernate, EntityManager, DataSource       |");
        System.out.println("| starter-security     | SecurityFilterChain, CSRF, login page      |");
        System.out.println("| starter-actuator     | Health, info, metrics endpoints            |");
        System.out.println("| starter-cache        | CacheManager (based on available provider) |");

        System.out.println("\n=== Overriding auto-config ===");
        System.out.println("Define your own @Bean → auto-config backs off (@ConditionalOnMissingBean)");
        System.out.println("Exclude: @SpringBootApplication(exclude = SecurityAutoConfiguration.class)");

        System.out.println("\n=== Debugging ===");
        System.out.println("./gradlew bootRun --args='--debug'");
        System.out.println("→ Shows 'Positive matches' and 'Negative matches'");
    }
}
