package interview.level6_microservices.discovery;

/**
 * Q3. What is Service Discovery and why is it needed?
 *
 * Problem: In microservices, services run on dynamic IPs/ports (containers, K8s).
 *   Hardcoding URLs (http://192.168.1.5:8082) breaks when instances scale or move.
 *
 * Solution: Service Discovery — services register themselves, others look them up by name.
 *
 * Two patterns:
 *   Client-side discovery (Eureka, Consul):
 *     1. Service registers with discovery server on startup
 *     2. Client queries discovery server for "user-service" → gets list of IPs
 *     3. Client picks one (load balancing) and calls it directly
 *
 *   Server-side discovery (K8s, AWS ALB):
 *     1. Service registers with discovery server
 *     2. Client calls discovery server/load balancer
 *     3. Load balancer routes to healthy instance
 *
 * Spring Cloud tools:
 *   Eureka:  Netflix OSS, self-hosted, Spring native
 *   Consul:  HashiCorp, multi-DC, key-value store included
 *   K8s DNS: built-in if running on Kubernetes (no extra server)
 *
 * Q4. How does Eureka work?
 *
 *   Eureka Server (@EnableEurekaServer):
 *     - Registry of all service instances
 *     - Heartbeat check (default 30s)
 *     - Evicts instances that miss heartbeats
 *
 *   Eureka Client (@EnableDiscoveryClient):
 *     - Registers on startup (name, host, port)
 *     - Sends heartbeat every 30s
 *     - Fetches registry to discover other services
 *     - Uses @LoadBalanced RestTemplate or WebClient
 *
 *   Example:
 *     @LoadBalanced
 *     @Bean
 *     RestTemplate restTemplate() { return new RestTemplate(); }
 *
 *     // Call by service name, not IP
 *     restTemplate.getForObject("http://user-service/api/users/1", User.class);
 */
public class ServiceDiscoveryExplained {

    public static void main(String[] args) {
        System.out.println("=== Service Discovery ===\n");

        System.out.println("=== Problem ===");
        System.out.println("Services run on dynamic IPs/ports → can't hardcode URLs\n");

        System.out.println("=== Client-side (Eureka) ===");
        System.out.println("1. Service registers with Eureka Server");
        System.out.println("2. Client queries Eureka: 'give me user-service instances'");
        System.out.println("3. Client load-balances and calls directly\n");

        System.out.println("=== Server-side (K8s, ALB) ===");
        System.out.println("1. Service registers with load balancer");
        System.out.println("2. Client calls load balancer");
        System.out.println("3. Load balancer routes to healthy instance\n");

        System.out.println("=== Spring Cloud ===");
        System.out.println("Eureka Server: @EnableEurekaServer");
        System.out.println("Eureka Client: @EnableDiscoveryClient");
        System.out.println("Call by name:  restTemplate.getForObject(\"http://user-service/...\")");
    }
}
