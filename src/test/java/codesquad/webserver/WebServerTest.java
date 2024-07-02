package codesquad.webserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.Test;
public class WebServerTest {


    // Server starts successfully on the specified port
    @Test
    public void test_server_starts_successfully_on_specified_port() {
        // Given
        int port = 8080;
        WebServer webServer = new WebServer(port);

        // When
        Thread serverThread = new Thread(() -> webServer.start());
        serverThread.start();

        // Then
        try (Socket socket = new Socket("localhost", port)) {
            assertTrue(socket.isConnected());
        } catch (IOException e) {
            fail("Server did not start successfully on the specified port");
        } finally {
            serverThread.interrupt();
        }
    }

    // Server accepts incoming connections
    @Test
    public void test_server_accepts_incoming_connections() {
        // Given
        int port = 8080;
        WebServer webServer = new WebServer(port);

        // When
        Thread serverThread = new Thread(() -> webServer.start());
        serverThread.start();

        // Then
        try (Socket socket = new Socket("localhost", port)) {
            assertTrue(socket.isConnected());
        } catch (IOException e) {
            fail("Server did not accept incoming connections");
        } finally {
            serverThread.interrupt();
        }
    }

    // Server fails to start due to port already in use
    @Test
    public void test_server_fails_to_start_due_to_port_already_in_use() {
        // Given
        int port = 8080;
        WebServer webServer1 = new WebServer(port);
        WebServer webServer2 = new WebServer(port);

        // When
        Thread serverThread1 = new Thread(() -> webServer1.start());
        serverThread1.start();

        // Then
        try {
            Thread.sleep(1000); // Ensure the first server has started
            Thread serverThread2 = new Thread(() -> webServer2.start());
            serverThread2.start();
            Thread.sleep(1000); // Give some time for the second server to attempt to start

            assertFalse(serverThread2.isAlive());
        } catch (InterruptedException e) {
            fail("Test interrupted");
        } finally {
            serverThread1.interrupt();
        }
    }

    // Server handles invalid HTTP requests gracefully
    @Test
    public void test_server_handles_invalid_http_requests_gracefully() {
        // Given
        int port = 8080;
        WebServer webServer = new WebServer(port);

        // When
        Thread serverThread = new Thread(() -> webServer.start());
        serverThread.start();

        // Then
        try (Socket socket = new Socket("localhost", port)) {
            socket.getOutputStream().write("INVALID REQUEST".getBytes());
            socket.getOutputStream().flush();

            byte[] buffer = new byte[1024];
            int read = socket.getInputStream().read(buffer);

            String response = new String(buffer, 0, read);
            assertTrue(response.contains("400 Bad Request"));

        } catch (IOException e) {
            fail("Server did not handle invalid HTTP requests gracefully");
        } finally {
            serverThread.interrupt();
        }
    }

}

