package interview.level6_microservices.config;

/**
 * Q5. What is centralized configuration and why use Spring Cloud Config?
 *
 * Problem: 20 microservices × 4 environments = 80 config files.
 *   Changing DB password means updating 20 services. Painful.
 *
 * Solution: Centralized Config Server — one source of truth for all config.
 *
 * Spring Cloud Config:
 *   Config Server:
 *     - Stores config in Git repo, filesystem, or Vault
 *     - Serves config via REST: GET /application/dev → returns properties
 *     - @EnableConfigServer
 *
 *   Config Client:
 *     - Fetches config from server on startup
 *     - spring.config.import=configserver:http://config-server:8888
 *     - Can refresh without restart (@RefreshScope + POST /actuator/refresh)
 *
 * Q6. What is @RefreshScope?
 *
 * @RefreshScope allows beans to be re-created with updated config at runtime.
 *
 *   1. Config changes in Git
 *   2. POST /actuator/refresh (or Spring Cloud Bus for all instances)
 *   3. @RefreshScope beans are destroyed and re-created with new values
 *
 *   @RefreshScope
 *   @Component
 *   class FeatureFlags {
 *       @Value("${feature.new-ui:false}")
 *       private boolean newUiEnabled;
 *   }
 *
 * Alternatives to Spring Cloud Config:
 *   - Kubernetes ConfigMaps + Secrets
 *   - HashiCorp Vault (secrets management)
 *   - AWS Systems Manager Parameter Store
 *   - Environment variables (simplest)
 */
public class ConfigServerExplained {

    public static void main(String[] args) {
        System.out.println("=== Centralized Configuration ===\n");

        System.out.println("=== Problem ===");
        System.out.println("20 microservices × 4 envs = 80 config files to manage\n");

        System.out.println("=== Spring Cloud Config ===");
        System.out.println("Config Server: stores config in Git, serves via REST");
        System.out.println("Config Client: fetches config on startup\n");

        System.out.println("=== @RefreshScope ===");
        System.out.println("POST /actuator/refresh → re-creates beans with new config");
        System.out.println("No restart needed!\n");

        System.out.println("=== Alternatives ===");
        System.out.println("K8s ConfigMaps, HashiCorp Vault, AWS Parameter Store");
    }
}
