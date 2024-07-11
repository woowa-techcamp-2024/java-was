package codesquad.webserver.config.conatiner;

import codesquad.webserver.WebServer;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.annotation.Controller;
import codesquad.webserver.filter.Filter;
import codesquad.webserver.filter.FilterChain;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApplicationContext implements ApplicationContext {
    private static final Logger logger = LoggerFactory.getLogger(SimpleApplicationContext.class);

    private final BeanContainer beanContainer;
    private final ClassScanner classScanner;
    private final AnnotationScanner annotationScanner;

    public SimpleApplicationContext() {
        this.beanContainer = new BeanContainer();
        this.classScanner = new SimpleClassScanner();
        this.annotationScanner = new SimpleAnnotationScanner();
    }

    @Override
    public void initialize(Class<?> mainClass) throws Exception {
        String basePackage = mainClass.getPackage().getName();
        initialize(basePackage);
    }

    @Override
    public void initialize(String basePackage) throws Exception {
        logger.info("Initializing application context with base package: {}", basePackage);
        Set<Class<?>> scannedClasses = classScanner.scanPackage(basePackage);
        Set<Class<?>> beanClasses = findBeanClasses(scannedClasses);
        registerBeans(beanClasses);
        beanContainer.instantiateAndRegister();

        initFilterCharin(scannedClasses);
        startWebServer();

        logger.debug("Application context initialization completed");
    }

    private void initFilterCharin(Set<Class<?>> scannedClasses) {
        logger.debug("Initializing filterChain");
        FilterChain filterChain = (FilterChain) beanContainer.getBean("filterChain");
        List<Filter> filters = new ArrayList<>();

        for (Class<?> clazz : scannedClasses) {
            if (Filter.class.isAssignableFrom(clazz) && clazz.isAnnotationPresent(codesquad.webserver.annotation.Filter.class)) {
                try {
                    Filter filter = (Filter) beanContainer.getBean(toBeanName(clazz.getSimpleName()));
                    filters.add(filter);
                    logger.debug("Added filter: {}", filter);
                } catch (Exception e) {
                    logger.error("Error creating Filter instance : {}", clazz.getName(), e);
                }
            }
        }

        for (Filter filter : filters) {
            filterChain.addFilter(filter, filter.getOrder());
        }

        logger.debug("FilterChain initialized");
    }

    private void startWebServer() {
        WebServer webServer = (WebServer) beanContainer.getBean("webServer");
        webServer.start();
        logger.error("web server injection filter chain : {}", webServer.getFilterChain());
    }

    private Set<Class<?>> findBeanClasses(Set<Class<?>> scannedClasses) {
        return annotationScanner.findAnnotatedClasses(scannedClasses, Component.class, Controller.class, codesquad.webserver.annotation.Filter.class);
    }

    private void registerBeans(Set<Class<?>> beanClasses) {
        beanClasses.forEach(beanClass -> {
            String beanName = toBeanName(beanClass.getSimpleName());
            beanContainer.registerBeanClass(beanName, beanClass);
            logger.debug("Registered bean class: {} with name: {}", beanClass.getName(), beanName);
        });
    }

    @Override
    public <T> T getBean(String name) {
        return (T) beanContainer.getBean(name);
    }

    private String toBeanName(String className) {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}