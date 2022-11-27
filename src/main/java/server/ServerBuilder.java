package server;

import shared.Connection;
import shared.ConnectionDTO;
import shared.Printable;

import java.util.Optional;

public class ServerBuilder {
    private Printable console;
    private ServerConnection connection;
    private Boolean testMode;

    public ServerBuilder setConsole(Printable console) {
        this.console = console;
        return this;
    }

    public ServerBuilder setConnection(ServerConnection connection) {
        this.connection = connection;
        return this;
    }

    public ServerBuilder setTestMode(Boolean testMode) {
        this.testMode = testMode;
        return this;
    }

    public Server createServer() {
        return new Server(console, connection, Optional.ofNullable(testMode));
    }
}