package interview.level1_core.annotations;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q5. What is the difference between @Bean and @Component?
 *
 * | Feature        | @Component                 | @Bean                          |
 * |---------------|---------------------------|-------------------------------|
 * | Target        | Class-level                | Method-level (in @Configuration) |
 * | Discovery     | Component scanning         | Explicit declaration           |
 * | Use case      | Your own classes           | Third-party library classes    |
 * | Customization | Limited (constructor args)  | Full control over instantiation |
 * | Naming        | Class name (lowercase)     | Method name (or @Bean("name")) |
 *
 * Rule of thumb:
 *   @Component → classes you own and control
 *   @Bean      → classes you DON'T own (RestTemplate, ObjectMapper, DataSource)
 */
public class BeanVsComponent {

    // === @Component: auto-detected by component scanning ===
    @Component
    static class MyService {
        String execute() { return "Executed by @Component bean"; }
    }

    // === @Bean: explicit declaration in @Configuration ===
    // Imagine this is a third-party class you can't annotate
    static class ExternalLibraryClient {
        private final String baseUrl;
        ExternalLibraryClient(String baseUrl) { this.baseUrl = baseUrl; }
        String call() { return "Called external API at " + baseUrl; }
    }

    @Configuration
    static class AppConfig {
        @Bean
        ExternalLibraryClient externalClient() {
            // Full control over instantiation — can pass config, do setup
            return new ExternalLibraryClient("https://api.example.com");
        }

        @Bean("customNamedBean")
        ExternalLibraryClient anotherClient() {
            return new ExternalLibraryClient("https://api2.example.com");
        }
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.scan("interview.level1_core.annotations");
        ctx.refresh();

        System.out.println("=== @Component vs @Bean ===\n");

        // @Component bean
        System.out.println("@Component: " + ctx.getBean(MyService.class).execute());

        // @Bean — default name is method name
        System.out.println("@Bean (default name): " + ctx.getBean("externalClient", ExternalLibraryClient.class).call());

        // @Bean with custom name
        System.out.println("@Bean (custom name): " + ctx.getBean("customNamedBean", ExternalLibraryClient.class).call());

        ctx.close();

        System.out.println("\n=== When to use which ===");
        System.out.println("@Component: UserService, OrderRepository (your code)");
        System.out.println("@Bean:      RestTemplate, ObjectMapper, DataSource (library code)");
    }
}
