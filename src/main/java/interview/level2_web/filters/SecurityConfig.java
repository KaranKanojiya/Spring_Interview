package interview.level2_web.filters;

import interview.level4_security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Q1. How does Spring Security work? What is the SecurityFilterChain?
 *
 * Spring Security is a chain of servlet filters that intercept every HTTP request.
 *
 * Request flow:
 *   Client → SecurityFilterChain → DispatcherServlet → Controller
 *
 * Key filters (in order):
 *   1. CorsFilter               → handles CORS preflight
 *   2. CsrfFilter               → CSRF token validation
 *   3. UsernamePasswordAuthFilter → form login (if enabled)
 *   4. BasicAuthFilter           → HTTP Basic auth (if enabled)
 *   5. JwtAuthFilter (custom)    → validate JWT token
 *   6. AuthorizationFilter       → check if user has required role
 *   7. ExceptionTranslationFilter → convert auth exceptions to HTTP responses
 *
 * SecurityFilterChain:
 *   Replaces the old WebSecurityConfigurerAdapter (deprecated in Spring Security 5.7+)
 *   Define as a @Bean returning SecurityFilterChain
 *
 * Q2. What is the difference between authentication and authorization?
 *
 * Authentication: WHO are you? (verify identity — login)
 *   → AuthenticationManager, AuthenticationProvider, UserDetailsService
 *   → Result: Authentication object with username + roles
 *
 * Authorization: WHAT can you do? (verify permissions — access control)
 *   → .authorizeHttpRequests() rules
 *   → @PreAuthorize, @Secured annotations
 *   → Result: 403 Forbidden if not authorized
 *
 * curl tests:
 *   # Public endpoints (no auth needed)
 *   curl -s http://localhost:8080/api/users | jq
 *   curl -s http://localhost:8080/api/products | jq
 *
 *   # Login to get JWT token
 *   curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"admin","password":"admin123"}' | jq
 *
 *   # Access secured endpoint with JWT
 *   TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"admin","password":"admin123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
 *   curl -s http://localhost:8080/api/secure/admin -H "Authorization: Bearer $TOKEN" | jq
 *
 *   # Access without token → 401
 *   curl -s http://localhost:8080/api/secure/admin | jq
 *
 *   # User role accessing admin endpoint → 403
 *   TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
 *     -H "Content-Type: application/json" \
 *     -d '{"username":"user","password":"user123"}' | python3 -c "import sys,json;print(json.load(sys.stdin)['token'])")
 *   curl -s http://localhost:8080/api/secure/admin -H "Authorization: Bearer $TOKEN" | jq
 */
@Configuration
@EnableMethodSecurity  // enables @PreAuthorize, @Secured
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())  // disable CSRF for REST APIs (stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // no sessions — JWT only
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        // Secured endpoints
                        .requestMatchers("/api/secure/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/secure/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))  // H2 console
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Q3. What is UserDetailsService?
    // Loads user data for authentication. Spring Security calls loadUserByUsername().
    // In production: query database. Here: in-memory for demo.
    @Bean
    public UserDetailsService userDetailsService() {
        var admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        var user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    // Q4. What is PasswordEncoder and why is it required?
    // NEVER store plain text passwords. BCrypt is the recommended encoder.
    // BCrypt: salted, slow (configurable rounds), resistant to rainbow tables.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Q5. What is AuthenticationManager?
    // Central interface for authentication. Delegates to AuthenticationProvider(s).
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Q10. What is CORS and how do you configure it in Spring?
    // CORS (Cross-Origin Resource Sharing): allows browsers to make requests to different domains.
    // Without CORS config: browser blocks requests from http://localhost:3000 to http://localhost:8080
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173")); // React/Vite
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);  // preflight cache: 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}
