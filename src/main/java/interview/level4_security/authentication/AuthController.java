package interview.level4_security.authentication;

import interview.level4_security.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Q8. How does the login flow work with JWT?
 *
 * 1. Client POSTs username/password to /api/auth/login
 * 2. AuthenticationManager authenticates via UserDetailsService + PasswordEncoder
 * 3. If valid → generate JWT token → return to client
 * 4. If invalid → 401 Unauthorized
 *
 * curl tests:
 *   # Login as admin
 *   curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"admin","password":"admin123"}' | jq
 *
 *   # Login as user
 *   curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"user","password":"user123"}' | jq
 *
 *   # Bad credentials → 401
 *   curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"admin","password":"wrong"}' | jq
 */
@RestController
@RequestMapping("/api/auth")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Authentication", description = "Login to get JWT token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Login", description = "Authenticate with username/password → returns JWT token. Users: admin/admin123, user/user123")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // AuthenticationManager delegates to UserDetailsService + PasswordEncoder
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", userDetails.getUsername(),
                    "roles", roles,
                    "type", "Bearer"
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid username or password"
            ));
        }
    }

    record LoginRequest(String username, String password) {}
}
