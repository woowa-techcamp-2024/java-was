package codesquad;

import codesquad.webserver.config.conatiner.ApplicationContext;
import codesquad.webserver.config.conatiner.SimpleApplicationContext;


public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new SimpleApplicationContext();
        applicationContext.initialize(Main.class);
    }
}
