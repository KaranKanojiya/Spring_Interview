package interview.level1_core.annotations;

/**
 * Q18. What is the difference between @SpringBootApplication and its component annotations?
 *
 * @SpringBootApplication = THREE annotations combined:
 *
 *   @Configuration
 *     → This class can define @Bean methods
 *     → Uses CGLIB proxy for singleton enforcement
 *
 *   @EnableAutoConfiguration
 *     → Triggers Spring Boot auto-configuration
 *     → Reads META-INF/spring/...AutoConfiguration.imports
 *     → Creates beans based on classpath + conditions
 *
 *   @ComponentScan
 *     → Scans current package and sub-packages for @Component classes
 *     → Discovers @Service, @Repository, @Controller, etc.
 *     → Default: scans the package of the class annotated with @SpringBootApplication
 *
 * This is why the main application class should be in the ROOT package:
 *   com.myapp/
 *     MyApplication.java        ← @SpringBootApplication here
 *     controller/               ← scanned automatically
 *     service/                  ← scanned automatically
 *     repository/               ← scanned automatically
 *
 * If MyApplication is in com.myapp.config, it won't scan com.myapp.controller!
 */
public class SpringBootApplicationBreakdown {

    public static void main(String[] args) {
        System.out.println("=== @SpringBootApplication Breakdown ===\n");

        System.out.println("@SpringBootApplication is equivalent to:\n");
        System.out.println("@Configuration           → defines @Bean methods");
        System.out.println("@EnableAutoConfiguration → auto-configures based on classpath");
        System.out.println("@ComponentScan           → scans for @Component classes\n");

        System.out.println("=== Package structure matters ===");
        System.out.println("com.myapp/");
        System.out.println("  Application.java       ← @SpringBootApplication here");
        System.out.println("  controller/            ← ✅ scanned (sub-package)");
        System.out.println("  service/               ← ✅ scanned (sub-package)");
        System.out.println("com.other/               ← ❌ NOT scanned (different root)");

        System.out.println("\n=== Customization ===");
        System.out.println("Exclude auto-config:");
        System.out.println("  @SpringBootApplication(exclude = SecurityAutoConfiguration.class)");
        System.out.println("\nCustom scan base:");
        System.out.println("  @SpringBootApplication(scanBasePackages = {\"com.myapp\", \"com.other\"})");

        System.out.println("\n=== Equivalent explicit config ===");
        System.out.println("@Configuration");
        System.out.println("@EnableAutoConfiguration");
        System.out.println("@ComponentScan(basePackages = \"com.myapp\")");
        System.out.println("public class Application { ... }");
    }
}
