package interview.level3_data.repositories;

import interview.level3_data.jpa.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Q7. What is the difference between JpaRepository, CrudRepository, and PagingAndSortingRepository?
 *
 * Hierarchy:
 *   Repository (marker)
 *     └── CrudRepository        → save, findById, findAll, delete, count, existsById
 *         └── ListCrudRepository → same but returns List instead of Iterable
 *         └── PagingAndSortingRepository → findAll(Pageable), findAll(Sort)
 *             └── JpaRepository  → flush, saveAndFlush, deleteInBatch, getById
 *
 * Use JpaRepository in most cases — it includes everything.
 *
 * Q8. What are derived query methods?
 *
 * Spring Data generates SQL from method names:
 *   findByName(String name)                    → WHERE name = ?
 *   findByNameContainingIgnoreCase(String)      → WHERE LOWER(name) LIKE '%?%'
 *   findByPriceBetween(BigDecimal, BigDecimal) → WHERE price BETWEEN ? AND ?
 *   findByCategoryOrderByPriceDesc(Category)   → WHERE category = ? ORDER BY price DESC
 *   countByCategory(Category)                  → SELECT COUNT(*) WHERE category = ?
 *   existsByName(String)                       → SELECT CASE WHEN COUNT(*)>0...
 *   deleteByCategory(Category)                 → DELETE WHERE category = ?
 *
 * Keywords: And, Or, Is, Not, Between, LessThan, GreaterThan,
 *           Like, Containing, StartingWith, EndingWith,
 *           OrderBy, In, True, False, IsNull, IsNotNull, Top, First
 *
 * Q9. What is @Query and when do you use it?
 *
 * Use @Query when derived method names get too long or complex.
 *   - JPQL: object-oriented query (uses entity names, not table names)
 *   - Native SQL: raw SQL (use nativeQuery = true)
 *
 * curl test:
 *   curl -s http://localhost:8080/api/products | jq
 *   curl -s "http://localhost:8080/api/products/search?category=ELECTRONICS" | jq
 *   curl -s "http://localhost:8080/api/products/price-range?min=10&max=100" | jq
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    // === Derived query methods (Q8) ===
    List<Product> findByCategory(Product.Category category);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<Product> findByCategoryOrderByPriceAsc(Product.Category category);

    long countByCategory(Product.Category category);

    boolean existsByName(String name);

    Page<Product> findByCategory(Product.Category category, Pageable pageable);

    // === @Query with JPQL (Q9) ===
    @Query("SELECT p FROM Product p WHERE p.price > :minPrice AND p.category = :category")
    List<Product> findExpensiveByCategory(@Param("minPrice") BigDecimal minPrice,
                                         @Param("category") Product.Category category);

    // === @Query with native SQL ===
    @Query(value = "SELECT * FROM products WHERE price = (SELECT MAX(price) FROM products)",
            nativeQuery = true)
    Product findMostExpensive();

    // === @Query for aggregation ===
    @Query("SELECT p.category, COUNT(p), AVG(p.price) FROM Product p GROUP BY p.category")
    List<Object[]> getCategoryStats();
}
