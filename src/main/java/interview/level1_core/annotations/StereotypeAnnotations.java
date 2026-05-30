package interview.level1_core.annotations;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * Q2. What is the difference between @Component, @Service, @Repository, and @Controller?
 *
 * All four are stereotype annotations — they mark classes for Spring to manage.
 * All are specializations of @Component.
 *
 * | Annotation    | Layer              | Extra Behavior                              |
 * |--------------|--------------------|--------------------------------------------|
 * | @Component   | Generic            | None                                        |
 * | @Service     | Business logic     | None (semantic/documentation only)          |
 * | @Repository  | Data access (DAO)  | Exception translation (SQL → DataAccessException) |
 * | @Controller  | Web (MVC)          | Works with @RequestMapping for views        |
 * | @RestController | Web (REST API)  | = @Controller + @ResponseBody               |
 *
 * Why use @Service instead of @Component?
 *   - Communicates intent to other developers
 *   - Spring may add behavior in the future
 *   - AOP pointcuts can target by annotation (@Service specifically)
 *
 * @Repository is special:
 *   - Enables PersistenceExceptionTranslationPostProcessor
 *   - Translates DB-specific exceptions (e.g., MySQL SQLIntegrityConstraintViolationException)
 *     into Spring's DataAccessException hierarchy (portable across DBs)
 */
public class StereotypeAnnotations {

    @Component
    static class UtilityHelper {
        String help() { return "I'm a generic @Component"; }
    }

    @Service
    static class OrderService {
        String process() { return "I'm a @Service — business logic layer"; }
    }

    @Repository
    static class OrderRepository {
        String query() { return "I'm a @Repository — data access layer (exception translation enabled)"; }
    }

    @Controller
    static class OrderController {
        String handle() { return "I'm a @Controller — web/MVC layer"; }
    }

    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.scan("interview.level1_core.annotations");
        ctx.refresh();

        System.out.println("=== Stereotype Annotations ===\n");
        System.out.println(ctx.getBean(UtilityHelper.class).help());
        System.out.println(ctx.getBean(OrderService.class).process());
        System.out.println(ctx.getBean(OrderRepository.class).query());
        System.out.println(ctx.getBean(OrderController.class).handle());

        // All are @Component under the hood
        System.out.println("\n=== All are @Component specializations ===");
        System.out.println("@Service meta-annotated with @Component: " +
                Service.class.isAnnotationPresent(Component.class));
        System.out.println("@Repository meta-annotated with @Component: " +
                Repository.class.isAnnotationPresent(Component.class));
        System.out.println("@Controller meta-annotated with @Component: " +
                Controller.class.isAnnotationPresent(Component.class));

        ctx.close();
    }
}
