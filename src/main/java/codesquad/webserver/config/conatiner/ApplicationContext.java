package codesquad.webserver.config.conatiner;

public interface ApplicationContext {

    //TODO: initialize 현재 동작 똑같다... 응집도 높혀야함.

    void initialize(String basePackage) throws Exception;

    void initialize(Class<?> mainClass) throws Exception;

    <T> T getBean(String name);
}
