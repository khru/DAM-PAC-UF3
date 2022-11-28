package client;

import server.Task;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

public class Client implements Runnable {

    private final Logger logger = Logger.getLogger(Client.class.getName());
    private final String username;
    private final String ip;
    private final String port;
    private List<Task> tasks = emptyList();
    Socket client;
    private final int TASKS_TO_DO = 3;


    public Client(String ip, String port, String username) {
        this.ip = ip;
        this.port = port;
        this.username = username;
    }

    private void connect() throws IOException {
        client = new Socket(ip, Integer.parseInt(port));
        System.out.println("CLIENT_STARTED");
    }

    @Override
    public void run() {
        try
        {
            connect();
            DataOutputStream serverOutput = new DataOutputStream(client.getOutputStream());
            DataInputStream serverInput = new DataInputStream(client.getInputStream());

            String requestUserName = serverInput.readUTF();
            System.out.println(requestUserName);

            serverOutput.writeUTF(username);

            String requestNumerOfTasks = serverInput.readUTF();
            System.out.println(requestNumerOfTasks);

            serverOutput.writeUTF(String.valueOf(TASKS_TO_DO));

            askTaskIfNotProvide();
            sendTaskToServer(serverOutput, serverInput);

            String taskSentByTheServer = serverInput.readUTF();
            System.out.println(taskSentByTheServer);

            client.close();
            System.out.println("CLIENT_DISCONNECTED");
        }
        catch (IOException connectException) {
            logger.log(Level.SEVERE,"Client error", connectException);
        }
    }

    private void sendTaskToServer(DataOutputStream serverOutput, DataInputStream serverInput) throws IOException {
        for (Task task : tasks) {
            String taskUnderProcess = serverInput.readUTF();
            System.out.println("TASK_UNDER_PROCESS " + taskUnderProcess);

            String serverRequestDescription = serverInput.readUTF();
            System.out.println(serverRequestDescription);

            serverOutput.writeUTF(task.description());

            String serverRequestStatus = serverInput.readUTF();
            System.out.println(serverRequestStatus);
            serverOutput.writeUTF(task.state());
        }
    }

    private void askTaskIfNotProvide() {
        if (tasks.size() == 0) {
            tasks = new ArrayList<>();
            for (int i = 0; i < TASKS_TO_DO ;i++) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Cuál es la descripción de la tarea");
                String description = scanner.nextLine();

                System.out.println("Cuál es la estado de la tarea");
                String status = scanner.nextLine();
                tasks.add(new Task(description, status));
            }
        }
    }

}
