package interview.level3_data.jpa;

/**
 * Q11. What is JPA Auditing in Spring Data?
 *
 * Auditing automatically populates created/modified timestamps and user info.
 *
 * Setup:
 *   1. Add @EnableJpaAuditing on a @Configuration class
 *   2. Add @EntityListeners(AuditingEntityListener.class) on entity
 *   3. Use auditing annotations on fields:
 *
 *   @CreatedDate     → set on insert (LocalDateTime/Instant)
 *   @LastModifiedDate → set on insert and update
 *   @CreatedBy       → who created (requires AuditorAware bean)
 *   @LastModifiedBy   → who last modified
 *
 * Example:
 *   @Entity
 *   @EntityListeners(AuditingEntityListener.class)
 *   class Product {
 *       @CreatedDate
 *       private LocalDateTime createdAt;
 *
 *       @LastModifiedDate
 *       private LocalDateTime updatedAt;
 *
 *       @CreatedBy
 *       private String createdBy;
 *   }
 *
 * AuditorAware (for @CreatedBy/@LastModifiedBy):
 *   @Bean
 *   AuditorAware<String> auditorProvider() {
 *       return () -> Optional.of(SecurityContextHolder.getContext()
 *           .getAuthentication().getName());
 *   }
 *
 * Alternative (used in Product.java): @PrePersist / @PreUpdate callbacks
 *   → Manual approach, no Spring dependency, works with plain JPA
 */
public class AuditingExplained {

    public static void main(String[] args) {
        System.out.println("=== JPA Auditing ===\n");

        System.out.println("=== Spring Data Auditing annotations ===");
        System.out.println("@CreatedDate      → auto-set on insert");
        System.out.println("@LastModifiedDate  → auto-set on insert + update");
        System.out.println("@CreatedBy         → who created (needs AuditorAware bean)");
        System.out.println("@LastModifiedBy    → who modified (needs AuditorAware bean)");

        System.out.println("\n=== Alternative: JPA callbacks ===");
        System.out.println("@PrePersist  → runs before INSERT");
        System.out.println("@PreUpdate   → runs before UPDATE");
        System.out.println("@PostPersist → runs after INSERT");
        System.out.println("@PostLoad    → runs after SELECT");
        System.out.println("→ Used in Product.java (no Spring dependency)");

        System.out.println("\n=== Setup ===");
        System.out.println("1. @EnableJpaAuditing on config class");
        System.out.println("2. @EntityListeners(AuditingEntityListener.class) on entity");
        System.out.println("3. AuditorAware<String> bean for @CreatedBy");
    }
}
