package interview.level5_advanced.testing;

/**
 * Q13. What testing annotations does Spring Boot provide?
 *
 * | Annotation       | What it does                              | Speed  |
 * |-----------------|------------------------------------------|--------|
 * | @SpringBootTest  | Loads FULL application context             | Slow   |
 * | @WebMvcTest      | Loads only web layer (controllers)         | Fast   |
 * | @DataJpaTest     | Loads only JPA layer (repos + H2)          | Fast   |
 * | @MockBean        | Replaces a bean with Mockito mock           | —      |
 * | @SpyBean         | Wraps existing bean with Mockito spy       | —      |
 * | @TestConfiguration | Extra beans for tests only               | —      |
 * | @ActiveProfiles  | Activate specific profile for test         | —      |
 *
 * Q14. What is the difference between @SpringBootTest and @WebMvcTest?
 *
 * @SpringBootTest:
 *   - Loads FULL context (all beans, DB, security, etc.)
 *   - Use for integration tests
 *   - Can use @Autowired on any bean
 *   - Slow (starts entire app)
 *   - webEnvironment = MOCK (default), RANDOM_PORT, DEFINED_PORT, NONE
 *
 * @WebMvcTest(UserController.class):
 *   - Loads ONLY web layer (controller + filters + advice)
 *   - Does NOT load @Service, @Repository beans
 *   - Must @MockBean all dependencies
 *   - Uses MockMvc (no real HTTP)
 *   - Fast (minimal context)
 *
 * @DataJpaTest:
 *   - Loads ONLY JPA layer (entities + repositories)
 *   - Auto-configures H2 in-memory DB
 *   - Each test is @Transactional (rolls back after test)
 *   - Does NOT load @Controller, @Service
 *
 * Q15. What is MockMvc?
 *
 * MockMvc simulates HTTP requests without starting a real server.
 *
 *   mockMvc.perform(get("/api/users"))
 *       .andExpect(status().isOk())
 *       .andExpect(jsonPath("$[0].name").value("Karan"))
 *       .andExpect(content().contentType(MediaType.APPLICATION_JSON));
 *
 *   mockMvc.perform(post("/api/users")
 *       .contentType(MediaType.APPLICATION_JSON)
 *       .content("{\"name\":\"Test\",\"email\":\"t@t.com\"}"))
 *       .andExpect(status().isCreated());
 *
 * Example test class:
 *   @WebMvcTest(UserController.class)
 *   class UserControllerTest {
 *       @Autowired MockMvc mockMvc;
 *       @MockBean UserRepository userRepository;
 *
 *       @Test
 *       void shouldReturnUsers() throws Exception {
 *           when(userRepository.findAll()).thenReturn(List.of(new User("Karan","k@t.com")));
 *
 *           mockMvc.perform(get("/api/users"))
 *               .andExpect(status().isOk())
 *               .andExpect(jsonPath("$", hasSize(1)))
 *               .andExpect(jsonPath("$[0].name").value("Karan"));
 *       }
 *   }
 */
public class TestingExplained {

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Testing ===\n");

        System.out.println("=== Test slice annotations ===");
        System.out.println("@SpringBootTest:  full context (integration tests)");
        System.out.println("@WebMvcTest:      web layer only (controller tests)");
        System.out.println("@DataJpaTest:     JPA layer only (repository tests)");

        System.out.println("\n=== Mocking ===");
        System.out.println("@MockBean:  replace bean with Mockito mock");
        System.out.println("@SpyBean:   wrap bean with Mockito spy (partial mock)");

        System.out.println("\n=== MockMvc ===");
        System.out.println("mockMvc.perform(get(\"/api/users\"))");
        System.out.println("    .andExpect(status().isOk())");
        System.out.println("    .andExpect(jsonPath(\"$[0].name\").value(\"Karan\"));");

        System.out.println("\n=== Test pyramid ===");
        System.out.println("Unit tests (@WebMvcTest, @DataJpaTest):    MANY, fast");
        System.out.println("Integration tests (@SpringBootTest):       FEW, slow");
        System.out.println("E2E tests (Testcontainers):                FEWER, slowest");

        System.out.println("\n=== Run tests ===");
        System.out.println("./gradlew test");
        System.out.println("./gradlew test --tests '*UserControllerTest'");
    }
}
