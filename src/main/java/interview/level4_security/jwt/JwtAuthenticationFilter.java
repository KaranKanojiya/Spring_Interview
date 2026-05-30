package interview.level4_security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Q7. How does JWT authentication work in Spring Security?
 *
 * This filter runs BEFORE Spring Security's authorization check.
 * It extracts the JWT from the Authorization header, validates it,
 * and sets the authentication in SecurityContext.
 *
 * Flow:
 *   1. Extract "Authorization: Bearer <token>" header
 *   2. Parse and validate JWT (signature + expiry)
 *   3. Load UserDetails from UserDetailsService
 *   4. Create Authentication object and set in SecurityContextHolder
 *   5. Spring Security's AuthorizationFilter uses this to check access
 *
 * OncePerRequestFilter:
 *   Guarantees this filter runs exactly once per request
 *   (prevents double execution on forwarded/included requests)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, @org.springframework.context.annotation.Lazy UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extract Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // no token → continue (may be public endpoint)
            return;
        }

        // 2. Extract token (remove "Bearer " prefix)
        String token = authHeader.substring(7);

        try {
            // 3. Extract username from token
            String username = jwtUtil.extractUsername(token);

            // 4. If not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 6. Validate token
                if (jwtUtil.isTokenValid(token, userDetails)) {

                    // 7. Create Authentication and set in SecurityContext
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Invalid token → don't authenticate, let Spring return 401
            logger.error("JWT validation failed: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
