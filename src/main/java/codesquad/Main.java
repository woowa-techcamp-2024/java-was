package codesquad;

import database.H2Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.User;
import org.slf4j.Logger;
import repository.UserRepositoryDB;
import util.LoggerUtil;


public class Main {
    private static final Logger logger = LoggerUtil.getLogger();
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException, SQLException {

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        H2Server h2Server = new H2Server();

        try {
            h2Server.start();
            // 데이터 베이스 커낵션 테스트
            //유저 추가
            UserRepositoryDB.getInstance().addUser( new User("a", "a", "a", "a"));
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                logger.info("Server started on port " + PORT);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client  connected");

                    // 클라이언트 요청을 처리하기 위해 스레드 풀에 작업 제출
                    threadPool.submit(new ClientThread(clientSocket));
                }
            } catch (IOException e) {
                logger.error("Could not start server: {}", e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            h2Server.stop();
        }
    }

}