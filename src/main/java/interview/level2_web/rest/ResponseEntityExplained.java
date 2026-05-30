package interview.level2_web.rest;

/**
 * Q13. When should you return ResponseEntity vs returning the object directly?
 *
 * Direct return (just the object):
 *   @GetMapping
 *   public List<User> getAll() { return userRepo.findAll(); }
 *   → Always returns 200 OK with JSON body
 *   → Simple and clean for standard responses
 *
 * ResponseEntity<T>:
 *   @PostMapping
 *   public ResponseEntity<User> create(@RequestBody User user) {
 *       User saved = repo.save(user);
 *       return ResponseEntity.created(URI.create("/users/" + saved.getId())).body(saved);
 *   }
 *   → Full control: status code + headers + body
 *   → Use when you need 201 Created, 204 No Content, Location header, etc.
 *
 * ResponseEntity builder methods:
 *   ResponseEntity.ok(body)                 → 200 + body
 *   ResponseEntity.ok().build()             → 200 no body
 *   ResponseEntity.created(uri).body(obj)   → 201 + Location header + body
 *   ResponseEntity.noContent().build()      → 204 no body
 *   ResponseEntity.notFound().build()       → 404 no body
 *   ResponseEntity.badRequest().body(err)   → 400 + error body
 *   ResponseEntity.status(HttpStatus.X)     → any status
 *
 * Q14. What is content negotiation in Spring?
 *
 * Spring can return JSON or XML based on:
 *   1. Accept header: Accept: application/json or Accept: application/xml
 *   2. URL suffix (deprecated): /users.json or /users.xml
 *   3. Query param: /users?format=json
 *
 * By default: JSON (Jackson on classpath)
 * For XML: add jackson-dataformat-xml dependency
 *
 * HttpMessageConverter does the serialization:
 *   Object → JSON: MappingJackson2HttpMessageConverter
 *   Object → XML: MappingJackson2XmlHttpMessageConverter
 */
public class ResponseEntityExplained {

    public static void main(String[] args) {
        System.out.println("=== ResponseEntity vs Direct Return ===\n");

        System.out.println("Direct return: simple, always 200 OK");
        System.out.println("ResponseEntity: full control (status + headers + body)\n");

        System.out.println("=== ResponseEntity builders ===");
        System.out.println("ResponseEntity.ok(body)               → 200 + body");
        System.out.println("ResponseEntity.created(uri).body(obj)  → 201 + Location + body");
        System.out.println("ResponseEntity.noContent().build()     → 204 (DELETE success)");
        System.out.println("ResponseEntity.notFound().build()      → 404");
        System.out.println("ResponseEntity.badRequest().body(err)  → 400 + error details");

        System.out.println("\n=== curl examples ===");
        System.out.println("POST returns 201:");
        System.out.println("  curl -v -X POST http://localhost:8080/api/users \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"name\":\"Test\",\"email\":\"t@t.com\"}'");
        System.out.println("  → Look for: HTTP/1.1 201 Created");
        System.out.println("  → Look for: Location: /api/users/4");
    }
}
