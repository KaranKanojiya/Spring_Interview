package interview.level1_core.di;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q16. What is the Spring ApplicationContext?
 *
 * ApplicationContext is the CENTRAL interface of the Spring IoC container.
 * It extends BeanFactory with enterprise features.
 *
 * | Feature          | BeanFactory          | ApplicationContext        |
 * |-----------------|---------------------|--------------------------|
 * | Bean creation   | Lazy (on demand)     | Eager (at startup)        |
 * | AOP             | No                   | Yes                       |
 * | Event publishing| No                   | Yes (ApplicationEvent)    |
 * | i18n            | No                   | Yes (MessageSource)       |
 * | Environment     | No                   | Yes (profiles, properties)|
 * | Resource loading| No                   | Yes (classpath:, file:)   |
 *
 * Common implementations:
 *   AnnotationConfigApplicationContext → Java config (@Configuration)
 *   ClassPathXmlApplicationContext     → XML config (legacy)
 *   GenericWebApplicationContext       → Web applications
 *   SpringApplication.run()            → Spring Boot (auto-creates context)
 */
public class ApplicationContextDemo {

    @Component
    static class GreetingService {
        String greet(String name) { return "Hello, " + name + "!"; }
    }

    public static void main(String[] args) {
        System.out.println("=== ApplicationContext ===\n");

        // Create context with component scanning
        var ctx = new AnnotationConfigApplicationContext();
        ctx.scan("interview.level1_core.di");
        ctx.refresh();

        // Get bean by type
        GreetingService service = ctx.getBean(GreetingService.class);
        System.out.println("By type: " + service.greet("Karan"));

        // Get bean by name
        GreetingService byName = (GreetingService) ctx.getBean("applicationContextDemo.GreetingService");
        System.out.println("By name: " + byName.greet("Spring"));

        // Check if bean exists
        System.out.println("\ncontainsBean: " + ctx.containsBean("applicationContextDemo.GreetingService"));
        System.out.println("getBeanDefinitionCount: " + ctx.getBeanDefinitionCount());

        // Environment
        System.out.println("\nActive profiles: " + java.util.Arrays.toString(ctx.getEnvironment().getActiveProfiles()));

        // ApplicationContext is also a BeanFactory
        BeanFactory factory = ctx;
        System.out.println("ApplicationContext extends BeanFactory: " + (ctx instanceof BeanFactory));

        ctx.close();

        System.out.println("\n=== Spring Boot simplifies this ===");
        System.out.println("SpringApplication.run(App.class, args)");
        System.out.println("  → Creates ApplicationContext automatically");
        System.out.println("  → Registers auto-configuration");
        System.out.println("  → Starts embedded server (if web app)");
    }
}
