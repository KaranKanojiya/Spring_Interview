package interview.level3_data.repositories;

import interview.level3_data.jpa.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Orders — demonstrates JOIN FETCH (N+1 solution).
 *
 * curl tests:
 *   curl -s http://localhost:8080/api/orders | jq        → JOIN FETCH (1 query)
 *   curl -s http://localhost:8080/api/orders/1 | jq      → single order with items
 */
@RestController
@RequestMapping("/api/orders")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Orders", description = "Orders with JOIN FETCH — public (N+1 demo)")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Uses JOIN FETCH → 1 query instead of N+1
    @GetMapping
    public List<Order> getAll() {
        return orderRepository.findAllWithItemsAndUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return orderRepository.findByIdWithItems(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
