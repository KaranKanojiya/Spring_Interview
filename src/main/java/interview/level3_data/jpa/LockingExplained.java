package interview.level3_data.jpa;

/**
 * Q14. What is Optimistic vs Pessimistic locking?
 *
 * Problem: Two users edit the same record simultaneously → lost update.
 *
 * === Optimistic Locking (recommended for most apps) ===
 *
 * Uses a @Version field. No DB locks held.
 *   @Entity
 *   class Product {
 *       @Version
 *       private Integer version;
 *   }
 *
 * How it works:
 *   1. TX1 reads Product (version=1)
 *   2. TX2 reads Product (version=1)
 *   3. TX1 updates → UPDATE ... SET version=2 WHERE id=1 AND version=1 → OK
 *   4. TX2 updates → UPDATE ... SET version=2 WHERE id=1 AND version=1 → 0 rows! → OptimisticLockException
 *   5. TX2 must retry (read fresh data, re-apply changes)
 *
 * Best for: Low contention (most web apps, e-commerce, CMS)
 *
 * === Pessimistic Locking ===
 *
 * Acquires actual DB row lock. Other transactions WAIT.
 *   @Lock(LockModeType.PESSIMISTIC_WRITE)
 *   @Query("SELECT p FROM Product p WHERE p.id = :id")
 *   Optional<Product> findByIdForUpdate(Long id);
 *   → SQL: SELECT ... FOR UPDATE
 *
 * LockModeType:
 *   PESSIMISTIC_READ   → shared lock (others can read, not write)
 *   PESSIMISTIC_WRITE  → exclusive lock (others can't read or write)
 *   PESSIMISTIC_FORCE_INCREMENT → exclusive lock + increment @Version
 *
 * Best for: High contention (inventory, seat booking, banking)
 *
 * | Feature        | Optimistic              | Pessimistic              |
 * |---------------|-------------------------|--------------------------|
 * | Mechanism     | @Version field           | DB row lock (FOR UPDATE) |
 * | Blocking      | No (non-blocking)        | Yes (blocks other TXs)   |
 * | Conflict      | Detected at commit       | Prevented (lock waits)   |
 * | Performance   | Better (no locks)        | Worse (lock contention)  |
 * | Scalability   | High                     | Lower                    |
 * | Use case      | Low contention           | High contention          |
 * | Failure       | OptimisticLockException  | Lock timeout             |
 */
public class LockingExplained {

    public static void main(String[] args) {
        System.out.println("=== Optimistic vs Pessimistic Locking ===\n");

        System.out.println("=== Optimistic (recommended) ===");
        System.out.println("@Version private Integer version;");
        System.out.println("→ No DB lock. Version check at commit time.");
        System.out.println("→ If version mismatch → OptimisticLockException → retry");
        System.out.println("→ Best for: web apps, low contention\n");

        System.out.println("=== Pessimistic ===");
        System.out.println("@Lock(LockModeType.PESSIMISTIC_WRITE)");
        System.out.println("→ SELECT ... FOR UPDATE (DB row lock)");
        System.out.println("→ Other transactions WAIT until lock released");
        System.out.println("→ Best for: inventory, banking, seat booking\n");

        System.out.println("=== Decision ===");
        System.out.println("Read-heavy, low contention → Optimistic (@Version)");
        System.out.println("Write-heavy, high contention → Pessimistic (FOR UPDATE)");
        System.out.println("Default choice → Optimistic (works for 95% of apps)");
    }
}
