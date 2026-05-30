package interview.level2_web.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Shared UserRepository for Level 2 demos.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContainingIgnoreCase(String name);
}
