package interview.level6_microservices.messaging;

/**
 * Q15. What are the key design principles for microservices?
 *
 * 1. Single Responsibility:
 *    Each service owns ONE business capability (user management, payments, inventory).
 *    If you can't describe a service in one sentence, it's too big.
 *
 * 2. Database per Service:
 *    Each service has its OWN database. No shared database.
 *    → Loose coupling, independent deployability
 *    → Challenge: data consistency (solved by Saga pattern, eventual consistency)
 *
 * 3. API First:
 *    Design the API contract before implementation.
 *    Use OpenAPI/Swagger for documentation.
 *
 * 4. Smart Endpoints, Dumb Pipes:
 *    Business logic in services, not in the message broker.
 *    Broker just delivers messages — no transformations.
 *
 * 5. Design for Failure:
 *    Circuit breakers, retries, timeouts, bulkheads.
 *    Assume every network call can fail.
 *
 * 6. Decentralized Governance:
 *    Each team picks the right tool for their service.
 *    User service (Java), Analytics (Python), Search (Elasticsearch).
 *
 * 7. Observability (Three Pillars):
 *    Logs:    structured logging with correlation IDs
 *    Metrics: RED (Rate, Errors, Duration)
 *    Traces:  distributed tracing (Zipkin, Jaeger)
 *
 * 8. Infrastructure Automation:
 *    CI/CD pipelines, containerization (Docker), orchestration (K8s).
 *    Each service is independently deployable.
 *
 * Common interview question: "When should you NOT use microservices?"
 *   - Small team (< 5 devs) → monolith is simpler
 *   - New product (unknown domain) → boundaries unclear
 *   - Low complexity → overhead not justified
 *   - Tight consistency requirements → distributed transactions are hard
 *
 * Monolith → Microservices migration:
 *   1. Strangler Fig pattern: gradually replace monolith endpoints
 *   2. Start with the most independent module
 *   3. Extract one service at a time, keep monolith running
 */
public class MicroservicesDesignPrinciples {

    public static void main(String[] args) {
        System.out.println("=== Microservices Design Principles ===\n");

        System.out.println("1. Single Responsibility — one service, one capability");
        System.out.println("2. Database per Service — no shared DB");
        System.out.println("3. API First — design contract before code");
        System.out.println("4. Smart Endpoints, Dumb Pipes — logic in services");
        System.out.println("5. Design for Failure — circuit breakers, retries");
        System.out.println("6. Decentralized Governance — right tool per service");
        System.out.println("7. Observability — logs, metrics, traces");
        System.out.println("8. Infrastructure Automation — CI/CD, Docker, K8s");

        System.out.println("\n=== When NOT to use microservices ===");
        System.out.println("- Small team (< 5 devs)");
        System.out.println("- New product (unknown domain boundaries)");
        System.out.println("- Low complexity");
        System.out.println("- Strong consistency requirements");
    }
}
