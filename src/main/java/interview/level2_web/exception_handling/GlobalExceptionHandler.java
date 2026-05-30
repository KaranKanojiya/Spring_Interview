package interview.level2_web.exception_handling;

import interview.level2_web.rest.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Q4. What is @ControllerAdvice and how do you handle exceptions globally?
 *
 * @ControllerAdvice = global exception handler for ALL controllers.
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody (returns JSON).
 *
 * Without @ControllerAdvice:
 *   Each controller handles its own exceptions → duplicated try-catch
 *
 * With @ControllerAdvice:
 *   ONE class handles ALL exceptions → centralized, consistent error responses
 *
 * Q5. What is ProblemDetail (RFC 7807)?
 *
 * Spring 6+ supports RFC 7807 "Problem Details for HTTP APIs":
 *   {
 *     "type": "https://api.example.com/errors/not-found",
 *     "title": "User Not Found",
 *     "status": 404,
 *     "detail": "User not found with id: 999",
 *     "instance": "/api/users/999"
 *   }
 *
 * curl test:
 *   curl -s http://localhost:8080/api/users/999 | jq                    → 404 ProblemDetail
 *   curl -s -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{"name":"","email":"bad"}' | jq  → 400 validation errors
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle UserNotFoundException → 404
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("User Not Found");
        problem.setType(URI.create("https://api.example.com/errors/not-found"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("userId", ex.getUserId());
        return problem;
    }

    // Handle validation errors (@Valid fails) → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setTitle("Validation Error");

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        problem.setProperty("errors", fieldErrors);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // Catch-all for unexpected exceptions → 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problem.setTitle("Internal Server Error");
        problem.setProperty("timestamp", Instant.now());
        // Don't expose stack trace in production!
        return problem;
    }
}
