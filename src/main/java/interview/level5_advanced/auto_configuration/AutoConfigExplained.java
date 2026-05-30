package interview.level5_advanced.auto_configuration;

/**
 * Q9. How do you create a custom Spring Boot Starter?
 *
 * A starter is a module that provides auto-configuration for a library.
 *
 * Structure (two modules):
 *   mylib-spring-boot-autoconfigure/
 *     └── src/main/java/
 *         └── MyLibAutoConfiguration.java
 *     └── src/main/resources/
 *         └── META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 *             → contains: com.mylib.MyLibAutoConfiguration
 *
 *   mylib-spring-boot-starter/
 *     └── build.gradle (just depends on autoconfigure module)
 *
 * Auto-configuration class:
 *   @AutoConfiguration
 *   @ConditionalOnClass(MyLibClient.class)
 *   @EnableConfigurationProperties(MyLibProperties.class)
 *   public class MyLibAutoConfiguration {
 *
 *       @Bean
 *       @ConditionalOnMissingBean
 *       public MyLibClient myLibClient(MyLibProperties props) {
 *           return new MyLibClient(props.getApiKey(), props.getBaseUrl());
 *       }
 *   }
 *
 * Properties:
 *   @ConfigurationProperties(prefix = "mylib")
 *   class MyLibProperties {
 *       private String apiKey;
 *       private String baseUrl = "https://api.mylib.com";
 *   }
 *
 * Q10. What is @ConditionalOnMissingBean and why is it important?
 *
 * @ConditionalOnMissingBean → create this bean ONLY if the user hasn't defined their own.
 * This is the "back-off" mechanism of auto-configuration.
 *
 * Example:
 *   Auto-config creates default DataSource (HikariCP).
 *   If user defines @Bean DataSource → auto-config backs off.
 *   User's bean wins, auto-config is ignored.
 */
public class AutoConfigExplained {

    public static void main(String[] args) {
        System.out.println("=== Custom Spring Boot Starter ===\n");

        System.out.println("=== Structure ===");
        System.out.println("mylib-spring-boot-autoconfigure/");
        System.out.println("  MyLibAutoConfiguration.java");
        System.out.println("  META-INF/spring/...AutoConfiguration.imports");
        System.out.println("mylib-spring-boot-starter/");
        System.out.println("  build.gradle (depends on autoconfigure)\n");

        System.out.println("=== Key annotations ===");
        System.out.println("@AutoConfiguration          → marks as auto-config class");
        System.out.println("@ConditionalOnClass         → only if library on classpath");
        System.out.println("@ConditionalOnMissingBean   → back off if user defines their own");
        System.out.println("@EnableConfigurationProperties → bind properties to POJO");

        System.out.println("\n=== Debug auto-config ===");
        System.out.println("./gradlew bootRun --args='--debug'");
        System.out.println("curl -s http://localhost:8080/actuator/conditions | jq");
    }
}
