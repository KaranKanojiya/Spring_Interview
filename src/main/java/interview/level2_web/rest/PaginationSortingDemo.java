package interview.level2_web.rest;

/**
 * Q19. How does pagination and sorting work in Spring Boot?
 *
 * Spring Data provides Pageable and Sort for pagination.
 *
 * In controller — just add Pageable parameter:
 *   @GetMapping
 *   Page<User> getUsers(Pageable pageable) {
 *       return userRepo.findAll(pageable);
 *   }
 *
 * Spring auto-resolves from query params:
 *   ?page=0            → page number (0-based)
 *   ?size=20           → page size (default 20)
 *   ?sort=name,asc     → sort by name ascending
 *   ?sort=age,desc     → sort by age descending
 *   ?sort=name,asc&sort=age,desc → multi-sort
 *
 * Page<T> response includes:
 *   content:          the data for this page
 *   totalElements:    total records in DB
 *   totalPages:       total pages
 *   number:           current page number
 *   size:             page size
 *   first/last:       is this the first/last page?
 *
 * Configuration:
 *   spring.data.web.pageable.default-page-size=20
 *   spring.data.web.pageable.max-page-size=100
 *
 * curl tests:
 *   curl -s "http://localhost:8080/api/users/search?page=0&size=2" | jq
 *   curl -s "http://localhost:8080/api/users/search?sort=name,asc" | jq
 *   curl -s "http://localhost:8080/api/users/search?page=0&size=1&sort=name,desc" | jq
 */
public class PaginationSortingDemo {

    public static void main(String[] args) {
        System.out.println("=== Pagination & Sorting ===\n");

        System.out.println("=== Query params (auto-resolved by Spring) ===");
        System.out.println("?page=0          → first page (0-based)");
        System.out.println("?size=10         → 10 items per page");
        System.out.println("?sort=name,asc   → sort by name ascending");
        System.out.println("?sort=age,desc   → sort by age descending\n");

        System.out.println("=== Page<T> response ===");
        System.out.println("{");
        System.out.println("  \"content\": [...],");
        System.out.println("  \"totalElements\": 100,");
        System.out.println("  \"totalPages\": 10,");
        System.out.println("  \"number\": 0,");
        System.out.println("  \"size\": 10,");
        System.out.println("  \"first\": true,");
        System.out.println("  \"last\": false");
        System.out.println("}\n");

        System.out.println("=== curl ===");
        System.out.println("curl -s \"http://localhost:8080/api/users/search?page=0&size=2&sort=name,asc\" | jq");
    }
}
