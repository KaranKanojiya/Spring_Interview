package interview.level4_security.cors_csrf;

/**
 * Q10. What is CORS and how do you configure it?
 *
 * CORS (Cross-Origin Resource Sharing):
 *   Browser security feature that blocks requests from a different origin.
 *   Origin = protocol + host + port (http://localhost:3000 ≠ http://localhost:8080)
 *
 * Without CORS config:
 *   React app at localhost:3000 → calls API at localhost:8080 → BLOCKED by browser!
 *
 * Configuration options:
 *   1. Global: CorsConfigurationSource bean (see SecurityConfig.java)
 *   2. Per-controller: @CrossOrigin(origins = "http://localhost:3000")
 *   3. WebMvcConfigurer: addCorsMappings()
 *
 * Q11. What is CSRF and when should you disable it?
 *
 * CSRF (Cross-Site Request Forgery):
 *   Attacker tricks user's browser into making unwanted requests to your site
 *   using the user's existing session cookies.
 *
 * Example attack:
 *   1. User is logged into bank.com (has session cookie)
 *   2. User visits evil.com
 *   3. evil.com has: <img src="bank.com/transfer?to=attacker&amount=1000">
 *   4. Browser sends bank.com cookie automatically → transfer happens!
 *
 * CSRF protection:
 *   Spring generates a CSRF token → client must send it with every state-changing request
 *   If token missing/invalid → 403 Forbidden
 *
 * When to DISABLE CSRF:
 *   ✅ REST APIs with JWT (stateless, no cookies → no CSRF risk)
 *   ✅ Service-to-service communication
 *
 * When to KEEP CSRF:
 *   ❌ Server-rendered apps (Thymeleaf, JSP) with session cookies
 *   ❌ Any app using cookie-based authentication
 */
public class CorsAndCsrfExplained {

    public static void main(String[] args) {
        System.out.println("=== CORS ===");
        System.out.println("Problem:  Browser blocks cross-origin requests");
        System.out.println("Solution: Server sends Access-Control-Allow-Origin headers");
        System.out.println("Config:   CorsConfigurationSource bean or @CrossOrigin\n");

        System.out.println("=== CSRF ===");
        System.out.println("Problem:  Attacker exploits session cookies for unauthorized requests");
        System.out.println("Solution: CSRF token (Spring Security default)");
        System.out.println("Disable:  REST APIs with JWT (no cookies = no CSRF risk)");
        System.out.println("Keep:     Server-rendered apps with session cookies");

        System.out.println("\n=== In this project ===");
        System.out.println("CSRF:    DISABLED (REST API + JWT, no session cookies)");
        System.out.println("CORS:    ENABLED for localhost:3000 and localhost:5173");
        System.out.println("Session: STATELESS (SessionCreationPolicy.STATELESS)");
    }
}
