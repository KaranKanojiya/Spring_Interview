package interview.level1_core.configuration;

/**
 * Q20. How does Spring Boot externalized configuration priority work?
 *
 * Spring Boot loads configuration from multiple sources.
 * Higher number = HIGHER priority (overrides lower).
 *
 * Priority order (highest → lowest):
 *   1.  Command-line arguments (--server.port=9090)
 *   2.  SPRING_APPLICATION_JSON (inline JSON from env var)
 *   3.  ServletConfig/ServletContext parameters
 *   4.  JNDI attributes
 *   5.  System properties (-Dserver.port=9090)
 *   6.  OS environment variables (SERVER_PORT=9090)
 *   7.  RandomValuePropertySource (random.*)
 *   8.  Profile-specific properties OUTSIDE jar (application-{profile}.properties)
 *   9.  Profile-specific properties INSIDE jar
 *  10.  Application properties OUTSIDE jar (application.properties)
 *  11.  Application properties INSIDE jar
 *  12.  @PropertySource annotations
 *  13.  Default properties (SpringApplication.setDefaultProperties)
 *
 * Practical implication:
 *   application.properties → base defaults
 *   application-prod.properties → prod overrides
 *   Environment variable → container/cloud overrides
 *   Command line → one-off overrides for debugging
 *
 * Relaxed binding (for environment variables):
 *   spring.datasource.url → SPRING_DATASOURCE_URL
 *   server.port            → SERVER_PORT
 */
public class ConfigPriorityDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Configuration Priority ===\n");
        System.out.println("Highest priority wins. Sources (high → low):\n");

        System.out.println("  1. Command-line args:     --server.port=9090");
        System.out.println("  2. SPRING_APPLICATION_JSON (env var with inline JSON)");
        System.out.println("  3-4. Servlet/JNDI (rare)");
        System.out.println("  5. System properties:     -Dserver.port=9090");
        System.out.println("  6. OS env variables:      SERVER_PORT=9090");
        System.out.println("  7. RandomValue:           ${random.int}, ${random.uuid}");
        System.out.println("  8. Profile props (outside jar): ./config/application-prod.properties");
        System.out.println("  9. Profile props (inside jar):  classpath:application-prod.properties");
        System.out.println(" 10. App props (outside jar):     ./config/application.properties");
        System.out.println(" 11. App props (inside jar):      classpath:application.properties");
        System.out.println(" 12. @PropertySource annotations");
        System.out.println(" 13. Default properties");

        System.out.println("\n=== Practical Example ===");
        System.out.println("application.properties:       server.port=8080 (default)");
        System.out.println("application-prod.properties:  server.port=80 (prod override)");
        System.out.println("ENV var SERVER_PORT=8443:      overrides both for this instance");
        System.out.println("--server.port=9999:            overrides everything (debugging)");

        System.out.println("\n=== Config file locations (searched in order) ===");
        System.out.println("1. ./config/                (project root /config)");
        System.out.println("2. ./                       (project root)");
        System.out.println("3. classpath:/config/       (inside jar /config)");
        System.out.println("4. classpath:/              (inside jar root)");

        System.out.println("\n=== Environment variable relaxed binding ===");
        System.out.println("spring.datasource.url  →  SPRING_DATASOURCE_URL");
        System.out.println("my-app.feature.enabled →  MY_APP_FEATURE_ENABLED");
    }
}
