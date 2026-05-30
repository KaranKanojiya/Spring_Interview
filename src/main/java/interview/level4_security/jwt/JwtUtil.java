package interview.level4_security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Q6. What is JWT and how does it work?
 *
 * JWT (JSON Web Token) = stateless authentication token.
 *
 * Structure: HEADER.PAYLOAD.SIGNATURE (3 Base64-encoded parts separated by dots)
 *
 *   Header:    {"alg":"HS256","typ":"JWT"}
 *   Payload:   {"sub":"admin","roles":["ADMIN"],"iat":...,"exp":...}
 *   Signature: HMACSHA256(base64(header) + "." + base64(payload), secret)
 *
 * Flow:
 *   1. Client sends username/password → POST /api/auth/login
 *   2. Server validates credentials → generates JWT → returns token
 *   3. Client stores token (localStorage/cookie)
 *   4. Client sends token in header: Authorization: Bearer <token>
 *   5. Server validates token signature + expiry → grants access
 *
 * JWT vs Session:
 *   | Feature    | JWT (stateless)           | Session (stateful)         |
 *   |-----------|---------------------------|----------------------------|
 *   | Storage   | Client (header/cookie)    | Server (memory/Redis)       |
 *   | Scalability| Easy (no shared state)   | Hard (session replication)   |
 *   | Revocation| Hard (token is self-contained) | Easy (delete session)  |
 *   | Size      | Larger (payload in token) | Small (just session ID)     |
 */
@Component
public class JwtUtil {

    // In production: load from environment variable or config
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "my-super-secret-key-for-jwt-signing-must-be-256-bits-long!!".getBytes());
    private static final long EXPIRATION_MS = 3600000; // 1 hour

    // Generate JWT token
    public String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
