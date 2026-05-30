package interview.level6_microservices.tracing;

/**
 * Q7. What is Distributed Tracing and why is it important?
 *
 * Problem: User request hits API Gateway → Service A → Service B → Service C → DB.
 *   Request fails. Which service? Where? Logs are scattered across services.
 *
 * Solution: Distributed Tracing — track a single request across all services.
 *
 * Key concepts:
 *   Trace:  entire journey of a request (trace ID — same across all services)
 *   Span:   one unit of work in one service (span ID — unique per service)
 *   Parent: the span that triggered this span
 *
 *   Trace ID: abc123 (same everywhere)
 *   ├── Span 1: API Gateway (parent: none)
 *   ├── Span 2: User Service (parent: Span 1)
 *   ├── Span 3: Order Service (parent: Span 2)
 *   └── Span 4: DB Query (parent: Span 3)
 *
 * Spring implementation:
 *   Micrometer Tracing (replaced Spring Cloud Sleuth in Spring Boot 3):
 *     - Auto-generates trace ID + span ID
 *     - Propagates via HTTP headers (traceparent, b3)
 *     - Adds to MDC for logging: [traceId=abc123, spanId=def456]
 *
 *   Export to:
 *     - Zipkin (spring-boot-starter-actuator + micrometer-tracing-bridge-brave)
 *     - Jaeger
 *     - OpenTelemetry (standard)
 *
 * Q8. What is a Correlation ID?
 *
 * A unique ID that follows a request through all services.
 *   - Same as Trace ID in distributed tracing
 *   - Can be custom: X-Correlation-ID header
 *   - Include in all log lines for easy debugging:
 *     logging.pattern.level=%5p [${spring.application.name},traceId=%X{traceId},spanId=%X{spanId}]
 *
 * Q9. What metrics should you monitor in microservices?
 *
 * RED method:
 *   Rate:     requests per second
 *   Errors:   error rate (5xx / total)
 *   Duration: response time (p50, p95, p99)
 *
 * USE method (infrastructure):
 *   Utilization: CPU, memory, disk usage
 *   Saturation:  queue depth, thread pool usage
 *   Errors:      connection failures, timeouts
 *
 * Four Golden Signals (Google SRE):
 *   Latency, Traffic, Errors, Saturation
 */
public class DistributedTracingExplained {

    public static void main(String[] args) {
        System.out.println("=== Distributed Tracing ===\n");

        System.out.println("=== Problem ===");
        System.out.println("Request spans 5 services. Where did it fail?\n");

        System.out.println("=== Solution ===");
        System.out.println("Trace ID (same across all services) → follow the request");
        System.out.println("Span ID (unique per service) → identify the service\n");

        System.out.println("=== Spring Boot 3 ===");
        System.out.println("Micrometer Tracing (replaces Sleuth)");
        System.out.println("Export: Zipkin, Jaeger, OpenTelemetry\n");

        System.out.println("=== Monitoring ===");
        System.out.println("RED: Rate, Errors, Duration");
        System.out.println("Four Golden Signals: Latency, Traffic, Errors, Saturation");
    }
}
