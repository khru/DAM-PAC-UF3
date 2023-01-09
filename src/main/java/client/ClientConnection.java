package client;

import shared.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection implements Connection {

    private final Logger logger = Logger.getLogger(ClientConnection.class.getName());
    private final Printable console;

    private ServerSocket server;
    private Socket client;
    private Communication bus;

    private final ConnectionDTO connection;

    public ClientConnection(ConnectionDTO connection, Printable console) {
        this.connection = connection;
        this.console = console;
    }

    @Override
    public void connect() {
        try {
            client = new Socket(connection.ip(), Integer.parseInt(connection.port()));
            bus = new CommunicationBusBuilder()
                    .setConsole(console)
                    .setSocket(client)
                    .createCommunicationBus();
        } catch (IOException connectException) {
            console.println(Level.SEVERE + ": Client error creating the connection");
            logger.log(Level.SEVERE,"Client error creating the connection", connectException);
        }
    }

    @Override
    public void disconnect() {
        if (null == client || client.isClosed()) {
            return;
        }
        try {
            client.close();
        } catch (IOException connectException) {
            logger.log(Level.SEVERE,"Client error disconnecting client", connectException);
        }
    }

    @Override
    public Communication bus() {
        return bus;
    }
}
