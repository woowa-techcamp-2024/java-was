package codesquad;

import java.io.IOException;

import codesquad.server.MyServer;

public class Main {

	public static void main(String[] args) throws IOException {

		MyServer server = new MyServer();
		server.start();
	}
}
