package interview.level2_web.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the H2 database with sample data on startup.
 * This allows testing REST endpoints immediately after bootRun.
 */
@Component
@org.springframework.core.annotation.Order(1)  // run FIRST (before Level 3 seeder)
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        userRepository.save(new User("Karan", "karan@test.com", 30));
        userRepository.save(new User("John", "john@test.com", 25));
        userRepository.save(new User("Alice", "alice@test.com", 28));
        System.out.println("\n=== Sample data loaded: " + userRepository.count() + " users ===\n");
    }
}
