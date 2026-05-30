package interview.level2_web.rest;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Q1. What is @RestController and how does it differ from @Controller?
 *
 * @RestController = @Controller + @ResponseBody
 *   - @Controller: returns VIEW names (HTML templates)
 *   - @RestController: returns DATA directly (JSON/XML)
 *   - Every method's return value is serialized to HTTP response body
 *
 * Q2. What HTTP methods map to CRUD operations?
 *
 * | HTTP Method | CRUD      | Annotation        | Idempotent? |
 * |------------|-----------|-------------------|-------------|
 * | GET        | Read      | @GetMapping       | Yes         |
 * | POST       | Create    | @PostMapping      | No          |
 * | PUT        | Update    | @PutMapping       | Yes         |
 * | PATCH      | Partial   | @PatchMapping     | No          |
 * | DELETE     | Delete    | @DeleteMapping    | Yes         |
 *
 * Q3. What is ResponseEntity and when should you use it?
 *
 * ResponseEntity<T> gives you full control over the HTTP response:
 *   - Status code (200, 201, 404, etc.)
 *   - Headers (Location, Cache-Control, etc.)
 *   - Body (the data)
 *
 * Use ResponseEntity when you need to customize status/headers.
 * Use direct return (just the object) when 200 OK is fine.
 *
 * curl tests:
 *   curl -s http://localhost:8080/api/users | jq
 *   curl -s http://localhost:8080/api/users/1 | jq
 *   curl -s -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{"name":"Karan","email":"k@test.com"}' | jq
 *   curl -s -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d '{"name":"Updated","email":"k@test.com"}' | jq
 *   curl -s -X DELETE http://localhost:8080/api/users/1 -w "\n%{http_code}\n"
 *   curl -s "http://localhost:8080/api/users/search?name=Karan&page=0&size=5" | jq
 */
@RestController
@RequestMapping("/api/users")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Users", description = "User CRUD — public endpoints (no auth required)")
public class UserController {

    private final UserRepository userRepository;

    // Constructor injection (no @Autowired needed — single constructor)
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET /api/users — list all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET /api/users/{id} — get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)                              // 200 OK
                .orElseThrow(() -> new UserNotFoundException(id));     // → 404
    }

    // POST /api/users — create user (returns 201 Created + Location header)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity
                .created(URI.create("/api/users/" + saved.getId()))    // 201 + Location header
                .body(saved);
    }

    // PUT /api/users/{id} — update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(user.getName());
                    existing.setEmail(user.getEmail());
                    existing.setAge(user.getAge());
                    return ResponseEntity.ok(userRepository.save(existing));
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // DELETE /api/users/{id} — delete user (returns 204 No Content)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)    // Q: What does @ResponseStatus do?
    public void deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    // GET /api/users/search?name=Karan&page=0&size=5 — search with @RequestParam + pagination
    @GetMapping("/search")
    public Page<User> searchUsers(
            @RequestParam(required = false, defaultValue = "") String name,
            Pageable pageable) {  // Spring auto-resolves page, size, sort from query params
        if (name.isBlank()) {
            return userRepository.findAll(pageable);
        }
        // For simplicity, using findAll with pageable. In practice, use a custom query.
        return userRepository.findAll(pageable);
    }
}
