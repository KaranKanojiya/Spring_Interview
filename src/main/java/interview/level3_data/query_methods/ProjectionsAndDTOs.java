package interview.level3_data.query_methods;

/**
 * Q10. What are Projections and DTOs in Spring Data?
 *
 * Problem: @Entity loads ALL columns. Sometimes you only need a few fields.
 * Solution: Projections — fetch only what you need.
 *
 * Three types:
 *
 * 1. Interface-based (closed) projection:
 *    interface ProductSummary {
 *        String getName();
 *        BigDecimal getPrice();
 *    }
 *    List<ProductSummary> findByCategory(Category cat);
 *    → Spring generates proxy, SQL selects only name and price
 *
 * 2. Class-based (DTO) projection:
 *    record ProductDTO(String name, BigDecimal price) {}
 *    @Query("SELECT new interview.dto.ProductDTO(p.name, p.price) FROM Product p")
 *    List<ProductDTO> findProductDTOs();
 *    → Constructor expression in JPQL
 *
 * 3. Dynamic projection:
 *    <T> List<T> findByCategory(Category cat, Class<T> type);
 *    → Caller decides: repo.findByCategory(BOOKS, ProductSummary.class)
 *
 * When to use:
 *   ✅ API responses that don't need all fields
 *   ✅ Reports and aggregations
 *   ✅ Avoid exposing internal entity structure
 *   ✅ Performance — fewer columns = less data transferred
 */
public class ProjectionsAndDTOs {

    public static void main(String[] args) {
        System.out.println("=== Projections and DTOs ===\n");

        System.out.println("=== 1. Interface projection (closed) ===");
        System.out.println("interface ProductSummary {");
        System.out.println("    String getName();");
        System.out.println("    BigDecimal getPrice();");
        System.out.println("}");
        System.out.println("→ SQL: SELECT name, price FROM products WHERE ...");

        System.out.println("\n=== 2. DTO projection (class/record) ===");
        System.out.println("record ProductDTO(String name, BigDecimal price) {}");
        System.out.println("@Query(\"SELECT new ...ProductDTO(p.name, p.price) FROM Product p\")");
        System.out.println("→ Explicit constructor in JPQL");

        System.out.println("\n=== 3. Dynamic projection ===");
        System.out.println("<T> List<T> findByCategory(Category cat, Class<T> type);");
        System.out.println("→ repo.findByCategory(BOOKS, ProductSummary.class)");
        System.out.println("→ repo.findByCategory(BOOKS, Product.class) // full entity");

        System.out.println("\n=== Best practice ===");
        System.out.println("Use DTOs/projections for READ operations (API responses)");
        System.out.println("Use entities for WRITE operations (save/update)");
    }
}
