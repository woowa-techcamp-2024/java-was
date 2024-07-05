package codesquad;

import codesquad.webserver.Connector;

public class Main {
    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.start();
    }
}
