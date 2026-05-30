package interview.level3_data.jpa;

import interview.level2_web.rest.User;
import interview.level2_web.rest.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import interview.level3_data.repositories.ProductRepository;
import interview.level3_data.repositories.OrderRepository;

import java.math.BigDecimal;

/**
 * Seeds Level 3 data (products and orders) on startup.
 */
@Component
@Order(2)  // run after Level 2 DataInitializer
public class Level3DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public Level3DataInitializer(ProductRepository productRepo, OrderRepository orderRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        // Products
        Product laptop = productRepo.save(new Product("Laptop", new BigDecimal("999.99"), Product.Category.ELECTRONICS));
        Product phone = productRepo.save(new Product("Phone", new BigDecimal("699.99"), Product.Category.ELECTRONICS));
        Product book = productRepo.save(new Product("Spring in Action", new BigDecimal("39.99"), Product.Category.BOOKS));
        Product shirt = productRepo.save(new Product("T-Shirt", new BigDecimal("19.99"), Product.Category.CLOTHING));
        Product coffee = productRepo.save(new Product("Coffee Beans", new BigDecimal("14.99"), Product.Category.FOOD));

        // Orders (using users from Level 2 seeder)
        User karan = userRepo.findById(1L).orElse(null);
        if (karan != null) {
            interview.level3_data.jpa.Order order1 = new interview.level3_data.jpa.Order(karan);
            order1.addItem(new OrderItem(laptop, 1));
            order1.addItem(new OrderItem(book, 2));
            order1.calculateTotal();
            orderRepo.save(order1);

            interview.level3_data.jpa.Order order2 = new interview.level3_data.jpa.Order(karan);
            order2.addItem(new OrderItem(phone, 1));
            order2.addItem(new OrderItem(coffee, 3));
            order2.calculateTotal();
            orderRepo.save(order2);
        }

        System.out.println("=== Level 3 data loaded: " + productRepo.count() + " products, " + orderRepo.count() + " orders ===\n");
    }
}
