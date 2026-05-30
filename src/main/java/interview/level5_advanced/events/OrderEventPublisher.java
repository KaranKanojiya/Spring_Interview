package interview.level5_advanced.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Q11. What is the Spring Event system?
 *
 * Spring's ApplicationEvent provides a publish-subscribe mechanism for
 * decoupling components within the application.
 *
 * Flow:
 *   1. Define event (extends ApplicationEvent or any object since Spring 4.2)
 *   2. Publisher injects ApplicationEventPublisher and calls publishEvent()
 *   3. Listener annotates method with @EventListener
 *
 * Q12. What is @Async and how does it work with events?
 *
 * @Async makes a method execute in a separate thread.
 *   - Requires @EnableAsync on a @Configuration class
 *   - Method must be public (AOP proxy)
 *   - Return void or CompletableFuture<T>
 *
 * @Async + @EventListener = non-blocking event processing
 *   - Publisher doesn't wait for listener
 *   - Listener runs in a background thread
 *
 * Without @Async: events are SYNCHRONOUS (listener blocks publisher)
 */

// === Event (plain POJO since Spring 4.2) ===
record OrderCreatedEvent(Long orderId, String customerName, double totalAmount) {}

// === Publisher ===
@Service
class OrderEventService {
    private final ApplicationEventPublisher eventPublisher;

    OrderEventService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createOrder(Long orderId, String customer, double total) {
        // ... save order to DB ...
        System.out.println("[Publisher] Order created: " + orderId);

        // Publish event — listeners react automatically
        eventPublisher.publishEvent(new OrderCreatedEvent(orderId, customer, total));
    }
}

// === Listeners ===
@Component
class EmailNotificationListener {
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        System.out.printf("[Listener-Email] Sending confirmation for order %d to %s ($%.2f)%n",
                event.orderId(), event.customerName(), event.totalAmount());
    }
}

@Component
class InventoryListener {
    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        System.out.printf("[Listener-Inventory] Reserving items for order %d%n", event.orderId());
    }
}

// Note: @Async listeners would need @EnableAsync on a config class
// @Component
// class AsyncAnalyticsListener {
//     @Async
//     @EventListener
//     public void onOrderCreated(OrderCreatedEvent event) {
//         System.out.println("[Async-Analytics] Processing in background thread: " + Thread.currentThread().getName());
//     }
// }

@Component
public class OrderEventPublisher {
    // Marker class for component scanning — actual logic is in OrderEventService
}
