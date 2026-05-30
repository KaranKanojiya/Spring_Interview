package interview.level1_core.bean_lifecycle;

import org.springframework.context.annotation.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Q3. What are the different bean scopes in Spring?
 *
 * | Scope       | Description                        | Default? |
 * |------------|------------------------------------|----------|
 * | singleton  | One instance per Spring container    | YES      |
 * | prototype  | New instance every time requested    | No       |
 * | request    | One per HTTP request (web only)      | No       |
 * | session    | One per HTTP session (web only)      | No       |
 * | application| One per ServletContext (web only)    | No       |
 *
 * Singleton: stateless services, repositories, controllers
 * Prototype: stateful objects, builders, anything that should not be shared
 *
 * IMPORTANT: Injecting prototype into singleton gives you ONE prototype instance
 * (created at singleton init time). Use Provider<T> or ObjectFactory<T> for fresh instances.
 */
public class BeanScopes {

    static class SingletonBean {
        private static int counter = 0;
        private final int id;
        SingletonBean() { this.id = ++counter; System.out.println("  SingletonBean created: #" + id); }
        int getId() { return id; }
    }

    static class PrototypeBean {
        private static int counter = 0;
        private final int id;
        PrototypeBean() { this.id = ++counter; System.out.println("  PrototypeBean created: #" + id); }
        int getId() { return id; }
    }

    @Configuration
    static class ScopeConfig {
        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
        SingletonBean singletonBean() { return new SingletonBean(); }

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
        PrototypeBean prototypeBean() { return new PrototypeBean(); }
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(ScopeConfig.class);

        // === Singleton: same instance every time ===
        System.out.println("=== Singleton Scope ===");
        SingletonBean s1 = ctx.getBean(SingletonBean.class);
        SingletonBean s2 = ctx.getBean(SingletonBean.class);
        System.out.println("s1 == s2: " + (s1 == s2));  // true — same instance
        System.out.println("s1.id: " + s1.getId() + ", s2.id: " + s2.getId());

        // === Prototype: new instance each time ===
        System.out.println("\n=== Prototype Scope ===");
        PrototypeBean p1 = ctx.getBean(PrototypeBean.class);
        PrototypeBean p2 = ctx.getBean(PrototypeBean.class);
        PrototypeBean p3 = ctx.getBean(PrototypeBean.class);
        System.out.println("p1 == p2: " + (p1 == p2));  // false — different instances
        System.out.println("IDs: " + p1.getId() + ", " + p2.getId() + ", " + p3.getId());

        // === GOTCHA: prototype inside singleton ===
        System.out.println("\n=== GOTCHA: Prototype inside Singleton ===");
        System.out.println("If you inject PrototypeBean into a SingletonBean,");
        System.out.println("you get ONE prototype instance (at singleton creation time).");
        System.out.println("Solution: Use Provider<PrototypeBean> or ObjectFactory<PrototypeBean>");
        System.out.println("to get a fresh prototype each time it's needed.");

        ctx.close();
    }
}
