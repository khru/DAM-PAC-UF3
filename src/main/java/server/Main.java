package server;

import io.github.cdimascio.dotenv.Dotenv;
import shared.ConnectionDTO;
import shared.Console;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("SERVER_PORT");
        String ip = dotenv.get("SERVER_IP");
        Console serverConsole = new Console();

        ServerConnection connection = new ServerConnection(port, serverConsole);

        Server server = new ServerBuilder()
                            .setConsole(serverConsole)
                            .setConnection(connection)
                            .createServer();

        server.run();
    }
}
