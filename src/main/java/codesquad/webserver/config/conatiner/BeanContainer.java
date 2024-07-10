package codesquad.webserver.config.conatiner;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.exception.BeanCreationException;
import codesquad.webserver.exception.BeanNotFoundException;
import codesquad.webserver.exception.CircularDependencyException;
import codesquad.webserver.exception.NoSuitableConstructorException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanContainer {
    private static final Logger log = LoggerFactory.getLogger(BeanContainer.class);
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
        beanClasses.keySet().forEach(this::createBean);
    }

    public Object getBean(String name) {
        Object bean = beans.get(name);
        if (bean == null) {
            if (!beanClasses.containsKey(name)) {
                throw new BeanNotFoundException("No bean found with name: " + name);
            }
            bean = createBean(name);
        }
        return bean;
    }

    private void registerType(Class<?> type, String name) {
        if (!typeToNameMap.containsKey(type)) {
            typeToNameMap.put(type, name);
        }
    }

    private Object createBean(String beanName) {
        if (beansInCreation.contains(beanName)) {
            throw new CircularDependencyException("Circular dependency detected for bean: " + beanName);
        }

        beansInCreation.add(beanName);

        try {
            Class<?> beanClass = Optional.ofNullable(beanClasses.get(beanName))
                    .orElseThrow(() -> new BeanNotFoundException("No bean class found for name: " + beanName));

            Constructor<?> constructor = findSuitableConstructor(beanClass);
            Object[] params = getConstructorParams(constructor);
            Object bean = constructor.newInstance(params);

            beans.put(beanName, bean);
            log.debug("Created and registered bean '{}': {}", beanName, bean);

            return bean;
        } catch (Exception e) {
            throw new BeanCreationException("Failed to create bean: " + beanName, e);
        } finally {
            beansInCreation.remove(beanName);
        }
    }

    private Constructor<?> findSuitableConstructor(Class<?> beanClass) {
        return Arrays.stream(beanClass.getDeclaredConstructors())
                .filter(c -> c.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseGet(() -> Arrays.stream(beanClass.getDeclaredConstructors())
                        .max(Comparator.comparingInt(Constructor::getParameterCount))
                        .orElseThrow(() -> new NoSuitableConstructorException(
                                "No suitable constructor found for " + beanClass.getName())));
    }

    private Object[] getConstructorParams(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::resolveParameter)
                .toArray();
    }

    private Object resolveParameter(Class<?> paramType) {
        if (paramType.isPrimitive() || paramType == String.class) {
            return getDefaultValueForType(paramType);
        }
        String beanName = typeToNameMap.get(paramType);
        if (beanName == null) {
            throw new BeanNotFoundException("No suitable constructor found for " + paramType.getName());
        }
        return getBean(beanName);
    }

    private Object getDefaultValueForType(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return 0;
        }
        if (type == long.class || type == Long.class) {
            return 0L;
        }
        if (type == double.class || type == Double.class) {
            return 0.0;
        }
        if (type == float.class || type == Float.class) {
            return 0.0f;
        }
        if (type == boolean.class || type == Boolean.class) {
            return false;
        }
        if (type == char.class || type == Character.class) {
            return '\u0000';
        }
        if (type == byte.class || type == Byte.class) {
            return (byte) 0;
        }
        if (type == short.class || type == Short.class) {
            return (short) 0;
        }
        if (type == String.class) {
            return "";
        }
        return null;
    }

    private String toBeanName(String className) {
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
}