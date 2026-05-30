package interview.level5_advanced.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Q1. What is AOP (Aspect-Oriented Programming) in Spring?
 *
 * AOP separates cross-cutting concerns (logging, security, transactions, caching)
 * from business logic. Instead of scattering logging code in every method,
 * you define it ONCE in an Aspect.
 *
 * Key concepts:
 *   Aspect:     the cross-cutting concern class (@Aspect)
 *   Advice:     the action to take (what to do)
 *   Pointcut:   where to apply (which methods)
 *   JoinPoint:  the actual method being intercepted
 *   Weaving:    linking aspects to target objects (Spring uses runtime proxies)
 *
 * Q2. What are the different types of Advice?
 *
 * | Advice         | When it runs                          | Use case               |
 * |---------------|---------------------------------------|------------------------|
 * | @Before        | Before method execution               | Logging, validation     |
 * | @After         | After method (success or exception)   | Cleanup                 |
 * | @AfterReturning| After method returns successfully     | Audit, result logging   |
 * | @AfterThrowing | After method throws exception         | Error logging           |
 * | @Around        | Wraps method (before + after)         | Timing, transactions    |
 *
 * Q3. What are Pointcut expressions?
 *
 * execution(modifiers? return-type declaring-type? method-name(params) throws?)
 *
 *   execution(* interview.level2_web.rest.*.*(..))     → all methods in rest package
 *   execution(public * *(..))                           → all public methods
 *   execution(* save*(..))                              → methods starting with "save"
 *   @annotation(org.springframework.web.bind.annotation.GetMapping) → all @GetMapping methods
 *   within(interview.level3_data.repositories.*)        → all methods in repositories package
 *
 * curl test:
 *   curl -s http://localhost:8080/api/products | jq
 *   → Check console for [AOP] log lines
 */
@Aspect
@Component
public class LoggingAspect {

    // Pointcut: all methods in rest and repositories controllers
    @Pointcut("within(interview.level2_web.rest.*) || within(interview.level3_data.repositories.*)")
    public void controllerMethods() {}

    // @Before: runs before the method
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.printf("[AOP @Before] → %s.%s()%n",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    // @AfterReturning: runs after successful return
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String resultStr = result != null ? result.getClass().getSimpleName() : "void";
        System.out.printf("[AOP @AfterReturning] ← %s.%s() → %s%n",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                resultStr);
    }

    // @AfterThrowing: runs when method throws exception
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        System.out.printf("[AOP @AfterThrowing] ✗ %s.%s() threw %s: %s%n",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getClass().getSimpleName(),
                ex.getMessage());
    }

    // @Around: wraps the method (most powerful — controls execution)
    @Around("execution(* interview.level3_data.repositories.ProductController.getAll(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();  // execute the actual method

        long duration = System.currentTimeMillis() - start;
        System.out.printf("[AOP @Around] %s.%s() executed in %dms%n",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                duration);

        return result;
    }
}
