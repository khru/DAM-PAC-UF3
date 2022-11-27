package Integration;

import client.Client;
import client.ClientBuilder;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.ServerBuilder;
import server.Task;
import shared.Connection;
import shared.ConnectionMessages;
import shared.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClientServerConnectionShould {
    Dotenv dotenv;
    @BeforeEach
    public void setUp() {
        dotenv = Dotenv.load();
    }

    @Test
    public void connect_send_a_username_and_a_list_of_task_and_expects_the_client_to_receive_it() {
        String port = dotenv.get("SERVER_PORT");
        String ip = dotenv.get("SERVER_IP");

        Console serverConsole = mock(Console.class);
        Console clientConsole = mock(Console.class);
        Connection connection = new Connection(ip, port);
        Boolean testMode = true;
        String username = "A_RANDOM_USER_NAME";
        String expectedOutputOfTask = "[Task{description='A_RANDOM_DESCRIPTION_1', state=COMPLETED}, Task{description='A_RANDOM_DESCRIPTION_2', state=TO_DO}]";

        List<Task> task = new ArrayList<>(
            Arrays.asList(
                new Task("A_RANDOM_DESCRIPTION_1", "COMPLETED"),
                new Task("A_RANDOM_DESCRIPTION_2", "TO_DO")
            )
        );

        Server server = new ServerBuilder()
                            .setConsole(serverConsole)
                            .setConnection(connection)
                            .setTestMode(testMode)
                            .createServer();

        Client client = new ClientBuilder()
                            .setConsole(clientConsole)
                            .setConnection(connection)
                            .setUsername(username)
                            .setTasks(task)
                            .createClient();

        // must not block main thread
        Thread serverProcess = new Thread(server);
        serverProcess.start();

        verify(serverConsole).println(ConnectionMessages.SERVER_STARTED.toString());
        client.run();
        verify(serverConsole).println(ConnectionMessages.CLIENT_CONNECTED.toString());
        verify(serverConsole).println(contains(username));
        verify(serverConsole).println(expectedOutputOfTask);
        verify(clientConsole).println(expectedOutputOfTask);
        verify(serverConsole).println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
        verify(serverConsole).println(ConnectionMessages.SERVER_STOPPED.toString());
        server.disconnect();
    }

}
