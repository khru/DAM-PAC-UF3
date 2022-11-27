package client;

import server.Task;
import shared.ConnectionMessages;
import shared.Messages;
import shared.Printable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

public class Client implements Runnable {

    private final Logger logger = Logger.getLogger(Client.class.getName());
    private final Printable console;
    private final ClientConnection connection;
    private final String username;
    private List<Task> tasks;

    private final Integer taskToDo;

    public Client(Printable console, ClientConnection connection, String username, Optional<List<Task>> tasks) {
        this.console = console;
        this.connection = connection;
        this.username = username;
        this.tasks = tasks.orElse(emptyList());
        this.taskToDo = this.tasks.isEmpty() ? 1 : this.tasks.size();
    }

    @Override
    public void run() {
        try {
            connection.connect();

            String requestUserName = connection.bus().input();
            console.println(requestUserName);

            connection.bus().output(username);

            String requestNumerOfTasks = connection.bus().input();
            console.println(requestNumerOfTasks);

            connection.bus().output(String.valueOf(taskToDo));

            askTaskIfNotProvide();
            sendTaskToServer();

            String taskSentByTheServer = connection.bus().input();
            console.println(taskSentByTheServer);

        } catch (IOException connectException) {
            logger.log(Level.SEVERE, "Client error", connectException);
        } finally {
            connection.disconnect();
            console.println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
        }
    }

    private void sendTaskToServer() throws IOException {
        for (Task task : tasks) {
            String taskUnderProcess = connection.bus().input();
            console.println(Messages.TASK_UNDER_PROCESS + " " + taskUnderProcess);

            String serverRequestDescription = connection.bus().input();
            console.println(serverRequestDescription);

            connection.bus().output(task.description());

            String serverRequestStatus = connection.bus().input();
            console.println(serverRequestStatus);
            connection.bus().output(task.state());
        }
    }

    private void askTaskIfNotProvide() {
        if (tasks.size() == 0) {
            tasks = new ArrayList<>();
            for (int i = 0; i < taskToDo; i++) {
                Scanner scanner = new Scanner(System.in);
                console.println("Cuál es la descripción de la tarea");
                String description = scanner.nextLine();

                console.println("Cuál es la estado de la tarea");
                String status = scanner.nextLine();
                tasks.add(new Task(description, status));
            }
        }
    }

}
