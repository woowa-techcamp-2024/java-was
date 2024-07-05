package codesquad.webserver;

import codesquad.webserver.dispatcher.HttpRequestDispatcher;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.parser.HttpParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final int TCP_KEEP_ALIVE_TIME = 30000; //ms단위
    private static final String KEEP_ALIVE = "keep-alive";
    private static final String KEEP_ALIVE_CLOSE = "close";
    private static final String KEEP_ALIVE_HEADER = "Connection";

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private final ExecutorService threadPool;
    private final int port;
    private final HttpRequestDispatcher dispatcher;

    public WebServer(int port, int poolSize, HttpRequestDispatcher dispatcher) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.port = port;
        this.dispatcher = dispatcher;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true); // 작업 중 껐다 켰다 하는 상황에 발생하는 포트 사용중 발생을 피하기 위해 임시 작성
            logger.info("Starting web server on port {}", port);
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();
                logger.info("연결");
                threadPool.execute(() -> handleConnection(client));
            }
        } catch (IOException e) {
            logger.error("Error while starting web server", e);
        }
    }

//    private void handleConnection(Socket client) {
//        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//             OutputStream outputStream = client.getOutputStream()) {
//            HttpRequest request = HttpParser.parse(in);
//            dispatcher.dispatch(request, outputStream);
//        } catch (Exception e) {
//            logger.error("Error while handling request", e);
//        } finally {
//            try {
//                client.close();
//            } catch (IOException e) {
//                logger.error("Error while closing connection", e);
//            }
//        }
//    }

    private void handleConnection(Socket client) {
        try {
            client.setSoTimeout(TCP_KEEP_ALIVE_TIME);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream out = client.getOutputStream();

            while (true) {
                HttpRequest request = HttpParser.parse(in);
                dispatcher.dispatch(request, out);
                if (isKeepAliveRequestClosed(request) || !isKeepAliveRequest(request)) {
                    break;
                }
            }

        } catch (Exception e) {
            logger.error("Error while handling connection", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    private boolean isKeepAliveRequest(HttpRequest request) {
        String connection = request.headers().get(KEEP_ALIVE_HEADER);
        return KEEP_ALIVE.equalsIgnoreCase(connection);
    }

    private boolean isKeepAliveRequestClosed(HttpRequest request) {
        String closed = request.headers().get(KEEP_ALIVE_HEADER);
        return KEEP_ALIVE_CLOSE.equalsIgnoreCase(closed);
    }

}
