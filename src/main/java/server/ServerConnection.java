package server;

import shared.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection implements Connection {

    private final Logger logger = Logger.getLogger(ServerConnection.class.getName());
    private final Printable console;

    private ServerSocket server;
    private Socket socket;
    private Communication bus;

    private final int port;

    public ServerConnection(String port, Printable console) {
        this.port = Integer.parseInt(port);
        this.console = console;
    }

    @Override
    public void connect() {
        try {
            if (isServerDisconnected() ) {
                server = new ServerSocket(port);;
            }

            socket = new Socket();
            socket = server.accept();
            bus = new CommunicationBusBuilder()
                                    .setConsole(console)
                                    .setSocket(socket)
                                    .createCommunicationBus();
        } catch (IOException | IllegalArgumentException connectException) {
            console.println(Level.SEVERE + ": Server error connecting");
            logger.log(Level.SEVERE,"Server error connecting", connectException);
        }
    }

    @Override
    public void disconnect() {
        if (isServerDisconnected()) {
            return;
        }

        try {
            server.close();
        } catch (IOException connectException) {
            logger.log(Level.SEVERE,"Server error disconnecting", connectException);
        }
    }

    @Override
    public Communication bus() {
        return bus;
    }

    public void closeSocket() {
        if (null == socket || socket.isClosed()) {
            return;
        }
        try {
            socket.close();
        } catch (IOException connectException) {
            logger.log(Level.SEVERE,"Server error disconnecting client", connectException);
        }
    }

    private boolean isServerDisconnected() {
        return null == server || server.isClosed();
    }

}
