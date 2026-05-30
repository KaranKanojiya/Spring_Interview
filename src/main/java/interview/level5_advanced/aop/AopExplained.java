package interview.level5_advanced.aop;

/**
 * Q4. How does Spring AOP work internally?
 *
 * Spring AOP uses RUNTIME PROXIES (not bytecode weaving like AspectJ).
 *
 * Two proxy types:
 *   1. JDK Dynamic Proxy — if target implements an interface
 *      → creates a proxy that implements the same interface
 *   2. CGLIB Proxy — if target has no interface (default in Spring Boot)
 *      → creates a subclass of the target class
 *
 * Limitation of proxy-based AOP:
 *   ❌ Self-invocation: this.method() bypasses the proxy → no AOP!
 *      (same problem as @Transactional and @Cacheable)
 *   ❌ Private methods: proxies can't intercept private methods
 *   ❌ Final classes/methods: CGLIB can't subclass final
 *
 * Spring AOP vs AspectJ:
 *   | Feature        | Spring AOP           | AspectJ              |
 *   |---------------|---------------------|---------------------|
 *   | Weaving       | Runtime (proxy)      | Compile/load-time    |
 *   | Performance   | Slight overhead      | No runtime overhead  |
 *   | Scope         | Spring beans only    | Any Java class       |
 *   | Self-invocation| Not intercepted     | Intercepted          |
 *   | Complexity    | Simple               | Complex (compiler)   |
 *
 * Q5. What are common use cases for AOP?
 *
 *   1. Logging / Auditing → @Around to log method entry/exit/duration
 *   2. Security → @Before to check permissions (@PreAuthorize is AOP internally)
 *   3. Transactions → @Transactional is implemented via AOP proxy
 *   4. Caching → @Cacheable is implemented via AOP proxy
 *   5. Exception handling → @AfterThrowing to log/notify on errors
 *   6. Performance monitoring → @Around to measure execution time
 *   7. Retry logic → @Around to retry on specific exceptions
 */
public class AopExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring AOP Internals ===\n");

        System.out.println("=== Proxy types ===");
        System.out.println("JDK Dynamic Proxy: target implements interface");
        System.out.println("CGLIB Proxy:       target has no interface (default)");

        System.out.println("\n=== Limitations ===");
        System.out.println("1. Self-invocation (this.method()) bypasses proxy");
        System.out.println("2. Private methods not intercepted");
        System.out.println("3. Final classes/methods can't be proxied by CGLIB");

        System.out.println("\n=== AOP in disguise (things that use AOP internally) ===");
        System.out.println("@Transactional  → TransactionInterceptor");
        System.out.println("@Cacheable      → CacheInterceptor");
        System.out.println("@Async          → AsyncAnnotationBeanPostProcessor");
        System.out.println("@PreAuthorize   → MethodSecurityInterceptor");
        System.out.println("@Retryable      → RetryOperationsInterceptor");
    }
}
