package interview.level4_security.authorization;

/**
 * Q14. What security annotations does Spring provide?
 *
 * Method-level security (requires @EnableMethodSecurity):
 *
 * @PreAuthorize:
 *   Evaluated BEFORE method execution. Supports SpEL.
 *   @PreAuthorize("hasRole('ADMIN')")
 *   @PreAuthorize("hasAuthority('user:write')")
 *   @PreAuthorize("hasRole('ADMIN') and #id > 0")
 *   @PreAuthorize("@authService.isOwner(#id, authentication)")
 *   @PreAuthorize("#username == authentication.name")
 *
 * @PostAuthorize:
 *   Evaluated AFTER method execution. Can check return value.
 *   @PostAuthorize("returnObject.owner == authentication.name")
 *   → Method runs, but result is blocked if condition fails
 *
 * @Secured:
 *   Simpler than @PreAuthorize. No SpEL.
 *   @Secured("ROLE_ADMIN")
 *   @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
 *
 * @RolesAllowed (JSR-250):
 *   Standard Java annotation (not Spring-specific).
 *   @RolesAllowed("ADMIN")
 *   Requires: @EnableMethodSecurity(jsr250Enabled = true)
 *
 * Q15. What is the difference between hasRole() and hasAuthority()?
 *
 * hasRole("ADMIN"):
 *   → checks for ROLE_ADMIN (adds "ROLE_" prefix automatically)
 *   → used for coarse-grained access (admin, user, manager)
 *
 * hasAuthority("user:write"):
 *   → checks exact authority string (no prefix added)
 *   → used for fine-grained permissions (user:read, user:write, order:delete)
 *
 * In UserDetails:
 *   .roles("ADMIN")           → stores as ROLE_ADMIN (automatic prefix)
 *   .authorities("user:read") → stores as-is (no prefix)
 */
public class SecurityAnnotationsExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Security Annotations ===\n");

        System.out.println("@PreAuthorize (SpEL — most powerful):");
        System.out.println("  @PreAuthorize(\"hasRole('ADMIN')\")");
        System.out.println("  @PreAuthorize(\"#id == authentication.name\")");
        System.out.println("  @PreAuthorize(\"@service.isOwner(#id)\")");

        System.out.println("\n@PostAuthorize (check return value):");
        System.out.println("  @PostAuthorize(\"returnObject.owner == authentication.name\")");

        System.out.println("\n@Secured (simple, no SpEL):");
        System.out.println("  @Secured(\"ROLE_ADMIN\")");

        System.out.println("\n=== hasRole vs hasAuthority ===");
        System.out.println("hasRole('ADMIN')        → checks ROLE_ADMIN (auto-prefix)");
        System.out.println("hasAuthority('ROLE_ADMIN') → checks exact string");
        System.out.println("hasAuthority('user:write') → fine-grained permission");

        System.out.println("\n=== 401 vs 403 ===");
        System.out.println("401 Unauthorized: not authenticated (no token / invalid token)");
        System.out.println("403 Forbidden:    authenticated but not authorized (wrong role)");
    }
}
