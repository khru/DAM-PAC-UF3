package client;

import io.github.cdimascio.dotenv.Dotenv;
import shared.Connection;
import shared.Console;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("SERVER_PORT");
        String ip = dotenv.get("SERVER_IP");
        String username = "A_RANDOM_USER_NAME";

        Client client = new ClientBuilder()
                            .setConsole(new Console())
                            .setConnection(new Connection(ip, port))
                            .setUsername(username)
                            .createClient();

        client.run();
    }
}
