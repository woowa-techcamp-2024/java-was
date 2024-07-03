package codesquad.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    private String serverAddress;
    private int port;
    private final Logger log = LoggerFactory.getLogger(Client.class);


    public Client(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
    }

    private void callGetRequest(Socket socket, String url) throws IOException {
        // 요청 보내기
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        String httpRequest = "GET "+ url + " HTTP/1.1\r\n" +
                "Host: " + serverAddress + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        out.println(httpRequest);
    }

    private void printResponse(Socket socket) throws IOException {
        // 요청 받기
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while((line = br.readLine()) != null){
            log.info(line);
        }
    }

    @Override
    public void run(){
        try{
            Socket socket = new Socket(serverAddress, port);
            log.info("socket: "+ socket.toString());
            callGetRequest(socket, "/index");
            printResponse(socket);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
