package Unit;

import client.ClientConnection;
import org.junit.jupiter.api.Test;
import server.ServerConnection;
import shared.ConnectionDTO;
import shared.Printable;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ServerConnectionShould {

    @Test
    public void handle_connection_exceptions() {
        String port = "-1";
        Printable serverConsole = mock(Printable.class);
        ServerConnection connection = new ServerConnection(port, serverConsole);
        connection.connect();
        verify(serverConsole).println(contains("Server error connecting"));
    }
}
