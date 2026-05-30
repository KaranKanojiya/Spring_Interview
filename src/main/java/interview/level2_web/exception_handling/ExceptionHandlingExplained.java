package interview.level2_web.exception_handling;

/**
 * Q15. What are the different ways to handle exceptions in Spring?
 *
 * 1. @ExceptionHandler (method-level in controller):
 *    Handles exceptions thrown by THAT controller only.
 *    @ExceptionHandler(UserNotFoundException.class)
 *    public ResponseEntity<String> handle(UserNotFoundException ex) { ... }
 *
 * 2. @ControllerAdvice / @RestControllerAdvice (global):
 *    Handles exceptions from ALL controllers. Centralized error handling.
 *    See: GlobalExceptionHandler.java
 *
 * 3. ResponseStatusException (inline):
 *    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
 *    → Quick one-liner, no custom exception class needed
 *
 * 4. @ResponseStatus on exception class:
 *    @ResponseStatus(HttpStatus.NOT_FOUND)
 *    public class UserNotFoundException extends RuntimeException { ... }
 *    → Spring automatically returns that status when this exception is thrown
 *
 * 5. ProblemDetail (Spring 6+ / RFC 7807):
 *    Standardized error response format.
 *    See: GlobalExceptionHandler.java
 *
 * Priority (lowest to highest):
 *   @ResponseStatus on exception class
 *   < @ExceptionHandler in controller
 *   < @ExceptionHandler in @ControllerAdvice
 *
 * Q16. What is the difference between checked and unchecked exceptions in Spring context?
 *
 * Spring transactions ONLY rollback on UNCHECKED exceptions (RuntimeException) by default.
 *   @Transactional → rollback on RuntimeException, NOT on checked Exception
 *   @Transactional(rollbackFor = Exception.class) → rollback on ALL exceptions
 *
 * Best practice: Use RuntimeException subclasses for business exceptions in Spring.
 */
public class ExceptionHandlingExplained {

    public static void main(String[] args) {
        System.out.println("=== Exception Handling in Spring ===\n");

        System.out.println("1. @ExceptionHandler (in controller)   → handles one controller");
        System.out.println("2. @RestControllerAdvice (global)       → handles ALL controllers");
        System.out.println("3. ResponseStatusException (inline)     → quick throw");
        System.out.println("4. @ResponseStatus on exception class   → auto-maps status");
        System.out.println("5. ProblemDetail (RFC 7807)             → standardized format\n");

        System.out.println("=== curl tests ===");
        System.out.println("404: curl -s http://localhost:8080/api/users/999 | jq");
        System.out.println("400: curl -s -X POST http://localhost:8080/api/users \\");
        System.out.println("       -H 'Content-Type: application/json' \\");
        System.out.println("       -d '{\"name\":\"\",\"email\":\"bad\"}' | jq");
    }
}
