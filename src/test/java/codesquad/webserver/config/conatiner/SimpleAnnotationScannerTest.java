package codesquad.webserver.config.conatiner;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleAnnotationScannerTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation1 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2 {
    }

    @TestAnnotation1
    static class TestClass1 {
    }

    @TestAnnotation2
    static class TestClass2 {
    }

    static class TestClass3 {
    }

    @TestAnnotation1
    @TestAnnotation2
    static class TestClass4 {
    }

    @TestAnnotation1
    interface TestInterface {
    }

    @Test
    @DisplayName("단일 어노테이션으로 클래스를 찾을 수 있다")
    void findClassesWithSingleAnnotation() {
        // Given
        SimpleAnnotationScanner scanner = new SimpleAnnotationScanner();
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestClass1.class);
        classes.add(TestClass2.class);
        classes.add(TestClass3.class);

        // When
        Set<Class<?>> result = scanner.findAnnotatedClasses(classes, TestAnnotation1.class);

        // Then
        assertThat(result).containsExactly(TestClass1.class);
    }

    @Test
    @DisplayName("여러 어노테이션으로 클래스를 찾을 수 있다")
    void findClassesWithMultipleAnnotations() {
        // Given
        SimpleAnnotationScanner scanner = new SimpleAnnotationScanner();
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestClass1.class);
        classes.add(TestClass2.class);
        classes.add(TestClass3.class);
        classes.add(TestClass4.class);

        // When
        Set<Class<?>> result = scanner.findAnnotatedClasses(classes, TestAnnotation1.class, TestAnnotation2.class);

        // Then
        assertThat(result).containsExactlyInAnyOrder(TestClass1.class, TestClass2.class, TestClass4.class);
    }

    @Test
    @DisplayName("어노테이션이 없는 클래스는 찾지 않는다")
    void doesNotFindClassesWithoutAnnotations() {
        // Given
        SimpleAnnotationScanner scanner = new SimpleAnnotationScanner();
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestClass1.class);
        classes.add(TestClass2.class);
        classes.add(TestClass3.class);

        // When
        Set<Class<?>> result = scanner.findAnnotatedClasses(classes, TestAnnotation1.class, TestAnnotation2.class);

        // Then
        assertThat(result).doesNotContain(TestClass3.class);
    }

    @Test
    @DisplayName("인터페이스는 결과에서 제외된다")
    void doesNotFindInterfaces() {
        // Given
        SimpleAnnotationScanner scanner = new SimpleAnnotationScanner();
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TestClass1.class);
        classes.add(TestInterface.class);

        // When
        Set<Class<?>> result = scanner.findAnnotatedClasses(classes, TestAnnotation1.class);

        // Then
        assertThat(result).containsExactly(TestClass1.class);
        assertThat(result).doesNotContain(TestInterface.class);
    }
}