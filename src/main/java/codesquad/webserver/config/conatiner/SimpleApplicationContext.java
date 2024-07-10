package codesquad.webserver.config.conatiner;

import codesquad.webserver.WebServer;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.annotation.Controller;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApplicationContext implements ApplicationContext {
    private static final Logger log = LoggerFactory.getLogger(SimpleApplicationContext.class);

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
        log.info("Initializing application context with base package: {}", basePackage);
        Set<Class<?>> scannedClasses = classScanner.scanPackage(basePackage);
        Set<Class<?>> beanClasses = findBeanClasses(scannedClasses);
        registerBeans(beanClasses);
        beanContainer.instantiateAndRegister();

        WebServer webServer = (WebServer) beanContainer.getBean("webServer");
        webServer.start();

        log.info("Application context initialization completed");
    }

    private Set<Class<?>> findBeanClasses(Set<Class<?>> scannedClasses) {
        return annotationScanner.findAnnotatedClasses(scannedClasses, Component.class, Controller.class);
    }

    private void registerBeans(Set<Class<?>> beanClasses) {
        beanClasses.forEach(beanClass -> {
            String beanName = toBeanName(beanClass.getSimpleName());
            beanContainer.registerBeanClass(beanName, beanClass);
            log.debug("Registered bean class: {} with name: {}", beanClass.getName(), beanName);
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