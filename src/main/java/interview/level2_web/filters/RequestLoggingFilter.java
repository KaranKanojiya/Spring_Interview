package interview.level2_web.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Q8. What is the difference between a Filter and an Interceptor?
 *
 * | Feature        | Filter (Servlet)           | Interceptor (Spring)         |
 * |---------------|---------------------------|------------------------------|
 * | Level         | Servlet API (jakarta.servlet) | Spring MVC                   |
 * | Scope         | ALL requests (including static) | Only DispatcherServlet requests |
 * | Access to     | Request/Response only      | Handler, ModelAndView        |
 * | Spring beans  | Not injected by default    | Full Spring context access    |
 * | Use case      | Security, logging, CORS    | Auth, logging, metrics        |
 * | Order         | @Order or FilterRegistration | Ordered in WebMvcConfigurer  |
 *
 * Request lifecycle:
 *   Client → Filter → DispatcherServlet → Interceptor → Controller → Interceptor → Filter → Client
 *
 * Q9. What is the request processing lifecycle in Spring MVC?
 *
 *   1. Client sends HTTP request
 *   2. Filters execute (security, CORS, logging)
 *   3. DispatcherServlet receives request
 *   4. HandlerMapping finds the right controller method
 *   5. HandlerInterceptor.preHandle() executes
 *   6. Controller method executes
 *   7. HandlerInterceptor.postHandle() executes
 *   8. ViewResolver resolves view (or @ResponseBody serializes JSON)
 *   9. HandlerInterceptor.afterCompletion() executes
 *  10. Response sent through Filters back to client
 *
 * curl test:
 *   curl -s http://localhost:8080/api/users | jq
 *   → Check console logs for filter execution
 */
@Component
@Order(1)
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        // Before controller
        System.out.printf("[Filter] → %s %s%n", httpReq.getMethod(), httpReq.getRequestURI());

        // Continue the chain (next filter or DispatcherServlet)
        chain.doFilter(request, response);

        // After controller
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("[Filter] ← %s %s → %d (%dms)%n",
                httpReq.getMethod(), httpReq.getRequestURI(),
                httpResp.getStatus(), duration);
    }
}
