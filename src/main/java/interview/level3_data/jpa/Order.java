package interview.level3_data.jpa;

import interview.level2_web.rest.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Q3. How do you map relationships in JPA?
 *
 * | Annotation   | Relationship            | Example                        |
 * |-------------|-------------------------|-------------------------------|
 * | @OneToOne   | 1:1                      | User ↔ UserProfile             |
 * | @OneToMany  | 1:N (parent side)        | Order → List<OrderItem>       |
 * | @ManyToOne  | N:1 (child side)         | OrderItem → Order              |
 * | @ManyToMany | M:N                      | Student ↔ Course (join table)  |
 *
 * Owning side: the side with the foreign key (usually @ManyToOne side)
 * Inverse side: uses mappedBy (the @OneToMany side)
 *
 * CASCADE types:
 *   CascadeType.PERSIST → saving parent saves children
 *   CascadeType.MERGE   → updating parent updates children
 *   CascadeType.REMOVE  → deleting parent deletes children
 *   CascadeType.ALL     → all of the above + DETACH + REFRESH
 *
 * orphanRemoval = true:
 *   Removing a child from the collection DELETES it from DB
 *   (without this, it's just unlinked, not deleted)
 *
 * Q13. What is Lazy vs Eager loading?
 *
 * | Fetch type | When loaded            | Default for        |
 * |-----------|------------------------|-------------------|
 * | LAZY      | On first access        | @OneToMany, @ManyToMany |
 * | EAGER     | Immediately with parent| @ManyToOne, @OneToOne   |
 *
 * Best practice: Always use LAZY, fetch explicitly when needed (JOIN FETCH)
 *
 * curl test:
 *   curl -s http://localhost:8080/api/orders | jq
 *   curl -s http://localhost:8080/api/orders/1 | jq
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Orders belong to one User
    @ManyToOne(fetch = FetchType.LAZY)  // LAZY: don't load User until accessed
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // One Order has many OrderItems
    @OneToMany(
            mappedBy = "order",               // inverse side — OrderItem owns the FK
            cascade = CascadeType.ALL,         // save/delete items with order
            orphanRemoval = true,              // remove item from list → delete from DB
            fetch = FetchType.LAZY             // default for @OneToMany
    )
    private List<OrderItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    public Order() {}

    public Order(User user) {
        this.user = user;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        calculateTotal();
    }

    // Helper: add item to order (maintains both sides of relationship)
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);  // set the owning side
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void calculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
