package server;

import shared.*;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements Runnable  {
    private final Logger logger = Logger.getLogger(Server.class.getName());
    private final boolean testMode;

    private final Printable console;
    private final ServerConnection connection;

    public Server(Printable console, ServerConnection connection, Optional<Boolean> testMode) {
        this.console = console;
        this.connection = connection;
        this.testMode = testMode.orElse(false);
    }

    @Override
    public void run() {
        try
        {
            console.println(ConnectionMessages.SERVER_STARTED.toString());
            do  {
                connection.connect();
                console.println(ConnectionMessages.CLIENT_CONNECTED.toString());

                TaskManager taskManager = new TaskManagerBuilder()
                                                .setConsole(console)
                                                .setBus(connection.bus())
                                                .createTaskManager();

                taskManager.run();

                connection.closeSocket();
                console.println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
            } while (!testMode);
        }
        catch (Exception unknownException) {
            logger.log(Level.SEVERE,"Server error", unknownException);
        } finally {
            console.println(ConnectionMessages.SERVER_STOPPED.toString());
            connection.disconnect();
        }
    }
}
