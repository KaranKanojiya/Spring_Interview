package interview.level1_core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

/**
 * Q12. What is the difference between @Value and @ConfigurationProperties?
 *
 * | Feature          | @Value                    | @ConfigurationProperties       |
 * |-----------------|--------------------------|-------------------------------|
 * | Granularity     | Single property           | Group of related properties    |
 * | Type safety     | No (parsed from String)   | Yes (bound to typed POJO)     |
 * | Validation      | No                        | Yes (with @Validated + JSR-303)|
 * | Relaxed binding | Limited                   | Full (kebab-case, camelCase)  |
 * | Use case        | One-off values            | Structured config (db, mail)  |
 * | SpEL support    | Yes (#{...})              | No                            |
 *
 * @Value: quick and simple for individual values
 * @ConfigurationProperties: structured, validated, type-safe config groups
 *
 * Example properties:
 *   myapp.name=Spring Interview
 *   myapp.cache.ttl=300
 *   myapp.cache.max-size=1000
 */
public class ValueVsConfigProperties {

    // === @Value — single properties ===
    // @Value("${myapp.name:DefaultApp}")
    // private String appName;
    //
    // @Value("${myapp.cache.ttl:60}")
    // private int cacheTtl;
    //
    // @Value("#{${myapp.cache.ttl:60} * 2}")  // SpEL — double the TTL
    // private int doubledTtl;

    // === @ConfigurationProperties — structured group ===
    // @ConfigurationProperties(prefix = "myapp.cache")
    // public class CacheProperties {
    //     private int ttl = 60;           // default value
    //     private int maxSize = 500;
    //     private boolean enabled = true;
    //     // getters, setters
    // }
    //
    // Usage: @EnableConfigurationProperties(CacheProperties.class)
    // Then inject: CacheProperties cacheProperties

    public static void main(String[] args) {
        System.out.println("=== @Value vs @ConfigurationProperties ===\n");

        System.out.println("=== @Value examples ===");
        System.out.println("@Value(\"${server.port:8080}\")         → single value with default");
        System.out.println("@Value(\"${app.name}\")                 → required (fails if missing)");
        System.out.println("@Value(\"#{${ttl} * 1000}\")            → SpEL expression");
        System.out.println("@Value(\"${app.features:a,b,c}\")       → comma-separated → List");

        System.out.println("\n=== @ConfigurationProperties example ===");
        System.out.println("@ConfigurationProperties(prefix = \"myapp.mail\")");
        System.out.println("public class MailProperties {");
        System.out.println("    private String host;           // myapp.mail.host");
        System.out.println("    private int port = 587;        // myapp.mail.port (with default)");
        System.out.println("    private String username;       // myapp.mail.username");
        System.out.println("    private boolean ssl = true;    // myapp.mail.ssl");
        System.out.println("}");

        System.out.println("\n=== Relaxed binding (only @ConfigurationProperties) ===");
        System.out.println("myapp.mail.max-retry-count=3   → maxRetryCount (kebab → camelCase)");
        System.out.println("MYAPP_MAIL_HOST=smtp.gmail.com → mail.host (env var → property)");

        System.out.println("\n=== Validation (only @ConfigurationProperties) ===");
        System.out.println("@Validated");
        System.out.println("@ConfigurationProperties(prefix = \"myapp\")");
        System.out.println("class AppProps {");
        System.out.println("    @NotBlank String name;");
        System.out.println("    @Min(1) @Max(65535) int port;");
        System.out.println("}");

        System.out.println("\n=== When to use which ===");
        System.out.println("@Value:                  quick one-off values, SpEL needed");
        System.out.println("@ConfigurationProperties: structured config groups, validation, type-safety");
    }
}
