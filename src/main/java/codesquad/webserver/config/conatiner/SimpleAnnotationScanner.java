package codesquad.webserver.config.conatiner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleAnnotationScanner implements AnnotationScanner {

    @Override
    public Set<Class<?>> findAnnotatedClasses(Set<Class<?>> classes, Class<? extends Annotation>... annotations) {
        Set<Class<? extends Annotation>> annotationSet = Arrays.stream(annotations)
                .collect(Collectors.toSet());

        return classes.stream()
                .filter(clazz -> !clazz.isAnnotation() && !clazz.isInterface())
                .filter(clazz -> Arrays.stream(clazz.getAnnotations())
                        .map(Annotation::annotationType)
                        .anyMatch(annotationSet::contains))
                .collect(Collectors.toSet());
    }
}