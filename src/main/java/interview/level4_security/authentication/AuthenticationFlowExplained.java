package interview.level4_security.authentication;

/**
 * Q12. What is the complete authentication flow in Spring Security?
 *
 * 1. Client sends credentials (POST /login with username + password)
 *
 * 2. AuthenticationFilter creates UsernamePasswordAuthenticationToken
 *    (an Authentication object containing credentials)
 *
 * 3. AuthenticationManager.authenticate(token) is called
 *    → delegates to AuthenticationProvider(s)
 *
 * 4. AuthenticationProvider calls:
 *    a. UserDetailsService.loadUserByUsername(username) → UserDetails
 *    b. PasswordEncoder.matches(rawPassword, encodedPassword) → boolean
 *
 * 5. If valid: returns authenticated Authentication (with authorities/roles)
 *    If invalid: throws AuthenticationException → 401
 *
 * 6. SecurityContextHolder.getContext().setAuthentication(auth)
 *    → stores the authenticated user for the request lifetime
 *
 * 7. Subsequent filters (AuthorizationFilter) check roles against endpoint rules
 *
 * Key components:
 *   AuthenticationManager      → orchestrates authentication
 *   AuthenticationProvider      → performs the actual authentication logic
 *   UserDetailsService          → loads user data (from DB, LDAP, etc.)
 *   UserDetails                 → user data: username, password, authorities
 *   PasswordEncoder            → BCrypt, Argon2, etc.
 *   SecurityContextHolder       → thread-local storage for current Authentication
 *   GrantedAuthority            → a permission/role (ROLE_ADMIN, ROLE_USER)
 *
 * Q13. What is SecurityContextHolder?
 *
 * Thread-local storage for the current Authentication object.
 *
 *   SecurityContextHolder.getContext().getAuthentication()
 *   → returns the currently authenticated user
 *   → available anywhere in the request thread
 *
 * Strategies:
 *   MODE_THREADLOCAL (default) → one Authentication per thread
 *   MODE_INHERITABLETHREADLOCAL → child threads inherit Authentication
 *   MODE_GLOBAL → shared across all threads (rare)
 */
public class AuthenticationFlowExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Security Authentication Flow ===\n");

        System.out.println("1. Client → POST /api/auth/login (username + password)");
        System.out.println("2. AuthenticationFilter → creates UsernamePasswordAuthenticationToken");
        System.out.println("3. AuthenticationManager → delegates to AuthenticationProvider");
        System.out.println("4. AuthenticationProvider:");
        System.out.println("   a. UserDetailsService.loadUserByUsername() → UserDetails");
        System.out.println("   b. PasswordEncoder.matches() → validates password");
        System.out.println("5. If valid → Authentication set in SecurityContextHolder");
        System.out.println("6. If invalid → AuthenticationException → 401\n");

        System.out.println("=== Key interfaces ===");
        System.out.println("AuthenticationManager    → authenticate(Authentication)");
        System.out.println("UserDetailsService       → loadUserByUsername(String)");
        System.out.println("PasswordEncoder          → encode(raw), matches(raw, encoded)");
        System.out.println("SecurityContextHolder    → getContext().getAuthentication()");
    }
}
