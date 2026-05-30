package interview.level6_microservices.discovery;

/**
 * Q13. What is an API Gateway and why is it needed?
 *
 * Problem: Client needs to call 10 different microservices on different ports/hosts.
 *   Frontend can't manage all those URLs, auth tokens, rate limits separately.
 *
 * Solution: API Gateway — single entry point for all client requests.
 *
 * Responsibilities:
 *   1. Request routing       → /api/users → user-service, /api/orders → order-service
 *   2. Authentication        → validate JWT before forwarding
 *   3. Rate limiting         → protect backend from traffic spikes
 *   4. Load balancing        → distribute across service instances
 *   5. Circuit breaking      → fail fast if downstream is down
 *   6. Request/Response transformation → add headers, modify body
 *   7. Logging & monitoring  → centralized request logging
 *   8. CORS handling         → one place for CORS config
 *
 * Spring Cloud Gateway:
 *   - Built on Spring WebFlux (reactive, non-blocking)
 *   - Route: predicate (when) + filters (transform) + URI (where)
 *
 *   spring:
 *     cloud:
 *       gateway:
 *         routes:
 *           - id: user-service
 *             uri: lb://user-service        # load-balanced via Eureka
 *             predicates:
 *               - Path=/api/users/**
 *             filters:
 *               - AddRequestHeader=X-Request-Source, gateway
 *               - CircuitBreaker=name=userCB,fallbackUri=forward:/fallback
 *
 * Alternatives:
 *   Kong, NGINX, Envoy (Istio), AWS API Gateway, Zuul (legacy Netflix)
 *
 * Q14. What is the BFF (Backend For Frontend) pattern?
 *
 * One API Gateway per client type:
 *   Mobile BFF:  optimized for mobile (smaller payloads, fewer calls)
 *   Web BFF:     optimized for web (richer data, more fields)
 *   → Each BFF aggregates/transforms data differently
 */
public class ApiGatewayExplained {

    public static void main(String[] args) {
        System.out.println("=== API Gateway ===\n");

        System.out.println("=== What it does ===");
        System.out.println("1. Request routing    → /api/users → user-service");
        System.out.println("2. Authentication     → validate JWT");
        System.out.println("3. Rate limiting      → protect backend");
        System.out.println("4. Load balancing     → distribute traffic");
        System.out.println("5. Circuit breaking   → fail fast");

        System.out.println("\n=== Spring Cloud Gateway ===");
        System.out.println("Route = predicate + filters + URI");
        System.out.println("Built on WebFlux (reactive, non-blocking)");

        System.out.println("\n=== BFF Pattern ===");
        System.out.println("Separate gateway per client: Mobile BFF, Web BFF");
    }
}
