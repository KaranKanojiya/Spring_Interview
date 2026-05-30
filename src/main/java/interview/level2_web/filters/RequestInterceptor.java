package interview.level2_web.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Q8 (continued). Spring HandlerInterceptor example.
 *
 * Three callback methods:
 *   preHandle()       → before controller (return false to block)
 *   postHandle()      → after controller, before view rendering
 *   afterCompletion() → after everything (cleanup, logging)
 *
 * Register via WebMvcConfigurer.addInterceptors()
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.printf("[Interceptor] preHandle → %s %s%n",
                request.getMethod(), request.getRequestURI());
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;  // return false → blocks the request
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        System.out.printf("[Interceptor] postHandle → %s %s%n",
                request.getMethod(), request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        System.out.printf("[Interceptor] afterCompletion → %s %s (%dms)%n",
                request.getMethod(), request.getRequestURI(), duration);
    }
}
