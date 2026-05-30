package interview.level1_core.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q6. What is @Autowired and how does it work?
 *
 * @Autowired tells Spring to inject a dependency automatically.
 *
 * Resolution order:
 *   1. By TYPE — finds a bean matching the parameter/field type
 *   2. By QUALIFIER — if multiple beans of same type, uses @Qualifier("name")
 *   3. By NAME — if no qualifier, falls back to matching variable name to bean name
 *
 * Since Spring 4.3:
 *   If a class has ONLY ONE constructor, @Autowired is OPTIONAL on it.
 *
 * @Autowired(required = false):
 *   Injection is optional — bean may not exist. Field will be null.
 *   Better alternative: use Optional<T> or @Nullable
 */
public class AutowiredDemo {

    interface PaymentGateway {
        String process(double amount);
    }

    @Component("stripeGateway")
    static class StripeGateway implements PaymentGateway {
        @Override
        public String process(double amount) { return "Stripe: $" + amount; }
    }

    @Component("paypalGateway")
    static class PayPalGateway implements PaymentGateway {
        @Override
        public String process(double amount) { return "PayPal: $" + amount; }
    }

    // === Resolution by @Qualifier ===
    @Component
    static class PaymentServiceQualifier {
        private final PaymentGateway gateway;

        PaymentServiceQualifier(@Qualifier("stripeGateway") PaymentGateway gateway) {
            this.gateway = gateway;
        }

        String pay(double amount) { return gateway.process(amount); }
    }

    // === Resolution by parameter name ===
    @Component
    static class PaymentServiceByName {
        private final PaymentGateway paypalGateway; // name matches bean name "paypalGateway"

        PaymentServiceByName(PaymentGateway paypalGateway) {
            this.paypalGateway = paypalGateway;
        }

        String pay(double amount) { return paypalGateway.process(amount); }
    }

    // === Optional injection ===
    @Component
    static class NotificationService {
        @Autowired(required = false)
        private java.util.Optional<SmsService> smsService; // may not exist

        String sendNotification() {
            return smsService.map(s -> s.send()).orElse("SMS service not available");
        }
    }

    static class SmsService {
        String send() { return "SMS sent"; }
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.scan("interview.level1_core.di");
        ctx.refresh();

        System.out.println("=== @Autowired Resolution ===\n");

        System.out.println("By @Qualifier: " +
                ctx.getBean(PaymentServiceQualifier.class).pay(100));

        System.out.println("By name: " +
                ctx.getBean(PaymentServiceByName.class).pay(200));

        System.out.println("Optional (missing bean): " +
                ctx.getBean(NotificationService.class).sendNotification());

        ctx.close();
    }
}
