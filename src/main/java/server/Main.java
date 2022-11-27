package server;

import io.github.cdimascio.dotenv.Dotenv;
import shared.Connection;
import shared.Console;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("SERVER_PORT");
        String ip = dotenv.get("SERVER_IP");

        Server server = new ServerBuilder()
                            .setConsole(new Console())
                            .setConnection(new Connection(ip, port))
                            .createServer();

        server.connect();
        server.run();
    }
}
