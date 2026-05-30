package interview.level5_advanced.events;

/**
 * Q11/Q12 continued — conceptual overview.
 *
 * Built-in Spring Events:
 *   ContextRefreshedEvent    → ApplicationContext initialized or refreshed
 *   ContextStartedEvent      → ApplicationContext started
 *   ContextStoppedEvent      → ApplicationContext stopped
 *   ContextClosedEvent       → ApplicationContext closed
 *   RequestHandledEvent      → HTTP request processed (web apps)
 *
 * @TransactionalEventListener:
 *   Runs after the current transaction completes.
 *   Phases:
 *     AFTER_COMMIT (default)  → only if transaction committed
 *     AFTER_ROLLBACK          → only if transaction rolled back
 *     AFTER_COMPLETION        → after commit or rollback
 *     BEFORE_COMMIT           → before commit
 *
 *   Use case: Send email ONLY after order is committed to DB
 *   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
 *   public void sendEmail(OrderCreatedEvent event) { ... }
 *
 * Events vs Direct Method Call:
 *   | Feature      | Direct call         | Events              |
 *   |-------------|--------------------|--------------------|
 *   | Coupling    | Tight (A calls B)  | Loose (A publishes) |
 *   | Adding      | Modify A           | Add new listener    |
 *   | Testing     | Mock B in A        | Test listeners independently |
 *   | Transactions| Same TX            | Can be separate TX  |
 */
public class EventsExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Events ===\n");

        System.out.println("=== Custom events ===");
        System.out.println("1. Define: record OrderCreatedEvent(Long id, String name) {}");
        System.out.println("2. Publish: eventPublisher.publishEvent(new OrderCreatedEvent(...))");
        System.out.println("3. Listen: @EventListener void onOrder(OrderCreatedEvent e) {}\n");

        System.out.println("=== @TransactionalEventListener ===");
        System.out.println("AFTER_COMMIT:   send email only if order saved successfully");
        System.out.println("AFTER_ROLLBACK: notify monitoring if order failed");

        System.out.println("\n=== @Async events ===");
        System.out.println("@Async + @EventListener → listener runs in background thread");
        System.out.println("Requires @EnableAsync on config class");
    }
}
