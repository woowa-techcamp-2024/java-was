package codesquad;

import codesquad.handler.HttpRequestHandler;
import codesquad.handler.MappingMediaTypeFileExtensionResolver;
import codesquad.handler.StaticResourceHandler;
import codesquad.http.HttpRequestMapper;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebApplicationServer {

    public static final int PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);

    private final HttpRequestMapper httpRequestMapper = new HttpRequestMapper();
    private final HttpRequestHandler httpRequestHandler =
            new HttpRequestHandler(new StaticResourceHandler(), new MappingMediaTypeFileExtensionResolver());
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.debug("Listening for connection on port 8080 ....");
            run(serverSocket);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void run(ServerSocket serverSocket) {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                HttpProcessor httpProcessor = new HttpProcessor(httpRequestMapper, httpRequestHandler, socket);
                executorService.execute(httpProcessor::process);
            } catch (IOException | RuntimeException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

}
