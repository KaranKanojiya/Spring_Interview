package interview.level1_core.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Q1. What is Inversion of Control (IoC) and Dependency Injection (DI)?
 *
 * IoC: The framework (Spring) controls object creation, not your code.
 *   WITHOUT IoC: OrderService service = new OrderService(new OrderRepository());
 *   WITH IoC:    Spring creates both and wires them together
 *
 * DI: The mechanism to implement IoC — dependencies are injected, not created.
 *
 * Three types of DI:
 *   1. Constructor Injection (RECOMMENDED)
 *   2. Setter Injection
 *   3. Field Injection (@Autowired on field — discouraged)
 */
public class IoCAndDIDemo {

    // === WITHOUT Spring (tight coupling) ===
    static class ManualExample {
        static class EmailService {
            void send(String msg) { System.out.println("  Email: " + msg); }
        }

        static class OrderService {
            private final EmailService emailService = new EmailService(); // tightly coupled!

            void placeOrder() {
                System.out.println("  Order placed");
                emailService.send("Order confirmation");
            }
        }
    }

    // === WITH Spring DI (loose coupling) ===

    interface NotificationService {
        void notify(String message);
    }

    @Component
    static class EmailNotificationService implements NotificationService {
        @Override
        public void notify(String message) {
            System.out.println("  [Spring DI] Email: " + message);
        }
    }

    // 1. Constructor Injection (RECOMMENDED)
    @Component
    static class OrderServiceConstructor {
        private final NotificationService notificationService;

        // @Autowired is optional when there's only one constructor (Spring 4.3+)
        OrderServiceConstructor(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        void placeOrder() {
            System.out.println("  [Constructor DI] Order placed");
            notificationService.notify("Order confirmed via constructor DI");
        }
    }

    // 2. Setter Injection
    @Component
    static class OrderServiceSetter {
        private NotificationService notificationService;

        @Autowired
        void setNotificationService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        void placeOrder() {
            System.out.println("  [Setter DI] Order placed");
            notificationService.notify("Order confirmed via setter DI");
        }
    }

    // 3. Field Injection (DISCOURAGED — hard to test)
    @Component
    static class OrderServiceField {
        @Autowired
        private NotificationService notificationService;

        void placeOrder() {
            System.out.println("  [Field DI] Order placed");
            notificationService.notify("Order confirmed via field DI");
        }
    }

    @Configuration
    static class AppConfig {
        // @Bean method — for classes you don't own or need custom instantiation
    }

    public static void main(String[] args) {
        System.out.println("=== WITHOUT Spring (manual wiring) ===");
        ManualExample.OrderService manual = new ManualExample.OrderService();
        manual.placeOrder();

        System.out.println("\n=== WITH Spring IoC Container ===");
        var ctx = new AnnotationConfigApplicationContext();
        ctx.register(IoCAndDIDemo.class);
        ctx.scan("interview.level1_core.di");
        ctx.refresh();

        System.out.println("\n--- Constructor Injection (recommended) ---");
        ctx.getBean(OrderServiceConstructor.class).placeOrder();

        System.out.println("\n--- Setter Injection ---");
        ctx.getBean(OrderServiceSetter.class).placeOrder();

        System.out.println("\n--- Field Injection (discouraged) ---");
        ctx.getBean(OrderServiceField.class).placeOrder();

        ctx.close();

        System.out.println("\n=== Why Constructor Injection is best ===");
        System.out.println("1. Fields can be final (immutable)");
        System.out.println("2. Dependencies are required (no NPE at runtime)");
        System.out.println("3. Easy to test (just pass mocks in constructor)");
        System.out.println("4. No reflection needed");
    }
}
