package client;

import io.github.cdimascio.dotenv.Dotenv;
import shared.ConnectionDTO;
import shared.Console;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String port = dotenv.get("SERVER_PORT");
        String ip = dotenv.get("SERVER_IP");
        String username = "A_RANDOM_USER_NAME";
        Console console = new Console();

        ClientConnection connection = new ClientConnection(new ConnectionDTO(ip, port), console);

        Client client = new ClientBuilder()
                            .setConsole(console)
                            .setConnection(connection)
                            .setUsername(username)
                            .createClient();

        client.run();
    }
}
