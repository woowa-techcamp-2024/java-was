package codesquad.webserver.config.conatiner;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.exception.BeanCreationException;
import codesquad.webserver.exception.BeanNotFoundException;
import codesquad.webserver.exception.CircularDependencyException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanContainer {
    private static final Logger logger = LoggerFactory.getLogger(BeanContainer.class);
    private final Map<String, Object> beans = new HashMap<>();
    private final Map<String, Class<?>> beanClasses = new HashMap<>();
    private final Map<Class<?>, String> typeToNameMap = new HashMap<>();
    private final Set<String> beansInCreation = new HashSet<>();

    public void registerBeanClass(String name, Class<?> beanClass) {
        beanClasses.put(name, beanClass);
        registerType(beanClass, name);
        for (Class<?> iface : beanClass.getInterfaces()) {
            registerType(iface, name);
        }
    }

    public void instantiateAndRegister() {
        List<String> remainingBeans = new ArrayList<>(beanClasses.keySet());
        while (!remainingBeans.isEmpty()) {
            boolean progress = false;
            Iterator<String> iterator = remainingBeans.iterator();
            while (iterator.hasNext()) {
                String beanName = iterator.next();
                if (canCreateBean(beanName)) {
                    createAndRegisterBean(beanName);
                    iterator.remove();
                    progress = true;
                }
            }
            if (!progress) {
                throw new BeanCreationException(
                        "Unable to create beans due to unresolved dependencies: " + remainingBeans);
            }
        }
    }

    private boolean canCreateBean(String beanName) {
        Class<?> beanClass = beanClasses.get(beanName);
        Constructor<?> constructor = findAutowiredConstructor(beanClass);
        if (constructor == null) {
            return true; // 의존성이 없는 빈
        }
        for (Class<?> paramType : constructor.getParameterTypes()) {
            String dependencyName = typeToNameMap.get(paramType);
            if (dependencyName != null && !beans.containsKey(dependencyName)) {
                return false; // 의존성이 아직 생성되지 않음
            }
        }
        return true;
    }

    private void createAndRegisterBean(String beanName) {
        if (beansInCreation.contains(beanName)) {
            throw new CircularDependencyException("Circular dependency detected for bean: " + beanName);
        }

        beansInCreation.add(beanName);
        try {
            Class<?> beanClass = beanClasses.get(beanName);
            Constructor<?> constructor = findAutowiredConstructor(beanClass);
            Object bean;
            if (constructor != null) {
                Object[] params = getConstructorParams(constructor);
                bean = constructor.newInstance(params);
            } else {
                bean = beanClass.getDeclaredConstructor().newInstance();
            }
            beans.put(beanName, bean);
            logger.debug("Created and registered bean '{}': {}", beanName, bean);
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create bean: " + beanName, e);
        } finally {
            beansInCreation.remove(beanName);
        }
    }

    public Object getBean(String name) {
        Object bean = beans.get(name);
        if (bean == null) {
            throw new BeanNotFoundException("No bean found with name: " + name);
        }
        return bean;
    }

    private void registerType(Class<?> type, String name) {
        typeToNameMap.putIfAbsent(type, name);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> beanClass) {
        return Arrays.stream(beanClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::resolveParameter)
                .toArray();
    }

    private Object resolveParameter(Class<?> paramType) {
        String beanName = typeToNameMap.get(paramType);
        if (beanName == null) {
            throw new BeanNotFoundException("No bean found for type: " + paramType.getName());
        }
        return getBean(beanName);
    }
}