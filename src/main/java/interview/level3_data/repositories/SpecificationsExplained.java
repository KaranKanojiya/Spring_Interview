package interview.level3_data.repositories;

/**
 * Q15. What are Spring Data Specifications?
 *
 * Specifications allow building dynamic, composable queries programmatically.
 * Based on JPA Criteria API but with a cleaner interface.
 *
 * When to use:
 *   - Dynamic search with optional filters (search form with 10 fields, user fills 3)
 *   - Complex queries that change at runtime
 *   - Composable query conditions (AND/OR combinations)
 *
 * Setup:
 *   1. Repository extends JpaSpecificationExecutor<T>
 *   2. Create Specification<T> lambdas
 *   3. Combine with .and() / .or()
 *
 * Example:
 *   interface ProductRepo extends JpaRepository<Product, Long>,
 *                                  JpaSpecificationExecutor<Product> {}
 *
 *   // Reusable specs
 *   static Specification<Product> hasCategory(Category cat) {
 *       return (root, query, cb) -> cb.equal(root.get("category"), cat);
 *   }
 *
 *   static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
 *       return (root, query, cb) -> cb.between(root.get("price"), min, max);
 *   }
 *
 *   // Compose dynamically
 *   Specification<Product> spec = Specification.where(null);
 *   if (category != null) spec = spec.and(hasCategory(category));
 *   if (minPrice != null)  spec = spec.and(priceBetween(minPrice, maxPrice));
 *   List<Product> results = productRepo.findAll(spec);
 *
 * Alternative: QueryDSL (type-safe, but adds dependency + code generation)
 */
public class SpecificationsExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Data Specifications ===\n");

        System.out.println("Problem: Dynamic search filters (user picks which filters to apply)");
        System.out.println("Solution: Composable Specification<T> objects\n");

        System.out.println("=== Building specs ===");
        System.out.println("Specification<Product> byCategory = (root, query, cb) ->");
        System.out.println("    cb.equal(root.get(\"category\"), category);");
        System.out.println();
        System.out.println("Specification<Product> byPrice = (root, query, cb) ->");
        System.out.println("    cb.between(root.get(\"price\"), min, max);");

        System.out.println("\n=== Composing ===");
        System.out.println("Specification<Product> spec = Specification.where(null);");
        System.out.println("if (category != null) spec = spec.and(byCategory);");
        System.out.println("if (minPrice != null)  spec = spec.and(byPrice);");
        System.out.println("productRepo.findAll(spec, pageable);");

        System.out.println("\n=== When to use what ===");
        System.out.println("Derived methods:   simple queries (findByName)");
        System.out.println("@Query (JPQL):     complex but static queries");
        System.out.println("Specifications:    dynamic, composable queries");
        System.out.println("Native SQL:        DB-specific features, complex joins");
    }
}
