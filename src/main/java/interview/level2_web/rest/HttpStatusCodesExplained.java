package interview.level2_web.rest;

/**
 * Q20. What HTTP status codes should a REST API return?
 *
 * | Code | Meaning                | When to use                          |
 * |------|------------------------|--------------------------------------|
 * | 200  | OK                     | GET success, PUT/PATCH success        |
 * | 201  | Created                | POST success (resource created)       |
 * | 204  | No Content             | DELETE success (no body to return)     |
 * | 301  | Moved Permanently      | URL permanently changed                |
 * | 302  | Found (redirect)       | Temporary redirect                     |
 * | 400  | Bad Request            | Validation error, malformed input      |
 * | 401  | Unauthorized           | Not authenticated (no/invalid token)   |
 * | 403  | Forbidden              | Authenticated but not authorized       |
 * | 404  | Not Found              | Resource doesn't exist                 |
 * | 405  | Method Not Allowed     | Wrong HTTP method for this endpoint    |
 * | 409  | Conflict               | Duplicate resource, state conflict     |
 * | 415  | Unsupported Media Type | Wrong Content-Type header              |
 * | 422  | Unprocessable Entity   | Valid JSON but semantic errors          |
 * | 429  | Too Many Requests      | Rate limiting                          |
 * | 500  | Internal Server Error  | Unhandled exception (bug)              |
 * | 502  | Bad Gateway            | Upstream service failure                |
 * | 503  | Service Unavailable    | Server overloaded or in maintenance    |
 *
 * Interview tip:
 *   401 vs 403:
 *     401 = "Who are you?" (not logged in)
 *     403 = "I know who you are, but you can't do that" (no permission)
 *
 *   200 vs 201:
 *     200 = generic success (GET, PUT)
 *     201 = resource was CREATED (POST) — include Location header
 *
 *   204 vs 200:
 *     204 = success, no body (DELETE)
 *     200 = success, with body
 */
public class HttpStatusCodesExplained {

    public static void main(String[] args) {
        System.out.println("=== HTTP Status Codes for REST APIs ===\n");

        System.out.println("2xx Success:");
        System.out.println("  200 OK            → GET, PUT success");
        System.out.println("  201 Created       → POST success + Location header");
        System.out.println("  204 No Content    → DELETE success\n");

        System.out.println("4xx Client Errors:");
        System.out.println("  400 Bad Request   → validation failed");
        System.out.println("  401 Unauthorized  → not authenticated");
        System.out.println("  403 Forbidden     → not authorized");
        System.out.println("  404 Not Found     → resource missing");
        System.out.println("  409 Conflict      → duplicate/state conflict");
        System.out.println("  429 Too Many Reqs → rate limited\n");

        System.out.println("5xx Server Errors:");
        System.out.println("  500 Internal      → unhandled exception");
        System.out.println("  502 Bad Gateway   → upstream failure");
        System.out.println("  503 Unavailable   → overloaded/maintenance\n");

        System.out.println("=== curl to check status ===");
        System.out.println("curl -o /dev/null -s -w \"%{http_code}\" http://localhost:8080/api/users");
        System.out.println("curl -v http://localhost:8080/api/users/999  → 404");
    }
}
