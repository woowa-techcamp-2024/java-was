package codesquad;

import codesquad.webserver.WebServer;

import java.io.IOException;

public class Main {

    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        WebServer server = new WebServer();
        server.setUp(PORT);
        server.run();
    }
}
