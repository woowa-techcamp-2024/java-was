package codesquad.server;

import java.io.BufferedReader;
import java.io.FileReader;
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
			try (Socket clientSocket = instance.mySocket.accept();
				 OutputStream clientOutput = clientSocket.getOutputStream();
				 BufferedReader br = new BufferedReader(new FileReader("src/main/resources/static/index.html"));
			) {
				// 클라이언트 연결 로그 출력
				logger.info("Client connected");

				// HTTP Status
				clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
				// Content-Type header
				clientOutput.write("Content-Type: text/html\r\n".getBytes());
				clientOutput.write("\r\n".getBytes());
				// Response Body
				String line = null;
				while ((line = br.readLine()) != null) {
					clientOutput.write(line.getBytes());
					clientOutput.write("\r\n".getBytes());
				}

				// write flush
				clientOutput.flush();
			}
		}
	}
}
