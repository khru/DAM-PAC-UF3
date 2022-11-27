package Unit;

import client.ClientConnection;
import org.junit.jupiter.api.Test;
import shared.ConnectionDTO;
import shared.Printable;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClientConnectionShould {

    @Test
    public void handle_connection_exceptions() {
        String ip = "NOT_A_VALID_IP";
        String port = "0";
        Printable clientConsole = mock(Printable.class);
        ClientConnection connection = new ClientConnection(new ConnectionDTO(ip, port), clientConsole);
        connection.connect();
        verify(clientConsole).println(contains("Client error creating the connection"));
    }
}
