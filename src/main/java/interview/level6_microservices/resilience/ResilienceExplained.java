package interview.level6_microservices.resilience;

/**
 * Q1. What is the Circuit Breaker pattern and why is it needed?
 *
 * Problem: Service A calls Service B. If B is down, A keeps retrying → cascade failure.
 * Solution: Circuit Breaker — stops calling B when failures exceed threshold.
 *
 * States:
 *   CLOSED  → normal operation, requests pass through
 *   OPEN    → failures exceeded threshold → requests fail fast (no call to B)
 *   HALF_OPEN → after wait duration, allows limited requests to test if B recovered
 *
 *   CLOSED → (failures > threshold) → OPEN → (wait) → HALF_OPEN → (success) → CLOSED
 *                                                        ↓ (failure)
 *                                                       OPEN
 *
 * Q2. What resilience patterns does Resilience4j provide?
 *
 * | Pattern        | Purpose                                    |
 * |---------------|-------------------------------------------|
 * | CircuitBreaker | Stop calling failing service               |
 * | Retry          | Retry failed calls (transient errors)      |
 * | RateLimiter    | Limit calls per second                     |
 * | Bulkhead       | Limit concurrent calls (isolate failures)  |
 * | TimeLimiter    | Timeout for slow calls                     |
 * | Fallback       | Default response when service unavailable  |
 *
 * Configuration (application.yml):
 *   resilience4j:
 *     circuitbreaker:
 *       instances:
 *         userService:
 *           failure-rate-threshold: 50        # open after 50% failures
 *           wait-duration-in-open-state: 5s   # stay open for 5s
 *           sliding-window-size: 10           # evaluate last 10 calls
 *     retry:
 *       instances:
 *         userService:
 *           max-attempts: 3
 *           wait-duration: 1s
 *     ratelimiter:
 *       instances:
 *         userService:
 *           limit-for-period: 10             # 10 calls per period
 *           limit-refresh-period: 1s
 *     bulkhead:
 *       instances:
 *         userService:
 *           max-concurrent-calls: 5
 */
public class ResilienceExplained {

    public static void main(String[] args) {
        System.out.println("=== Resilience4j Patterns ===\n");

        System.out.println("=== Circuit Breaker ===");
        System.out.println("CLOSED → failures exceed threshold → OPEN → wait → HALF_OPEN → test → CLOSED/OPEN");
        System.out.println("Prevents cascade failures when downstream service is down\n");

        System.out.println("=== Retry ===");
        System.out.println("Automatically retry failed calls (good for transient network errors)");
        System.out.println("Configure: max-attempts, wait-duration, retryExceptions\n");

        System.out.println("=== Rate Limiter ===");
        System.out.println("Limit calls per time window (protect downstream from overload)");
        System.out.println("Configure: limit-for-period, limit-refresh-period\n");

        System.out.println("=== Bulkhead ===");
        System.out.println("Limit concurrent calls (isolate slow services from consuming all threads)");
        System.out.println("Two types: Semaphore (thread limit) or ThreadPool (separate pool)\n");

        System.out.println("=== Combining patterns ===");
        System.out.println("Order matters! Typical: Retry → CircuitBreaker → RateLimiter → Bulkhead");
        System.out.println("@Retry(name=\"svc\") @CircuitBreaker(name=\"svc\", fallbackMethod=\"fb\")");
    }
}
