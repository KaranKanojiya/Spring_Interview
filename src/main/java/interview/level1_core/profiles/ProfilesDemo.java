package interview.level1_core.profiles;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q9. What are Spring Profiles and how do you use them?
 *
 * Profiles let you register beans conditionally based on the active environment.
 *
 * Activating profiles:
 *   1. application.properties: spring.profiles.active=dev
 *   2. Environment variable: SPRING_PROFILES_ACTIVE=dev
 *   3. Command line: --spring.profiles.active=dev
 *   4. Programmatically: ctx.getEnvironment().setActiveProfiles("dev")
 *
 * Profile-specific config files:
 *   application-dev.properties    → loaded when "dev" profile active
 *   application-prod.properties   → loaded when "prod" profile active
 *   application.properties        → always loaded (defaults)
 *
 * @Profile on beans:
 *   @Profile("dev")       → only in dev
 *   @Profile("!prod")     → in all profiles EXCEPT prod
 *   @Profile({"dev","qa"})→ in dev OR qa
 */
public class ProfilesDemo {

    interface DataSource {
        String getConnection();
    }

    @Component
    @Profile("dev")
    static class H2DataSource implements DataSource {
        @Override
        public String getConnection() { return "H2 in-memory (dev)"; }
    }

    @Component
    @Profile("prod")
    static class PostgresDataSource implements DataSource {
        @Override
        public String getConnection() { return "PostgreSQL (prod)"; }
    }

    @Component
    @Profile("!prod")  // active in ALL profiles except prod
    static class DebugLogger {
        String log() { return "Debug logging enabled (not in prod)"; }
    }

    public static void main(String[] args) {
        // === Dev profile ===
        System.out.println("=== Activating 'dev' profile ===");
        var devCtx = new AnnotationConfigApplicationContext();
        devCtx.getEnvironment().setActiveProfiles("dev");
        devCtx.scan("interview.level1_core.profiles");
        devCtx.refresh();

        System.out.println("DataSource: " + devCtx.getBean(DataSource.class).getConnection());
        System.out.println("DebugLogger: " + devCtx.getBean(DebugLogger.class).log());
        devCtx.close();

        // === Prod profile ===
        System.out.println("\n=== Activating 'prod' profile ===");
        var prodCtx = new AnnotationConfigApplicationContext();
        prodCtx.getEnvironment().setActiveProfiles("prod");
        prodCtx.scan("interview.level1_core.profiles");
        prodCtx.refresh();

        System.out.println("DataSource: " + prodCtx.getBean(DataSource.class).getConnection());
        try {
            prodCtx.getBean(DebugLogger.class);
        } catch (Exception e) {
            System.out.println("DebugLogger: not available in prod (@Profile(\"!prod\"))");
        }
        prodCtx.close();

        System.out.println("\n=== Profile activation methods ===");
        System.out.println("1. spring.profiles.active=dev (application.properties)");
        System.out.println("2. SPRING_PROFILES_ACTIVE=dev (env var)");
        System.out.println("3. --spring.profiles.active=dev (command line)");
        System.out.println("4. @ActiveProfiles(\"dev\") (in tests)");
    }
}
