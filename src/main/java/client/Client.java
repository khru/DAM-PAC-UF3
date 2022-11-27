package client;

import server.Task;
import shared.Connection;
import shared.ConnectionMessages;
import shared.Messages;
import shared.Printable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    private final Connection connection;
    private final String username;
    private List<Task> tasks;
    Socket client;

    private final Integer taskToDo;

    public Client(Printable console, Connection connection, String username, Optional<List<Task>> tasks) {
        this.console = console;
        this.connection = connection;
        this.username = username;
        this.tasks = tasks.orElse(emptyList());
        this.taskToDo = this.tasks.isEmpty() ? 1 : this.tasks.size();
    }

    private void connect() throws IOException {
        client = new Socket(connection.ip(), Integer.parseInt(connection.port()));
        console.println(ConnectionMessages.CLIENT_STARTED.toString());
    }

    @Override
    public void run() {
        try
        {
            connect();
            DataOutputStream serverOutput = new DataOutputStream(client.getOutputStream());
            DataInputStream serverInput = new DataInputStream(client.getInputStream());

            String requestUserName = serverInput.readUTF();
            console.println(requestUserName);

            serverOutput.writeUTF(username);

            String requestNumerOfTasks = serverInput.readUTF();
            console.println(requestNumerOfTasks);

            serverOutput.writeUTF(String.valueOf(taskToDo));

            askTaskIfNotProvide();
            sendTaskToServer(serverOutput, serverInput);

            String taskSentByTheServer = serverInput.readUTF();
            console.println(taskSentByTheServer);

            client.close();
            console.println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
        }
        catch (IOException connectException) {
            logger.log(Level.SEVERE,"Client error", connectException);
        }
    }

    private void sendTaskToServer(DataOutputStream serverOutput, DataInputStream serverInput) throws IOException {
        for (Task task : tasks) {
            String taskUnderProcess = serverInput.readUTF();
            console.println(Messages.TASK_UNDER_PROCESS + " " + taskUnderProcess);

            String serverRequestDescription = serverInput.readUTF();
            console.println(serverRequestDescription);

            serverOutput.writeUTF(task.description());

            String serverRequestStatus = serverInput.readUTF();
            console.println(serverRequestStatus);
            serverOutput.writeUTF(task.state());
        }
    }

    private void askTaskIfNotProvide() {
        if (tasks.size() == 0) {
            tasks = new ArrayList<>();
            for (int i = 0; i < taskToDo ;i++) {
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
