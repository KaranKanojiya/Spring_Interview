package interview.level5_advanced.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Q8. How to create a custom Actuator endpoint?
 *
 * @Endpoint(id = "appinfo") → creates /actuator/appinfo
 * @ReadOperation   → GET
 * @WriteOperation  → POST
 * @DeleteOperation → DELETE
 *
 * curl test:
 *   curl -s http://localhost:8080/actuator/appinfo | jq
 */
@Component
@Endpoint(id = "appinfo")
public class CustomActuatorEndpoint {

    @ReadOperation
    public Map<String, Object> appInfo() {
        return Map.of(
                "name", "Spring Boot Interview Prep",
                "version", "1.0.0",
                "levels", 6,
                "database", "H2 / PostgreSQL",
                "timestamp", LocalDateTime.now().toString()
        );
    }
}
