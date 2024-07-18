package codesquad.server.socket;

import codesquad.server.socket.read.MessageReader;
import codesquad.server.socket.write.MessageWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocket {
    private final Socket clientSocket;

    public ClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public byte[] read() throws IOException {
        return MessageReader.read(clientSocket.getInputStream());
    }

    public void write(byte[] message) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        MessageWriter.write(outputStream, message);
        outputStream.flush();
    }

    public void close() throws IOException {
        clientSocket.close();
    }
}
