package codesquad.webserver;

import codesquad.servlet.HandlerMapper;
import codesquad.servlet.MappingMediaTypeFileExtensionResolver;
import codesquad.servlet.StaticResourceReader;
import codesquad.servlet.handler.HttpRequestHandler;
import codesquad.servlet.handler.StaticResourceHandler;
import codesquad.webserver.http.HttpRequestParser;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector {

    public static final int PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(Connector.class);

    private final HttpRequestParser httpRequestParser = new HttpRequestParser();
    private final HttpRequestHandler httpRequestHandler = new HttpRequestHandler(
            new HandlerMapper(),
            new StaticResourceHandler(new StaticResourceReader(), new MappingMediaTypeFileExtensionResolver()));
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
                HttpProcessor httpProcessor = new HttpProcessor(httpRequestParser, httpRequestHandler, socket);
                executorService.execute(httpProcessor::process);
            } catch (IOException | RuntimeException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

}
