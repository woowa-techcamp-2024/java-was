package codesquad;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class Server {

    public static void run(ServerSocket serverSocket) throws IOException {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 200, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50));
        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.execute(new ClientRequest(clientSocket));
        }
    }

    private Server() {
    }
}
