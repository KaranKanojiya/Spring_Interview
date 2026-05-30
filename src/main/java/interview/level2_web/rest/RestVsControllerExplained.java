package interview.level2_web.rest;

/**
 * Q17. What is the difference between @Controller and @RestController?
 *
 * @Controller:
 *   - Returns VIEW names (e.g., "home", "users/list")
 *   - Spring resolves view via ViewResolver (Thymeleaf, JSP)
 *   - To return JSON: must add @ResponseBody on each method
 *
 * @RestController:
 *   - Returns DATA directly (serialized to JSON/XML)
 *   - = @Controller + @ResponseBody on every method
 *   - Every method's return value goes through HttpMessageConverter
 *
 * Example:
 *
 *   @Controller
 *   class WebController {
 *       @GetMapping("/home")
 *       String home() { return "home"; }       // → resolves home.html template
 *
 *       @GetMapping("/api/data")
 *       @ResponseBody                           // → returns JSON
 *       Data getData() { return new Data(); }
 *   }
 *
 *   @RestController                              // @ResponseBody on ALL methods
 *   class ApiController {
 *       @GetMapping("/api/users")
 *       List<User> getUsers() { return users; } // → returns JSON automatically
 *   }
 *
 * Q18. What is @RequestMapping's produces and consumes attribute?
 *
 *   @PostMapping(
 *       consumes = "application/json",           // only accepts JSON input
 *       produces = "application/json"             // only returns JSON output
 *   )
 *
 * If client sends XML but controller only consumes JSON → 415 Unsupported Media Type
 * If client wants XML but controller only produces JSON → 406 Not Acceptable
 */
public class RestVsControllerExplained {

    public static void main(String[] args) {
        System.out.println("=== @Controller vs @RestController ===\n");
        System.out.println("@Controller:     returns VIEW names (HTML templates)");
        System.out.println("@RestController: returns DATA (JSON/XML) — every method has @ResponseBody\n");

        System.out.println("=== When to use ===");
        System.out.println("@Controller:     server-rendered pages (Thymeleaf, JSP)");
        System.out.println("@RestController: REST APIs (frontend is React/Angular/mobile)");

        System.out.println("\n=== produces / consumes ===");
        System.out.println("consumes: what input format the endpoint accepts");
        System.out.println("produces: what output format the endpoint returns");
        System.out.println("Wrong format → 415 (Unsupported) or 406 (Not Acceptable)");
    }
}
