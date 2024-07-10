package codesquad.webserver.config.conatiner;

import java.util.Set;

public interface ClassScanner {

    //TODO: scanPackage 현재 동작 똑같다... 응집도 높혀야함.

    Set<Class<?>> scanPackage(String basePackage) throws Exception;

    Set<Class<?>> scanPackage(Class<?> basePackage) throws Exception;
}
