package interview.level1_core.di;

/**
 * Q7. What is the difference between constructor and field injection?
 *
 * | Feature              | Constructor Injection     | Field Injection           |
 * |---------------------|--------------------------|--------------------------|
 * | Testability         | Easy — pass mocks in ctor | Hard — needs reflection   |
 * | Immutability        | Fields can be final       | Cannot be final           |
 * | Required deps       | Enforced at compile time  | NPE at runtime if null    |
 * | Circular deps       | Fails fast at startup     | May silently resolve      |
 * | Spring recommendation| RECOMMENDED              | DISCOURAGED               |
 * | Boilerplate         | More (constructor code)   | Less (just @Autowired)    |
 *
 * Field injection looks cleaner but hides dependencies and makes testing hard.
 * Constructor injection is explicit, testable, and safe.
 *
 * This file is a conceptual demo — no Spring context needed.
 */
public class ConstructorVsFieldInjection {

    // === Interface ===
    interface UserRepository {
        String findById(long id);
    }

    static class JpaUserRepository implements UserRepository {
        @Override
        public String findById(long id) { return "User-" + id; }
    }

    // === BAD: Field Injection ===
    static class UserServiceFieldInjection {
        // @Autowired — Spring would inject this, but:
        // 1. Can't be final
        // 2. Hard to test without Spring context
        // 3. Dependency is hidden — not visible in constructor
        private UserRepository userRepository;

        void setUserRepository(UserRepository repo) { this.userRepository = repo; }

        String getUser(long id) {
            return userRepository.findById(id); // NPE if not injected!
        }
    }

    // === GOOD: Constructor Injection ===
    static class UserServiceConstructorInjection {
        private final UserRepository userRepository; // FINAL — immutable

        // Dependency is explicit — visible in constructor signature
        UserServiceConstructorInjection(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        String getUser(long id) {
            return userRepository.findById(id); // Never null — guaranteed by constructor
        }
    }

    public static void main(String[] args) {

        UserRepository repo = new JpaUserRepository();

        // === Constructor Injection: easy to test ===
        System.out.println("=== Constructor Injection ===");
        UserServiceConstructorInjection ctorService = new UserServiceConstructorInjection(repo);
        System.out.println("Result: " + ctorService.getUser(1));
        System.out.println("Testing: just pass a mock in constructor — done!");

        // === Field Injection: harder to test ===
        System.out.println("\n=== Field Injection ===");
        UserServiceFieldInjection fieldService = new UserServiceFieldInjection();
        // fieldService.getUser(1); // Would throw NPE — dependency not set!
        fieldService.setUserRepository(repo);
        System.out.println("Result: " + fieldService.getUser(1));
        System.out.println("Testing: need to use setter or reflection — messy!");

        // === Unit test comparison ===
        System.out.println("\n=== Unit Test Comparison ===");
        System.out.println("Constructor injection test:");
        System.out.println("  UserRepository mock = Mockito.mock(UserRepository.class);");
        System.out.println("  UserService service = new UserService(mock); // clean!");
        System.out.println("\nField injection test:");
        System.out.println("  UserService service = new UserService();");
        System.out.println("  ReflectionTestUtils.setField(service, \"repo\", mock); // ugly!");
    }
}
