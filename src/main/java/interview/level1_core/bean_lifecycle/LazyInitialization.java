package interview.level1_core.bean_lifecycle;

import org.springframework.context.annotation.*;

/**
 * Q15. What is @Lazy and when should you use it?
 *
 * Default: Spring creates ALL singleton beans at startup (eager initialization).
 *   Pro: Fails fast if there's a configuration error
 *   Con: Slow startup if many heavy beans
 *
 * @Lazy: Defers bean creation until FIRST ACCESS.
 *
 * Apply at:
 *   @Lazy on @Bean method     → that specific bean is lazy
 *   @Lazy on @Component class → that component is lazy
 *   @Lazy on @Autowired       → injects a lazy proxy
 *   spring.main.lazy-initialization=true → ALL beans lazy (global)
 *
 * When to use:
 *   ✅ Expensive bean that's rarely used
 *   ✅ Breaking circular dependencies (temporary fix with @Lazy on one side)
 *   ✅ Faster startup in dev/test (global lazy)
 *
 * When NOT to use:
 *   ❌ Production — delays failure detection to runtime
 *   ❌ Beans that should fail fast on bad config
 */
public class LazyInitialization {

    static class EagerBean {
        EagerBean() { System.out.println("  EagerBean created at STARTUP"); }
        void use() { System.out.println("  EagerBean used"); }
    }

    static class LazyBean {
        LazyBean() { System.out.println("  LazyBean created on FIRST ACCESS"); }
        void use() { System.out.println("  LazyBean used"); }
    }

    @Configuration
    static class LazyConfig {
        @Bean
        EagerBean eagerBean() { return new EagerBean(); }

        @Bean
        @Lazy
        LazyBean lazyBean() { return new LazyBean(); }
    }

    public static void main(String[] args) {
        System.out.println("=== @Lazy Initialization ===\n");

        System.out.println("--- Creating ApplicationContext ---");
        var ctx = new AnnotationConfigApplicationContext(LazyConfig.class);
        System.out.println("--- Context created ---\n");

        System.out.println("Notice: EagerBean was created at startup, LazyBean was NOT\n");

        System.out.println("--- Now accessing LazyBean ---");
        ctx.getBean(LazyBean.class).use();

        ctx.close();

        System.out.println("\n=== Global lazy initialization ===");
        System.out.println("spring.main.lazy-initialization=true → ALL beans lazy");
        System.out.println("Good for dev/test (faster startup)");
        System.out.println("Bad for prod (hides config errors until runtime)");
    }
}
