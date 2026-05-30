package interview.level3_data.transactions;

/**
 * Q4. What is @Transactional and how does propagation work?
 *
 * @Transactional wraps a method in a database transaction.
 * If the method completes → COMMIT. If RuntimeException → ROLLBACK.
 *
 * Key attributes:
 *   propagation   → how to handle existing transactions
 *   isolation      → visibility of concurrent changes
 *   readOnly       → optimization hint (no dirty checking)
 *   rollbackFor    → which exceptions trigger rollback
 *   timeout        → max seconds before auto-rollback
 *
 * Propagation types:
 * | Type           | Behavior                                         |
 * |---------------|--------------------------------------------------|
 * | REQUIRED (default) | Join existing TX, or create new if none       |
 * | REQUIRES_NEW  | Always create new TX (suspend existing)           |
 * | SUPPORTS      | Join existing TX, or run without TX if none       |
 * | NOT_SUPPORTED | Run without TX (suspend existing if any)          |
 * | MANDATORY     | Must run in existing TX (throw if none)           |
 * | NEVER         | Must NOT run in TX (throw if one exists)          |
 * | NESTED        | Create savepoint in existing TX (partial rollback)|
 *
 * Common patterns:
 *   @Transactional                    → default (REQUIRED, rollback on RuntimeException)
 *   @Transactional(readOnly = true)   → SELECT queries (skip dirty checking)
 *   @Transactional(rollbackFor = Exception.class) → rollback on checked exceptions too
 *   @Transactional(propagation = REQUIRES_NEW) → audit logs (commit even if parent fails)
 *
 * GOTCHA: @Transactional only works on PUBLIC methods called from OUTSIDE the class.
 *   self.method() → proxy is bypassed → no transaction!
 *   Solution: inject self, or use TransactionTemplate
 */
public class TransactionalDemo {

    public static void main(String[] args) {
        System.out.println("=== @Transactional ===\n");

        System.out.println("=== Propagation ===");
        System.out.println("REQUIRED (default): join or create new");
        System.out.println("REQUIRES_NEW:       always new (suspend current)");
        System.out.println("SUPPORTS:           join or run without TX");
        System.out.println("MANDATORY:          must be in TX (throw if not)");
        System.out.println("NEVER:              must NOT be in TX");
        System.out.println("NESTED:             savepoint (partial rollback)");

        System.out.println("\n=== Rollback rules ===");
        System.out.println("Default: rollback on RuntimeException + Error only");
        System.out.println("Checked exceptions: NO rollback (unless rollbackFor specified)");
        System.out.println("@Transactional(rollbackFor = Exception.class) → rollback on everything");

        System.out.println("\n=== GOTCHA: Self-invocation ===");
        System.out.println("@Transactional uses AOP proxy → only works when called externally");
        System.out.println("  this.saveInternal()  → NO PROXY → no transaction!");
        System.out.println("  injectedSelf.saveInternal() → PROXY → transaction works");

        System.out.println("\n=== Best practices ===");
        System.out.println("1. @Transactional on service layer, NOT controller");
        System.out.println("2. Use readOnly=true for queries (performance)");
        System.out.println("3. Keep transactions SHORT (no HTTP calls inside TX)");
        System.out.println("4. Don't catch exceptions silently inside @Transactional");
    }
}
