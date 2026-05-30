package interview.level3_data.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Q1. What is JPA and how does Hibernate relate to it?
 *
 * JPA (Jakarta Persistence API):
 *   - SPECIFICATION (interface) for ORM in Java
 *   - Defines annotations: @Entity, @Table, @Id, @Column, @OneToMany, etc.
 *   - Does NOT implement anything — it's just a contract
 *
 * Hibernate:
 *   - IMPLEMENTATION of JPA (the actual ORM engine)
 *   - Spring Boot auto-configures Hibernate as the JPA provider
 *   - Other implementations: EclipseLink, OpenJPA (rare)
 *
 * Spring Data JPA:
 *   - Abstraction OVER JPA — provides repositories, query methods, pagination
 *   - You write interfaces, Spring generates the implementation
 *
 * Stack: Spring Data JPA → JPA (spec) → Hibernate (impl) → JDBC → Database
 *
 * Q2. What are the key JPA annotations for entity mapping?
 *
 * @Entity          → marks class as a JPA entity (maps to a DB table)
 * @Table           → customize table name, schema, indexes
 * @Id              → primary key
 * @GeneratedValue  → auto-generate PK (IDENTITY, SEQUENCE, TABLE, AUTO)
 * @Column          → customize column name, nullable, length, unique
 * @Enumerated      → map Java enum to DB column
 * @Temporal        → map Date/Calendar (not needed for java.time types)
 * @Transient       → field NOT persisted to DB
 * @Lob             → large object (CLOB/BLOB)
 *
 * curl test:
 *   curl -s http://localhost:8080/api/products | jq
 *   curl -s -X POST http://localhost:8080/api/products \
 *     -H "Content-Type: application/json" \
 *     -d '{"name":"Laptop","price":999.99,"category":"ELECTRONICS"}' | jq
 */
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_category", columnList = "category"),
        @Index(name = "idx_product_name", columnList = "name")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)  // stores "ELECTRONICS", not 0/1/2
    @Column(nullable = false)
    private Category category;

    @Column(length = 500)
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient  // NOT persisted — computed field
    private String displayName;

    public enum Category {
        ELECTRONICS, BOOKS, CLOTHING, FOOD, OTHER
    }

    public Product() {}

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
