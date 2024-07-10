package codesquad.webserver.config.conatiner;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface AnnotationScanner {
    Set<Class<?>> findAnnotatedClasses(Set<Class<?>> classes, Class<? extends Annotation>... annotations);
}
