package Acceptance;

import client.Client;
import client.ClientBuilder;
import client.ClientConnection;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import server.Server;
import server.ServerBuilder;
import server.ServerConnection;
import server.Task;
import shared.ConnectionDTO;
import shared.ConnectionMessages;
import shared.Printable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

public class ClientServerConnectionShould {
    Dotenv dotenv;
    Server server;

    Printable serverConsole = mock(Printable.class);
    Printable clientConsole = mock(Printable.class);
    String port;

    ServerConnection connection;

    @BeforeEach
    public void setUp() {
        dotenv = Dotenv.load();
        Boolean testMode = true;
        port = dotenv.get("SERVER_PORT");
        serverConsole = mock(Printable.class);
        clientConsole = mock(Printable.class);
        ServerConnection connection = new ServerConnection(port, serverConsole);

        server = new ServerBuilder()
            .setConsole(serverConsole)
            .setConnection(connection)
            .setTestMode(testMode)
            .createServer();
        // must not block main thread
        Thread serverProcess = new Thread(server);
        serverProcess.start();
    }

    @ParameterizedTest
    @MethodSource("generateTasks")
    public void connect_send_a_username_and_a_list_of_task_and_expects_the_client_to_receive_it(List<Task> taskToDo) {
        String ip = dotenv.get("SERVER_IP");
        String username = "A_RANDOM_USER_NAME";
        ClientConnection connection = new ClientConnection(new ConnectionDTO(ip, port), clientConsole);
        String expectedOutputOfTask = taskToDo.toString();

        List<Task> task = new ArrayList<>(taskToDo);


        Client client = new ClientBuilder()
            .setConsole(clientConsole)
            .setConnection(connection)
            .setUsername(username)
            .setTasks(task)
            .createClient();


        verify(serverConsole).println(ConnectionMessages.SERVER_STARTED.toString());
        client.run();
        verify(serverConsole).println(ConnectionMessages.CLIENT_CONNECTED.toString());
        verify(serverConsole).println(contains(username));
        verify(serverConsole).println(expectedOutputOfTask);
        verify(clientConsole).println(expectedOutputOfTask);
        verify(serverConsole).println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
        verify(serverConsole).println(ConnectionMessages.SERVER_STOPPED.toString());
    }

    private static Stream<Arguments> generateTasks() {
        return Stream.of(
            arguments(
                List.of(
                    new Task("A_RANDOM_DESCRIPTION_1", "COMPLETED")
                )
            ),
            arguments(
                Arrays.asList(
                    new Task("A_RANDOM_DESCRIPTION_1", "COMPLETED"),
                    new Task("A_RANDOM_DESCRIPTION_2", "TO_DO")
                )
            ),
            arguments(
                Arrays.asList(
                    new Task("A_RANDOM_DESCRIPTION_1", "COMPLETED"),
                    new Task("A_RANDOM_DESCRIPTION_2", "TO_DO"),
                    new Task("A_RANDOM_DESCRIPTION_3", "TEST")
                )
            )
        );
    }

}
