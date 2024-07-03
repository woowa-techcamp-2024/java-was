package codesquad.webserver;


import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebClientTest {
    @Test
    public void test(){
        int NUM_OF_CLIENTS = 2;
        String serverAddress = "localhost";
        int port = 8080;
        Client client = null;

        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i < NUM_OF_CLIENTS; i++){
            executorService.submit(new Client(serverAddress, port));
        }
        executorService.shutdown();
    }
}