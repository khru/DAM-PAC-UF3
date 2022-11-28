package client;

import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        String port = "9876";
        String ip = "localhost";
        String username = "A_RANDOM_USER_NAME";

        Client client = new Client(ip, port, username);

        client.run();
    }
}
