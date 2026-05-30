package interview.level3_data.transactions;

/**
 * Q5. What are transaction isolation levels?
 *
 * Isolation controls what concurrent transactions can see.
 *
 * Problems without proper isolation:
 *   Dirty Read:        TX1 reads uncommitted data from TX2 (TX2 might rollback!)
 *   Non-Repeatable Read: TX1 reads a row, TX2 modifies it, TX1 reads again → different value
 *   Phantom Read:      TX1 runs a query, TX2 inserts rows, TX1 runs same query → extra rows
 *
 * | Level            | Dirty Read | Non-Repeatable | Phantom | Performance |
 * |-----------------|------------|----------------|---------|-------------|
 * | READ_UNCOMMITTED | Yes        | Yes            | Yes     | Fastest     |
 * | READ_COMMITTED   | No         | Yes            | Yes     | Fast        |
 * | REPEATABLE_READ  | No         | No             | Yes     | Moderate    |
 * | SERIALIZABLE     | No         | No             | No      | Slowest     |
 *
 * PostgreSQL default: READ_COMMITTED
 * MySQL default: REPEATABLE_READ
 *
 * Spring usage:
 *   @Transactional(isolation = Isolation.READ_COMMITTED)
 *   @Transactional(isolation = Isolation.SERIALIZABLE)
 *
 * In practice:
 *   READ_COMMITTED is fine for most applications.
 *   SERIALIZABLE only for critical financial operations.
 *   Use optimistic locking (@Version) instead of higher isolation for better performance.
 */
public class IsolationLevelsDemo {

    public static void main(String[] args) {
        System.out.println("=== Transaction Isolation Levels ===\n");

        System.out.println("| Level            | Dirty Read | Non-Repeatable | Phantom |");
        System.out.println("|-----------------|------------|----------------|---------|");
        System.out.println("| READ_UNCOMMITTED | Yes        | Yes            | Yes     |");
        System.out.println("| READ_COMMITTED   | No         | Yes            | Yes     |");
        System.out.println("| REPEATABLE_READ  | No         | No             | Yes     |");
        System.out.println("| SERIALIZABLE     | No         | No             | No      |");

        System.out.println("\n=== Defaults ===");
        System.out.println("PostgreSQL: READ_COMMITTED");
        System.out.println("MySQL:      REPEATABLE_READ");
        System.out.println("H2:         READ_COMMITTED");

        System.out.println("\n=== Practical advice ===");
        System.out.println("1. READ_COMMITTED is fine for 95% of cases");
        System.out.println("2. Use @Version (optimistic locking) over SERIALIZABLE");
        System.out.println("3. SERIALIZABLE → only for money transfers, inventory deduction");
    }
}
