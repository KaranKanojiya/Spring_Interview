package interview.level6_microservices.messaging;

/**
 * Q10. What is event-driven architecture in microservices?
 *
 * Synchronous (REST):
 *   Order Service → calls → Payment Service → calls → Inventory Service
 *   Problem: tight coupling, cascade failures, high latency
 *
 * Asynchronous (Events):
 *   Order Service → publishes "OrderCreated" event → Message Broker
 *   Payment Service ← subscribes → processes payment
 *   Inventory Service ← subscribes → reserves stock
 *   Email Service ← subscribes → sends confirmation
 *   → Loose coupling, independent scaling, fault tolerant
 *
 * Q11. Kafka vs RabbitMQ — when to use which?
 *
 * | Feature       | Kafka                    | RabbitMQ                |
 * |--------------|--------------------------|-------------------------|
 * | Model        | Distributed log (append) | Message queue (AMQP)     |
 * | Ordering     | Per partition            | Per queue                |
 * | Throughput   | Very high (millions/sec) | High (thousands/sec)     |
 * | Retention    | Configurable (days/weeks)| Deleted after consume    |
 * | Replay       | Yes (consumers can replay)| No                      |
 * | Use case     | Event streaming, logs    | Task queues, RPC         |
 * | Consumer     | Pull-based               | Push-based               |
 * | Complexity   | Higher (partitions, offsets)| Lower (simpler setup) |
 *
 * Use Kafka: event sourcing, log aggregation, high-throughput streaming
 * Use RabbitMQ: task queues, request-reply, lower throughput, simpler setup
 *
 * Q12. What is the Saga pattern?
 *
 * Problem: Distributed transactions across microservices.
 *   Order Service (DB1) + Payment Service (DB2) + Inventory Service (DB3)
 *   Can't use 2PC (two-phase commit) — too slow, locks resources.
 *
 * Solution: Saga — sequence of local transactions with compensating actions.
 *
 * Choreography (event-based):
 *   Order created → Payment processes → Inventory reserves → done
 *   If Inventory fails → Payment refunds (compensation) → Order cancelled
 *   → No central coordinator, services react to events
 *
 * Orchestration (command-based):
 *   Saga Orchestrator tells each service what to do:
 *     1. Command: "Process payment" → Payment Service
 *     2. Command: "Reserve inventory" → Inventory Service
 *     3. If failure: "Refund payment" → Payment Service (compensation)
 *   → Central coordinator manages the workflow
 *
 * | Feature       | Choreography          | Orchestration           |
 * |--------------|----------------------|-------------------------|
 * | Coordinator  | None (events)         | Central orchestrator    |
 * | Coupling     | Loose                 | Moderate                |
 * | Complexity   | Hard to track flow    | Easy to understand      |
 * | Best for     | Simple, few services  | Complex, many services  |
 */
public class EventDrivenExplained {

    public static void main(String[] args) {
        System.out.println("=== Event-Driven Microservices ===\n");

        System.out.println("=== Sync vs Async ===");
        System.out.println("REST (sync): tight coupling, cascade failures");
        System.out.println("Events (async): loose coupling, independent scaling\n");

        System.out.println("=== Kafka vs RabbitMQ ===");
        System.out.println("Kafka:    high throughput, event streaming, replay");
        System.out.println("RabbitMQ: task queues, simpler setup, lower throughput\n");

        System.out.println("=== Saga Pattern ===");
        System.out.println("Choreography: services react to events (no coordinator)");
        System.out.println("Orchestration: central coordinator sends commands");
    }
}
