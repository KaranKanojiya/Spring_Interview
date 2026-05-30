package interview.level5_advanced.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Q7. Custom Health Indicator — shows in /actuator/health
 *
 * curl test:
 *   curl -s http://localhost:8080/actuator/health | jq
 *   → Should show "externalApi" component with status UP
 *
 * Add to application.properties for full details:
 *   management.endpoint.health.show-details=always
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // In production: check external service, DB connection, disk space, etc.
        boolean externalApiUp = checkExternalApi();

        if (externalApiUp) {
            return Health.up()
                    .withDetail("externalApi", "reachable")
                    .withDetail("responseTime", "42ms")
                    .build();
        }

        return Health.down()
                .withDetail("externalApi", "unreachable")
                .withDetail("error", "Connection timeout")
                .build();
    }

    private boolean checkExternalApi() {
        // Simulate: always up for demo
        return true;
    }
}
