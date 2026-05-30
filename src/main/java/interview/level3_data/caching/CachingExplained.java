package interview.level3_data.caching;

/**
 * Q12. How does caching work in Spring Boot?
 *
 * Spring Cache Abstraction: annotation-driven caching layer.
 *
 * Setup:
 *   1. Add @EnableCaching on a @Configuration class
 *   2. Use cache annotations on service methods
 *
 * Annotations:
 *   @Cacheable("products")           → cache the result; skip method if cached
 *   @CachePut("products")            → always execute method, update cache
 *   @CacheEvict("products")          → remove entry from cache
 *   @CacheEvict(value="products", allEntries=true) → clear entire cache
 *   @Caching(evict={...}, put={...}) → combine multiple cache ops
 *
 * Example:
 *   @Service
 *   class ProductService {
 *
 *       @Cacheable(value = "products", key = "#id")
 *       public Product findById(Long id) {
 *           // Called ONLY if not in cache
 *           return repo.findById(id).orElseThrow();
 *       }
 *
 *       @CachePut(value = "products", key = "#product.id")
 *       public Product update(Product product) {
 *           // Always executes, then updates cache
 *           return repo.save(product);
 *       }
 *
 *       @CacheEvict(value = "products", key = "#id")
 *       public void delete(Long id) {
 *           // Removes from cache after execution
 *           repo.deleteById(id);
 *       }
 *   }
 *
 * Cache providers (auto-detected by classpath):
 *   - ConcurrentMapCache (default — in-memory, no config needed)
 *   - Redis (spring-boot-starter-data-redis)
 *   - Caffeine (caffeine dependency)
 *   - EhCache, Hazelcast
 *
 * Configuration:
 *   spring.cache.type=redis
 *   spring.cache.redis.time-to-live=600000  (10 min TTL)
 *
 * GOTCHA: Same as @Transactional — @Cacheable only works via proxy.
 *   this.findById(1) → cache is BYPASSED (self-invocation)
 */
public class CachingExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Cache Abstraction ===\n");

        System.out.println("=== Annotations ===");
        System.out.println("@Cacheable(\"cache\", key=\"#id\")  → cache result, skip if cached");
        System.out.println("@CachePut(\"cache\", key=\"#p.id\") → execute + update cache");
        System.out.println("@CacheEvict(\"cache\", key=\"#id\") → remove from cache");
        System.out.println("@CacheEvict(allEntries=true)      → clear entire cache");

        System.out.println("\n=== Providers ===");
        System.out.println("Default:  ConcurrentMapCache (in-memory, dev only)");
        System.out.println("Redis:    spring-boot-starter-data-redis");
        System.out.println("Caffeine: com.github.ben-manes.caffeine");

        System.out.println("\n=== GOTCHA ===");
        System.out.println("@Cacheable uses AOP proxy → self-invocation bypasses cache!");
        System.out.println("Same problem as @Transactional.");
    }
}
