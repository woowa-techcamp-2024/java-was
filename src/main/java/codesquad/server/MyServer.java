package codesquad.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.Main;
import codesquad.socket.MySocket;

public class MyServer {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final MyServer instance = new MyServer();

	private final int PORT = 8080;

	private MySocket mySocket;

	private MyServer() {
		try {
			mySocket = new MySocket(PORT);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public static void start() throws IOException {
		while (true) {
			try (Socket clientSocket = instance.mySocket.accept()) {
				logger.info("Client connected");

				// HTTP 응답을 생성합니다.
				OutputStream clientOutput = clientSocket.getOutputStream();
				clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
				clientOutput.write("Content-Type: text/html\r\n".getBytes());
				clientOutput.write("\r\n".getBytes());
				clientOutput.write("<h1>Hello</h1>\r\n".getBytes()); // 응답 본문으로 "Hello"를 보냅니다.
				clientOutput.flush();
			}
		}
	}
}
