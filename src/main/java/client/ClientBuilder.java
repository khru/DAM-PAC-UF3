package client;

import server.Task;
import shared.ConnectionDTO;
import shared.Printable;

import java.util.List;
import java.util.Optional;

public class ClientBuilder {
    private Printable console;
    private ClientConnection connection;
    private String username;
    private List<Task> tasks;

    public ClientBuilder setConsole(Printable console) {
        this.console = console;
        return this;
    }

    public ClientBuilder setConnection(ClientConnection connection) {
        this.connection = connection;
        return this;
    }

    public ClientBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public ClientBuilder setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Client createClient() {
        return new Client(console, connection, username, Optional.ofNullable(tasks));
    }
}
