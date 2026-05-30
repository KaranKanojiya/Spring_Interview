package interview.level5_advanced.actuator;

/**
 * Q6. What is Spring Boot Actuator and what endpoints does it provide?
 *
 * Actuator adds production-ready monitoring and management endpoints.
 *
 * Key endpoints:
 *   | Endpoint         | Description                          | Sensitive? |
 *   |-----------------|--------------------------------------|-----------|
 *   | /actuator/health | Application health status             | No        |
 *   | /actuator/info   | Application metadata                  | No        |
 *   | /actuator/metrics| All available metrics                 | Yes       |
 *   | /actuator/env    | Environment properties                | Yes       |
 *   | /actuator/beans  | All Spring beans                      | Yes       |
 *   | /actuator/mappings| All @RequestMapping endpoints        | Yes       |
 *   | /actuator/loggers| Logger levels (can change at runtime)| Yes       |
 *   | /actuator/threaddump | Thread dump                      | Yes       |
 *   | /actuator/heapdump   | Heap dump (downloads file)       | Yes       |
 *   | /actuator/conditions | Auto-config conditions report    | Yes       |
 *   | /actuator/configprops| All @ConfigurationProperties     | Yes       |
 *   | /actuator/scheduledtasks | Scheduled tasks               | Yes       |
 *
 * Configuration:
 *   management.endpoints.web.exposure.include=*    → expose all (dev only!)
 *   management.endpoints.web.exposure.include=health,info,metrics → selective
 *   management.endpoint.health.show-details=always → show component details
 *
 * Q7. How do you create a custom health indicator?
 *
 *   @Component
 *   class DatabaseHealthIndicator implements HealthIndicator {
 *       @Override
 *       public Health health() {
 *           if (isDatabaseUp()) {
 *               return Health.up().withDetail("db", "PostgreSQL").build();
 *           }
 *           return Health.down().withDetail("error", "Connection refused").build();
 *       }
 *   }
 *
 * curl tests:
 *   curl -s http://localhost:8080/actuator | jq            → list all endpoints
 *   curl -s http://localhost:8080/actuator/health | jq     → health status
 *   curl -s http://localhost:8080/actuator/metrics | jq    → available metrics
 *   curl -s http://localhost:8080/actuator/metrics/jvm.memory.used | jq
 *   curl -s http://localhost:8080/actuator/beans | jq      → all Spring beans
 *   curl -s http://localhost:8080/actuator/mappings | jq   → all endpoints
 *   curl -s http://localhost:8080/actuator/env | jq        → environment
 *   curl -s http://localhost:8080/actuator/conditions | jq → auto-config report
 */
public class ActuatorExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Actuator ===\n");

        System.out.println("=== Key endpoints ===");
        System.out.println("/actuator/health   → UP/DOWN status");
        System.out.println("/actuator/metrics  → JVM, HTTP, DB metrics");
        System.out.println("/actuator/beans    → all Spring beans");
        System.out.println("/actuator/mappings → all REST endpoints");
        System.out.println("/actuator/env      → environment properties");
        System.out.println("/actuator/loggers  → change log levels at runtime");

        System.out.println("\n=== Production config ===");
        System.out.println("NEVER expose all endpoints in production!");
        System.out.println("management.endpoints.web.exposure.include=health,info,metrics");
        System.out.println("management.server.port=8081  → separate port for actuator");
    }
}
