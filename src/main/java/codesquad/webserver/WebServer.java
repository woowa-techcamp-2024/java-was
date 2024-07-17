package codesquad.webserver;

import codesquad.webserver.annotation.Autowired;
import codesquad.webserver.annotation.Component;
import codesquad.webserver.dispatcher.DispatcherServlet;
import codesquad.webserver.filter.FilterChain;
import codesquad.webserver.httprequest.HttpRequest;
import codesquad.webserver.httpresponse.HttpResponse;
import codesquad.webserver.parser.HttpParser;
import codesquad.webserver.staticresouce.StaticResourceHandler;
import codesquad.webserver.staticresouce.StaticResourceResolver;
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

@Component
public class WebServer {

    private static final int PORT = 8080;
    private static final int POOL_SIZE = 10;

    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private final ExecutorService threadPool;
    private final DispatcherServlet dispatcherServlet;
    private final StaticResourceHandler staticResourceHandler;
    private final StaticResourceResolver staticResourceResolver;
    private final FilterChain filterChain;

    @Autowired
    public WebServer(DispatcherServlet dispatcherServlet, StaticResourceHandler staticResourceHandler,
                     StaticResourceResolver staticResourceResolver, FilterChain filterChain) {
        this.threadPool = Executors.newFixedThreadPool(POOL_SIZE);
        this.dispatcherServlet = dispatcherServlet;
        this.staticResourceHandler = staticResourceHandler;
        this.staticResourceResolver = staticResourceResolver;
        this.filterChain = filterChain;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true); // 작업 중 껐다 켰다 하는 상황에 발생하는 포트 사용중 발생을 피하기 위해 임시 작성
            logger.info("Starting web server on port {}", PORT);
            while (!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();
                logger.info("연결");
                threadPool.execute(() -> handleConnection(client));
            }
        } catch (IOException e) {
            logger.error("Error while starting web server", e);
        }
    }

    private void handleConnection(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             OutputStream outputStream = client.getOutputStream()) {

            HttpRequest request = createHttpRequest(in);
            HttpResponse response = filterChain.doFilter(request);

            if (response.getStatusCode() != 200) {
                
            } else if (staticResourceResolver.isStaticResource(request.getRequestLine().getPath())) {
                logger.debug("정적 경로 처리 중 : {}", request.getRequestLine().getPath());
                response = staticResourceHandler.handleRequest(request);
            } else {
                response = dispatcherServlet.service(request);
            }
            writeResponse(outputStream, response);
        } catch (Exception e) {
            logger.error("Error while handling request", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }

    private HttpRequest createHttpRequest(BufferedReader in) throws IOException {
        return HttpParser.parse(in);
    }

    private void writeResponse(OutputStream out, HttpResponse response) throws IOException {
        out.write(response.generateHttpResponse());
        out.flush();
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }
}
