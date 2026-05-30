package interview.level2_web.rest;

/**
 * Custom exception for user not found.
 * Handled by GlobalExceptionHandler → returns 404.
 */
public class UserNotFoundException extends RuntimeException {

    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("User not found with id: " + userId);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
