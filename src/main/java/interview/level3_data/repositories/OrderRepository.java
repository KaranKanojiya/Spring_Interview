package interview.level3_data.repositories;

import interview.level3_data.jpa.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Q6. What is the N+1 problem and how do you solve it?
 *
 * N+1 problem:
 *   1 query to fetch N orders
 *   + N queries to fetch each order's items (LAZY loaded one at a time)
 *   = N+1 total queries!
 *
 * Example:
 *   SELECT * FROM orders;                    → 1 query (10 orders)
 *   SELECT * FROM order_items WHERE order_id = 1;  → query 2
 *   SELECT * FROM order_items WHERE order_id = 2;  → query 3
 *   ...
 *   SELECT * FROM order_items WHERE order_id = 10; → query 11
 *   Total: 11 queries for 10 orders!
 *
 * Solutions:
 *   1. JOIN FETCH (JPQL) — fetch related entities in one query
 *   2. @EntityGraph — declarative join fetch
 *   3. @BatchSize — fetch N related collections in batches
 *   4. DTO projection — select only what you need
 *
 * curl test:
 *   curl -s http://localhost:8080/api/orders | jq
 *   → Watch console: JOIN FETCH = 1 query vs LAZY = N+1 queries
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    // === BAD: causes N+1 (items loaded lazily one by one) ===
    // List<Order> findAll();  // inherited — loads orders, then items one at a time

    // === GOOD: JOIN FETCH solves N+1 ===
    @Query("SELECT o FROM Order o JOIN FETCH o.items JOIN FETCH o.user")
    List<Order> findAllWithItemsAndUser();

    // === JOIN FETCH for single order ===
    @Query("SELECT o FROM Order o JOIN FETCH o.items JOIN FETCH o.user WHERE o.id = :id")
    Optional<Order> findByIdWithItems(Long id);

    // === Find orders by user ===
    List<Order> findByUserId(Long userId);

    // === Find orders by status ===
    List<Order> findByStatus(Order.OrderStatus status);
}
