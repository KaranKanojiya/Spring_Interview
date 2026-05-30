package interview.level3_data.repositories;

import interview.level3_data.jpa.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * REST controller for Product — demonstrates repository queries.
 *
 * curl tests:
 *   curl -s http://localhost:8080/api/products | jq
 *   curl -s http://localhost:8080/api/products/1 | jq
 *   curl -s "http://localhost:8080/api/products/search?category=ELECTRONICS" | jq
 *   curl -s "http://localhost:8080/api/products/price-range?min=10&max=50" | jq
 *   curl -s http://localhost:8080/api/products/most-expensive | jq
 *   curl -s http://localhost:8080/api/products/stats | jq
 *   curl -s -X POST http://localhost:8080/api/products \
 *     -H "Content-Type: application/json" \
 *     -d '{"name":"Keyboard","price":49.99,"category":"ELECTRONICS"}' | jq
 */
@RestController
@RequestMapping("/api/products")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Products", description = "Product queries — public (JPA/Repository demos)")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(saved);
    }

    // Derived query: by category
    @GetMapping("/search")
    public List<Product> byCategory(@RequestParam Product.Category category) {
        return productRepository.findByCategory(category);
    }

    // Derived query: price range
    @GetMapping("/price-range")
    public List<Product> byPriceRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return productRepository.findByPriceBetween(min, max);
    }

    // @Query: most expensive
    @GetMapping("/most-expensive")
    public ResponseEntity<Product> mostExpensive() {
        Product product = productRepository.findMostExpensive();
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // @Query: category stats
    @GetMapping("/stats")
    public List<Object[]> categoryStats() {
        return productRepository.getCategoryStats();
    }
}
