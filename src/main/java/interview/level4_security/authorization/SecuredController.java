package interview.level4_security.authorization;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Q9. What is the difference between role-based and method-level security?
 *
 * URL-based (in SecurityFilterChain):
 *   .requestMatchers("/api/admin/**").hasRole("ADMIN")
 *   → Coarse-grained, configured in one place
 *
 * Method-level (@PreAuthorize, @Secured):
 *   @PreAuthorize("hasRole('ADMIN')")
 *   public void deleteUser(Long id) { ... }
 *   → Fine-grained, on the method itself
 *
 * @PreAuthorize vs @Secured:
 *   @PreAuthorize("hasRole('ADMIN')")           → SpEL expressions (recommended)
 *   @PreAuthorize("hasRole('ADMIN') and #id > 0") → can reference method params
 *   @PreAuthorize("@myService.isOwner(#id)")     → can call beans
 *   @Secured("ROLE_ADMIN")                       → simpler, no SpEL
 *
 * Requires: @EnableMethodSecurity on config class
 *
 * curl tests:
 *   # Get token
 *   TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"admin","password":"admin123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
 *
 *   # Access secured endpoints
 *   curl -s http://localhost:8080/api/secure/profile -H "Authorization: Bearer $TOKEN" | jq
 *   curl -s http://localhost:8080/api/secure/admin -H "Authorization: Bearer $TOKEN" | jq
 *
 *   # Without token → 401
 *   curl -s http://localhost:8080/api/secure/profile | jq
 *
 *   # User accessing admin → 403
 *   USER_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"user","password":"user123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
 *   curl -s http://localhost:8080/api/secure/admin -H "Authorization: Bearer $USER_TOKEN" | jq
 */
@RestController
@RequestMapping("/api/secure")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Secured Endpoints", description = "JWT required — use Authorize button with token from /api/auth/login")
public class SecuredController {

    // Any authenticated user (USER or ADMIN)
    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                "message", "You are authenticated!"
        );
    }

    // ADMIN only (URL-based in SecurityFilterChain)
    @GetMapping("/admin")
    public Map<String, String> adminOnly() {
        return Map.of("message", "Welcome, Admin! This endpoint requires ROLE_ADMIN.");
    }

    // Method-level security with @PreAuthorize
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminManageUsers() {
        return Map.of("message", "Admin user management — @PreAuthorize('hasRole(ADMIN)')");
    }

    // @PreAuthorize with SpEL — access method parameter
    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.name")
    public Map<String, String> getUserById(@PathVariable String id, Authentication auth) {
        return Map.of(
                "message", "Accessing user: " + id,
                "rule", "ADMIN can access any user, regular user can only access their own"
        );
    }

    // Get current principal
    @GetMapping("/me")
    public Map<String, Object> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return Map.of(
                "username", userDetails.getUsername(),
                "authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                "enabled", userDetails.isEnabled()
        );
    }
}
