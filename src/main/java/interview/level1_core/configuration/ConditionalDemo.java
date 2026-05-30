package interview.level1_core.configuration;

import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;

/**
 * Q17. What is @Conditional and how does Spring Boot use it?
 *
 * @Conditional creates beans ONLY when a condition is met.
 * This is the backbone of Spring Boot auto-configuration.
 *
 * Spring Boot @Conditional variants:
 *
 * | Annotation                     | Bean created when...                    |
 * |-------------------------------|----------------------------------------|
 * | @ConditionalOnClass            | Class is on classpath                   |
 * | @ConditionalOnMissingClass     | Class is NOT on classpath               |
 * | @ConditionalOnBean             | Another specific bean exists             |
 * | @ConditionalOnMissingBean      | No bean of this type exists (back-off)  |
 * | @ConditionalOnProperty         | Property has specific value              |
 * | @ConditionalOnWebApplication   | Running as a web application             |
 * | @ConditionalOnNotWebApplication| NOT running as a web app                 |
 * | @ConditionalOnExpression       | SpEL expression evaluates to true        |
 *
 * Auto-configuration pattern:
 *   @ConditionalOnClass(DataSource.class)           → JDBC driver present
 *   @ConditionalOnMissingBean(DataSource.class)     → user hasn't defined their own
 *   → Spring creates default HikariCP DataSource
 *
 * If you define your own @Bean DataSource → auto-config backs off!
 */
public class ConditionalDemo {

    interface CacheService {
        String getType();
    }

    static class RedisCacheService implements CacheService {
        @Override
        public String getType() { return "Redis Cache"; }
    }

    static class InMemoryCacheService implements CacheService {
        @Override
        public String getType() { return "In-Memory Cache (default)"; }
    }

    @Configuration
    static class CacheConfig {

        // Only create if property is set
        @Bean
        @ConditionalOnProperty(name = "cache.type", havingValue = "redis")
        CacheService redisCacheService() {
            return new RedisCacheService();
        }

        // Back-off: only if no other CacheService exists
        @Bean
        @ConditionalOnMissingBean(CacheService.class)
        CacheService defaultCacheService() {
            return new InMemoryCacheService();
        }
    }

    public static void main(String[] args) {
        // Without redis property → InMemoryCacheService (default)
        System.out.println("=== @Conditional Demo ===\n");

        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(CacheConfig.class);
        ctx.refresh();

        CacheService cache = ctx.getBean(CacheService.class);
        System.out.println("Active cache: " + cache.getType());
        System.out.println("(Redis not configured → default in-memory used)\n");
        ctx.close();

        System.out.println("=== Auto-configuration pattern ===");
        System.out.println("@Configuration");
        System.out.println("@ConditionalOnClass(DataSource.class)");
        System.out.println("@ConditionalOnMissingBean(DataSource.class)");
        System.out.println("public class DataSourceAutoConfiguration {");
        System.out.println("    @Bean DataSource dataSource() { return new HikariDataSource(); }");
        System.out.println("}");
        System.out.println("\n→ If you define your own @Bean DataSource, auto-config backs off!");
    }
}
