package codesquad;

import codesquad.configuration.Container;
import codesquad.webserver.Connector;

public class Main {
    public static void main(String[] args) {
        Container container = new Container();
        Connector connector = container.connector();
        connector.start();
    }
}
