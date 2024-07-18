package codesquad;

import codesquad.database.DatabaseInitializer;
import codesquad.handler.HandlersMapper;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final int MAXIMUM_THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException, SQLException {
        ExecutorService executorService = Executors.newFixedThreadPool(MAXIMUM_THREAD_POOL_SIZE);
        Server server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            DatabaseInitializer.initialize();
            server.start();

            log.debug("Listening for connection on port 8080 ....");
            while (true) {
                executorService.execute(new HttpRequestProcessor(serverSocket.accept(), new HandlersMapper()));
            }
        } finally {
            executorService.shutdown();
            server.stop();
        }
    }

}
