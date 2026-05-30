package interview.level2_web.validation;

import jakarta.validation.*;
import java.lang.annotation.*;

/**
 * Q6. How does validation work in Spring Boot? What is @Valid?
 *
 * Spring Boot uses Bean Validation (JSR-380) via Hibernate Validator.
 *
 * Flow:
 *   1. Add @Valid to @RequestBody parameter in controller
 *   2. Spring validates the object BEFORE the method executes
 *   3. If validation fails → MethodArgumentNotValidException → 400 Bad Request
 *
 * Built-in annotations:
 *   @NotNull, @NotBlank, @NotEmpty
 *   @Size(min=, max=), @Min, @Max
 *   @Email, @Pattern(regexp=)
 *   @Past, @Future (dates)
 *   @Positive, @Negative
 *
 * Q7. How to create a custom validator?
 *
 * 1. Create annotation (e.g., @NoSpecialChars)
 * 2. Create ConstraintValidator implementation
 * 3. Apply annotation to field
 *
 * curl test:
 *   curl -s -X POST http://localhost:8080/api/users \
 *     -H "Content-Type: application/json" \
 *     -d '{"name":"","email":"invalid"}' | jq
 *   → Returns 400 with field-level errors
 */
// === Step 1: Custom annotation ===
@Documented
@Constraint(validatedBy = CustomValidator.NoSpecialCharsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidator {

    String message() default "Must not contain special characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // === Step 2: Validator implementation ===
    class NoSpecialCharsValidator implements ConstraintValidator<CustomValidator, String> {

        @Override
        public void initialize(CustomValidator constraintAnnotation) {
            // initialization if needed
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) return true;  // let @NotNull handle null
            return value.matches("^[a-zA-Z0-9 ]*$");  // only alphanumeric + space
        }
    }
}

// Usage on entity field:
// @CustomValidator(message = "Name must not contain special characters")
// private String name;
