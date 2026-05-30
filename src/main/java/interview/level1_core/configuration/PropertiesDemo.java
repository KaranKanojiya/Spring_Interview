package interview.level1_core.configuration;

/**
 * Q11. What is the difference between application.properties and application.yml?
 *
 * Both configure Spring Boot. Same capabilities, different syntax.
 *
 * application.properties:
 *   server.port=8080
 *   spring.datasource.url=jdbc:h2:mem:db
 *   spring.datasource.username=sa
 *   myapp.features.list[0]=featureA
 *   myapp.features.list[1]=featureB
 *
 * application.yml:
 *   server:
 *     port: 8080
 *   spring:
 *     datasource:
 *       url: jdbc:h2:mem:db
 *       username: sa
 *   myapp:
 *     features:
 *       list:
 *         - featureA
 *         - featureB
 *
 * | Feature           | .properties          | .yml                  |
 * |------------------|---------------------|-----------------------|
 * | Format           | key=value            | YAML hierarchy         |
 * | Readability      | Flat, repetitive     | Nested, cleaner        |
 * | Multi-profile    | Separate files       | --- separator in one   |
 * | Lists            | Indexed: [0], [1]    | - item syntax          |
 * | Comments         | # or !               | # only                 |
 * | Precedence       | Higher if both exist | Lower                  |
 * | Spring support   | Full                 | Full                   |
 */
public class PropertiesDemo {

    public static void main(String[] args) {
        System.out.println("=== application.properties vs application.yml ===\n");

        System.out.println("=== .properties format ===");
        System.out.println("server.port=8080");
        System.out.println("spring.datasource.url=jdbc:h2:mem:db");
        System.out.println("spring.datasource.username=sa");
        System.out.println("myapp.cache.ttl=300");

        System.out.println("\n=== .yml format (same config) ===");
        System.out.println("server:");
        System.out.println("  port: 8080");
        System.out.println("spring:");
        System.out.println("  datasource:");
        System.out.println("    url: jdbc:h2:mem:db");
        System.out.println("    username: sa");
        System.out.println("myapp:");
        System.out.println("  cache:");
        System.out.println("    ttl: 300");

        System.out.println("\n=== Multi-profile in YAML (single file) ===");
        System.out.println("spring:");
        System.out.println("  profiles: default");
        System.out.println("server:");
        System.out.println("  port: 8080");
        System.out.println("---");
        System.out.println("spring:");
        System.out.println("  config.activate.on-profile: dev");
        System.out.println("server:");
        System.out.println("  port: 9090");

        System.out.println("\n=== Recommendation ===");
        System.out.println("Small projects: .properties (simple, familiar)");
        System.out.println("Large projects: .yml (cleaner hierarchy, less repetition)");
        System.out.println("Team preference: be consistent — pick one and stick with it");
    }
}
