package codesquad.application;


import codesquad.webserver.server.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.run();
    }
}
