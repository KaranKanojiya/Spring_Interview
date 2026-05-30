package interview.level1_core.di;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

/**
 * Q13. What is the difference between @Primary and @Qualifier?
 *
 * When multiple beans of the same type exist, Spring doesn't know which to inject.
 *
 * @Primary: marks ONE bean as the default choice
 *   - Used when most injection points want the same bean
 *   - Only ONE @Primary per type
 *
 * @Qualifier("name"): explicitly selects a specific bean
 *   - Used at the injection point
 *   - Overrides @Primary
 *
 * Precedence: @Qualifier > @Primary > bean name matching
 */
public class PrimaryVsQualifier {

    interface MessageSender {
        String send(String msg);
    }

    static class EmailSender implements MessageSender {
        @Override
        public String send(String msg) { return "Email: " + msg; }
    }

    static class SmsSender implements MessageSender {
        @Override
        public String send(String msg) { return "SMS: " + msg; }
    }

    static class SlackSender implements MessageSender {
        @Override
        public String send(String msg) { return "Slack: " + msg; }
    }

    @Configuration
    static class MessagingConfig {
        @Bean
        @Primary  // default when no @Qualifier specified
        MessageSender emailSender() { return new EmailSender(); }

        @Bean
        MessageSender smsSender() { return new SmsSender(); }

        @Bean("slack")
        MessageSender slackSender() { return new SlackSender(); }
    }

    // Uses @Primary (EmailSender)
    static class NotificationService {
        private final MessageSender sender;
        NotificationService(MessageSender sender) { this.sender = sender; }
        String notify(String msg) { return sender.send(msg); }
    }

    // Uses @Qualifier to override @Primary
    static class AlertService {
        private final MessageSender sender;
        AlertService(@Qualifier("smsSender") MessageSender sender) { this.sender = sender; }
        String alert(String msg) { return sender.send(msg); }
    }

    @Configuration
    static class ServiceConfig {
        @Bean
        NotificationService notificationService(MessageSender sender) {
            return new NotificationService(sender); // gets @Primary (email)
        }

        @Bean
        AlertService alertService(@Qualifier("smsSender") MessageSender sender) {
            return new AlertService(sender); // gets SMS (overrides @Primary)
        }
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(MessagingConfig.class, ServiceConfig.class);

        System.out.println("=== @Primary vs @Qualifier ===\n");
        System.out.println("NotificationService (uses @Primary):");
        System.out.println("  " + ctx.getBean(NotificationService.class).notify("Hello"));

        System.out.println("\nAlertService (uses @Qualifier(\"smsSender\")):");
        System.out.println("  " + ctx.getBean(AlertService.class).alert("Critical alert"));

        System.out.println("\nDirect lookup by name:");
        System.out.println("  " + ctx.getBean("slack", MessageSender.class).send("Channel message"));

        ctx.close();

        System.out.println("\n=== Precedence ===");
        System.out.println("1. @Qualifier(\"name\") — highest, explicit selection");
        System.out.println("2. @Primary — default when no qualifier");
        System.out.println("3. Bean name matching — fallback by variable name");
    }
}
