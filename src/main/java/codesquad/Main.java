package codesquad;

import codesquad.webserver.config.conatiner.ApplicationContext;
import codesquad.webserver.config.conatiner.SimpleApplicationContext;


public class Main {
//TODO: 쿠키 여러 설정이 적용 안되는 문제 발생, 핸들러 추가 방식 변경
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new SimpleApplicationContext();
        applicationContext.initialize(Main.class);
    }
}
