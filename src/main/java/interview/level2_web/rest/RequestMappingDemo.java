package interview.level2_web.rest;

/**
 * Q10. What is the difference between @RequestParam, @PathVariable, @RequestBody, and @RequestHeader?
 *
 * | Annotation     | Source               | Example                              |
 * |---------------|---------------------|--------------------------------------|
 * | @PathVariable  | URL path segment     | /users/{id}   → @PathVariable Long id |
 * | @RequestParam  | Query string         | /users?name=K → @RequestParam String name |
 * | @RequestBody   | HTTP body (JSON/XML) | POST body     → @RequestBody User user |
 * | @RequestHeader | HTTP header          | Authorization → @RequestHeader String auth |
 *
 * @PathVariable: identifies a SPECIFIC resource (/users/42)
 * @RequestParam: filters/pagination (/users?page=0&size=10)
 * @RequestBody:  data payload for POST/PUT
 * @RequestHeader: metadata (auth tokens, content type)
 *
 * Q11. What is @RequestMapping and its shorthand annotations?
 *
 * @RequestMapping: generic — maps any HTTP method
 * Shorthands (Spring 4.3+):
 *   @GetMapping     = @RequestMapping(method = GET)
 *   @PostMapping    = @RequestMapping(method = POST)
 *   @PutMapping     = @RequestMapping(method = PUT)
 *   @DeleteMapping  = @RequestMapping(method = DELETE)
 *   @PatchMapping   = @RequestMapping(method = PATCH)
 *
 * Q12. What does @ResponseStatus do?
 *
 * Sets the HTTP status code for a method or exception:
 *   @ResponseStatus(HttpStatus.CREATED)       → 201
 *   @ResponseStatus(HttpStatus.NO_CONTENT)    → 204
 *   @ResponseStatus(HttpStatus.NOT_FOUND)     → 404 (on exception class)
 *
 * See UserController for live examples of all these.
 */
public class RequestMappingDemo {

    public static void main(String[] args) {
        System.out.println("=== Request Mapping Annotations ===\n");

        System.out.println("=== @PathVariable ===");
        System.out.println("URL: /api/users/42");
        System.out.println("Code: @GetMapping(\"/{id}\") getUserById(@PathVariable Long id)");
        System.out.println("curl: curl http://localhost:8080/api/users/1\n");

        System.out.println("=== @RequestParam ===");
        System.out.println("URL: /api/users/search?name=Karan&page=0");
        System.out.println("Code: searchUsers(@RequestParam String name, Pageable pageable)");
        System.out.println("curl: curl \"http://localhost:8080/api/users/search?name=Karan\"\n");

        System.out.println("=== @RequestBody ===");
        System.out.println("Body: {\"name\":\"Karan\",\"email\":\"k@test.com\"}");
        System.out.println("Code: createUser(@Valid @RequestBody User user)");
        System.out.println("curl: curl -X POST http://localhost:8080/api/users -H 'Content-Type: application/json' -d '{...}'\n");

        System.out.println("=== @RequestHeader ===");
        System.out.println("Header: Authorization: Bearer abc123");
        System.out.println("Code: method(@RequestHeader(\"Authorization\") String token)");
        System.out.println("curl: curl -H 'Authorization: Bearer abc123' http://localhost:8080/api/...");
    }
}
