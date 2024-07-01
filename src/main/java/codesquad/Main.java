package codesquad;

public class Main {
    private static final Server server = new Server();

    public static void main(String[] args) {
        try {
            server.setUp();
            server.receive();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
