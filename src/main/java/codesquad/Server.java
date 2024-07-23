package codesquad;

import codesquad.was.ConnectionHandler;
import codesquad.was.RequestHandlerMapping;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final int nThread;

    public Server(final int port, final int nThread) {
        this.port = port;
        this.nThread = nThread;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.debug("Listening for connection on port 8080 ....");

            ExecutorService executor = Executors.newFixedThreadPool(nThread);
            RequestHandlerMapping requestHandlerMapping = new RequestHandlerMapping();

            while (true) {
                executor.execute(new ConnectionHandler(serverSocket.accept(), requestHandlerMapping));
            }
        } catch (IOException e) {
            log.error("서버 소켓에 오류가 발생했습니다.", e);
        }
    }
}
