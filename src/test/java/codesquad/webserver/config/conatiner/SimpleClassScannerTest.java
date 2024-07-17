package codesquad.webserver.config.conatiner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SimpleClassScannerTest {

    // 테스트용 클래스들
    public static class TestClass1 {
    }

    public static class TestClass2 {
    }

    public interface TestInterface {
    }

    public @interface TestAnnotation {
    }

    @Test
    @DisplayName("지정된 패키지의 모든 클래스를 스캔한다")
    void scanPackageShouldFindAllClasses() throws Exception {
        // Given
        SimpleClassScanner scanner = new SimpleClassScanner();
        String packageToScan = this.getClass().getPackage().getName();

        // When
        Set<Class<?>> scannedClasses = scanner.scanPackage(packageToScan);

        // Then
        assertThat(scannedClasses)
                .contains(SimpleClassScannerTest.class, TestClass1.class, TestClass2.class)
                .contains(TestInterface.class)  // 인터페이스도 포함됩니다
                .doesNotContain(TestAnnotation.class);  // 어노테이션은 제외됩니다

        assertThat(scannedClasses).allSatisfy(clazz ->
                assertThat(clazz.getPackage().getName()).startsWith(packageToScan)
        );
    }

    @Test
    @DisplayName("메인 클래스의 패키지를 스캔한다")
    void scanPackageShouldFindClassesInMainClassPackage() throws Exception {
        // Given
        SimpleClassScanner scanner = new SimpleClassScanner();

        // When
        Set<Class<?>> scannedClasses = scanner.scanPackage(this.getClass());

        // Then
        assertThat(scannedClasses)
                .contains(SimpleClassScannerTest.class, TestClass1.class, TestClass2.class)
                .contains(TestInterface.class)
                .doesNotContain(TestAnnotation.class);

        String packageName = this.getClass().getPackage().getName();
        assertThat(scannedClasses).allSatisfy(clazz ->
                assertThat(clazz.getPackage().getName()).startsWith(packageName)
        );
    }
}