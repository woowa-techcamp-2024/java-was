package codesquad.webserver.init;

import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.handler.DynamicResourceHandler;
import codesquad.webserver.handler.HandlerMethod;
import codesquad.webserver.http.HttpMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicResourceHandlerInitializer {
    private static final Logger log = LoggerFactory.getLogger(DynamicResourceHandler.class);

    public static void init(DynamicResourceHandler dynamicResourceHandler, List<Class<?>> classes, Map<String, Object> databaseInstances) {
        log.info("classes length: {}", classes.size());
        log.info("classes: "+classes);
        log.info("databases: "+databaseInstances);

        for (Class<?> clazz : classes) {
            try {
                if (clazz.isAnnotationPresent(ApiHandler.class)) {
                    Object handler = createHandlerInstance(clazz, databaseInstances);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                            HttpMethod httpMethod = requestMapping.method();
                            String path = requestMapping.path();
                            dynamicResourceHandler.addRoute(httpMethod, path, new HandlerMethod(handler, method));
                        }
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.info("DynamicResourceHandler {}", dynamicResourceHandler);
    }

    private static Object createHandlerInstance(Class<?> handlerClass, Map<String, Object> databaseInstances) throws Exception {
        log.debug("create: "+handlerClass.getName());
        Constructor<?>[] constructors = handlerClass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length > 0) {
                Specify specify = parameters[0].getAnnotation(Specify.class);
                if (specify != null) {
                    Object databaseInstance = databaseInstances.get(specify.value());
                    if (databaseInstance != null) {
                        return constructor.newInstance(databaseInstance);
                    }
                }
            }
        }
        return handlerClass.getDeclaredConstructor().newInstance();
    }
}
