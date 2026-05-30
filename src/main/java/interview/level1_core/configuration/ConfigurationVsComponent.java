package interview.level1_core.configuration;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q8. What is @Configuration and how does it differ from @Component?
 *
 * @Configuration is a special @Component that uses CGLIB proxying.
 *
 * Key difference:
 *   @Configuration: @Bean methods are proxied — calling one @Bean method from another
 *                   returns the SAME singleton instance (proxy intercepts the call)
 *
 *   @Component:     @Bean methods are NOT proxied — calling one @Bean method from another
 *                   creates a NEW instance each time (no interception)
 *
 * This is called "full mode" (@Configuration) vs "lite mode" (@Component).
 *
 * proxyBeanMethods:
 *   @Configuration(proxyBeanMethods = true)  → default, full CGLIB proxy
 *   @Configuration(proxyBeanMethods = false) → lite mode (faster startup, no proxy)
 */
public class ConfigurationVsComponent {

    static class DataSource {
        private static int counter = 0;
        private final int id;
        DataSource() { this.id = ++counter; }
        @Override
        public String toString() { return "DataSource#" + id; }
    }

    static class UserRepository {
        private final DataSource dataSource;
        UserRepository(DataSource ds) { this.dataSource = ds; }
        @Override
        public String toString() { return "UserRepo using " + dataSource; }
    }

    // === @Configuration: CGLIB proxy — same DataSource instance ===
    @Configuration
    static class FullModeConfig {
        @Bean
        DataSource fullDataSource() { return new DataSource(); }

        @Bean
        UserRepository fullUserRepository() {
            // This calls fullDataSource() — proxy intercepts and returns singleton
            return new UserRepository(fullDataSource());
        }
    }

    // === @Component: NO proxy — different DataSource instance! ===
    @Component
    static class LiteModeConfig {
        @Bean
        DataSource liteDataSource() { return new DataSource(); }

        @Bean
        UserRepository liteUserRepository() {
            // This calls liteDataSource() — NO proxy, creates NEW instance!
            return new UserRepository(liteDataSource());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== @Configuration (Full Mode — CGLIB Proxy) ===");
        var fullCtx = new AnnotationConfigApplicationContext(FullModeConfig.class);
        DataSource fullDs = fullCtx.getBean(DataSource.class);
        UserRepository fullRepo = fullCtx.getBean(UserRepository.class);
        System.out.println("DataSource bean: " + fullDs);
        System.out.println("UserRepo's DataSource: " + fullRepo);
        System.out.println("Same instance? YES — proxy ensures singleton\n");
        fullCtx.close();

        System.out.println("=== @Component (Lite Mode — No Proxy) ===");
        var liteCtx = new AnnotationConfigApplicationContext();
        liteCtx.register(LiteModeConfig.class);
        liteCtx.refresh();
        DataSource liteDs = liteCtx.getBean(DataSource.class);
        UserRepository liteRepo = liteCtx.getBean(UserRepository.class);
        System.out.println("DataSource bean: " + liteDs);
        System.out.println("UserRepo's DataSource: " + liteRepo);
        System.out.println("Same instance? NO — no proxy, new instance created");
        liteCtx.close();

        System.out.println("\n=== Rule ===");
        System.out.println("Use @Configuration when @Bean methods call each other");
        System.out.println("Use @Component with @Bean only for independent beans");
    }
}
