package server;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        String port = "9876";
        Server server = new Server(port);

        server.connect();
        server.run();
    }
}
