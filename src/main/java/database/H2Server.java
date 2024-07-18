package database;

import org.h2.tools.Server;
import java.sql.SQLException;

public class H2Server {
    private Server server;
    private Server webServer;

    public void start() throws SQLException {
        server = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9092", "-ifNotExists").start();
        System.out.println("H2 server started and listening on port 9092.");
        webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
    }

    public void stop() {
        if (server != null) {
            server.stop();
            System.out.println("H2 server stopped.");
        }
        if (webServer != null) {
            webServer.stop();
            System.out.println("H2 web server stopped.");
        }
    }
}
