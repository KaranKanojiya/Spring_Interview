package interview.level1_core.bean_lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;

/**
 * Q4. What is the Bean lifecycle in Spring?
 *
 * Full lifecycle order:
 *   1. Constructor (instantiation)
 *   2. DI (populate properties — setter/field injection)
 *   3. BeanNameAware.setBeanName()
 *   4. BeanFactoryAware.setBeanFactory()
 *   5. ApplicationContextAware.setApplicationContext()
 *   6. BeanPostProcessor.postProcessBeforeInitialization()
 *   7. @PostConstruct
 *   8. InitializingBean.afterPropertiesSet()
 *   9. Custom init-method
 *  10. BeanPostProcessor.postProcessAfterInitialization()
 *  11. Bean is READY
 *  12. @PreDestroy
 *  13. DisposableBean.destroy()
 *  14. Custom destroy-method
 *
 * In practice, most apps only use:
 *   @PostConstruct — initialization logic
 *   @PreDestroy — cleanup (close connections, release resources)
 */
public class BeanLifecycleDemo {

    static class LifecycleBean implements BeanNameAware, BeanFactoryAware,
            ApplicationContextAware, InitializingBean, DisposableBean {

        private String beanName;

        LifecycleBean() {
            System.out.println("1. Constructor called");
        }

        @Override
        public void setBeanName(String name) {
            this.beanName = name;
            System.out.println("3. BeanNameAware.setBeanName(): " + name);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            System.out.println("4. BeanFactoryAware.setBeanFactory()");
        }

        @Override
        public void setApplicationContext(ApplicationContext ctx) throws BeansException {
            System.out.println("5. ApplicationContextAware.setApplicationContext()");
        }

        @PostConstruct
        void postConstruct() {
            System.out.println("7. @PostConstruct — initialization logic goes here");
        }

        @Override
        public void afterPropertiesSet() {
            System.out.println("8. InitializingBean.afterPropertiesSet()");
        }

        void customInit() {
            System.out.println("9. Custom init-method");
        }

        @PreDestroy
        void preDestroy() {
            System.out.println("12. @PreDestroy — cleanup logic goes here");
        }

        @Override
        public void destroy() {
            System.out.println("13. DisposableBean.destroy()");
        }

        void customDestroy() {
            System.out.println("14. Custom destroy-method");
        }
    }

    static class CustomBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof LifecycleBean) {
                System.out.println("6. BeanPostProcessor.postProcessBEFOREInitialization()");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof LifecycleBean) {
                System.out.println("10. BeanPostProcessor.postProcessAFTERInitialization()");
                System.out.println("11. *** Bean is READY ***");
            }
            return bean;
        }
    }

    @Configuration
    static class LifecycleConfig {
        @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
        LifecycleBean lifecycleBean() { return new LifecycleBean(); }

        @Bean
        CustomBeanPostProcessor customBeanPostProcessor() { return new CustomBeanPostProcessor(); }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Bean Lifecycle ===\n");
        System.out.println("--- CREATION ---");
        var ctx = new AnnotationConfigApplicationContext(LifecycleConfig.class);

        System.out.println("\n--- DESTRUCTION ---");
        ctx.close();

        System.out.println("\n=== In practice, just use ===");
        System.out.println("@PostConstruct — for init (load cache, validate config)");
        System.out.println("@PreDestroy    — for cleanup (close connections, flush buffers)");
    }
}
