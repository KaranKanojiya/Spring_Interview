package interview.level1_core.di;

/**
 * Q14. What happens when Spring encounters a circular dependency?
 *
 * Circular dependency: A depends on B, and B depends on A.
 *
 * Behavior by injection type:
 *
 * Constructor Injection:
 *   → BeanCurrentlyInCreationException at startup (GOOD — fails fast!)
 *   → Spring cannot create A without B, and B without A
 *
 * Setter/Field Injection (Spring < 2.6):
 *   → Resolved using THREE-LEVEL CACHE:
 *     L1: singletonObjects (fully initialized beans)
 *     L2: earlySingletonObjects (partially initialized, but injectable)
 *     L3: singletonFactories (ObjectFactory that creates early references)
 *   → Spring injects an early (incomplete) reference to break the cycle
 *
 * Spring Boot 2.6+:
 *   → Circular dependencies PROHIBITED by default
 *   → spring.main.allow-circular-references=false (default)
 *   → Set to true to allow (NOT recommended)
 *
 * Solutions:
 *   1. Refactor: extract shared logic into a third bean (BEST)
 *   2. @Lazy on one dependency (delays creation)
 *   3. Use events or interfaces to decouple
 *   4. Use setter injection (last resort, hides the problem)
 */
public class CircularDependencyDemo {

    // === Circular dependency example ===
    // class ServiceA {
    //     private final ServiceB serviceB;
    //     ServiceA(ServiceB serviceB) { this.serviceB = serviceB; } // needs B
    // }
    // class ServiceB {
    //     private final ServiceA serviceA;
    //     ServiceB(ServiceA serviceA) { this.serviceA = serviceA; } // needs A → BOOM!
    // }

    // === Solution 1: Extract shared logic ===
    static class SharedLogic {
        String compute() { return "shared result"; }
    }

    static class ServiceA {
        private final SharedLogic shared;
        ServiceA(SharedLogic shared) { this.shared = shared; }
    }

    static class ServiceB {
        private final SharedLogic shared;
        ServiceB(SharedLogic shared) { this.shared = shared; }
    }

    // === Solution 2: @Lazy ===
    // class ServiceA {
    //     private final ServiceB serviceB;
    //     ServiceA(@Lazy ServiceB serviceB) { this.serviceB = serviceB; }
    //     // Spring injects a PROXY, actual B is created on first use
    // }

    public static void main(String[] args) {
        System.out.println("=== Circular Dependency in Spring ===\n");

        System.out.println("=== The Problem ===");
        System.out.println("ServiceA needs ServiceB in constructor");
        System.out.println("ServiceB needs ServiceA in constructor");
        System.out.println("→ BeanCurrentlyInCreationException (constructor injection)");
        System.out.println("→ Silently resolved with setter/field injection (hidden bug)");

        System.out.println("\n=== Spring Boot 2.6+ ===");
        System.out.println("Circular dependencies are BANNED by default");
        System.out.println("spring.main.allow-circular-references=false");

        System.out.println("\n=== Solutions (best to worst) ===");
        System.out.println("1. REFACTOR: Extract shared logic into third bean");
        System.out.println("   A → Shared ← B (no cycle)");
        System.out.println("2. @Lazy: ServiceA(@Lazy ServiceB b) → proxy injected");
        System.out.println("3. Events: A publishes event, B listens (decoupled)");
        System.out.println("4. Setter injection: allows cycle (NOT recommended)");

        System.out.println("\n=== Three-Level Cache (how setter injection resolves it) ===");
        System.out.println("L1: singletonObjects       — fully initialized beans");
        System.out.println("L2: earlySingletonObjects   — partially initialized (early ref)");
        System.out.println("L3: singletonFactories      — ObjectFactory for creating early refs");
        System.out.println("Spring exposes A's early ref in L3, creates B with that ref,");
        System.out.println("then finishes initializing A. Both now have valid references.");
    }
}
