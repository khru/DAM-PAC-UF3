package server;

import shared.Connection;
import shared.ConnectionMessages;
import shared.Messages;
import shared.Printable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server implements Runnable  {
    private final Logger logger = Logger.getLogger(Server.class.getName());
    private final boolean testMode;
    private ServerSocket server;
    private Socket socket;

    private final Printable console;
    private final Connection connection;

    public Server(Printable console, Connection connection, Optional<Boolean> testMode) {
        this.console = console;
        this.connection = connection;
        this.testMode = testMode.orElse(false);
    }

    public void connect() {
        try {
            int port = Integer.parseInt(connection.port());
            server = new ServerSocket(port);
            socket = new Socket();
        } catch (IOException connectException) {
            logger.log(Level.SEVERE,"Server error", connectException);
        }
    }

    public void disconnect() {
        if (isServerConnected()) {
            return;
        }

        try {
            server.close();
        } catch (IOException connectException) {
            logger.log(Level.SEVERE,"Server error disconnecting", connectException);
        }
    }

    @Override
    public void run() {
        try
        {
            connectServerIfNotConnected();
            console.println(ConnectionMessages.SERVER_STARTED.toString());
            do  {
                socket = server.accept();

                console.println(ConnectionMessages.CLIENT_CONNECTED.toString());

                DataOutputStream clientOutput = new DataOutputStream(socket.getOutputStream());
                DataInputStream clientInput = new DataInputStream(socket.getInputStream());

                askUsername(clientOutput);
                String username = clientInput.readUTF();
                console.println("Nombre del usuario: " + username);

                askNumberOfTaskToDo(clientOutput);
                String numberOfTasksToDoInput = clientInput.readUTF();
                console.println("El usuario quiere hacer #{" + numberOfTasksToDoInput + "} tareas");

                int numberOfTasksToDo = Integer.parseInt(numberOfTasksToDoInput);
                List<Task> tasks = createTasks(clientOutput, clientInput, numberOfTasksToDo);

                responseTaskReceived(clientOutput, tasks);

                socket.close();
                console.println(ConnectionMessages.CLIENT_DISCONNECTED.toString());
            } while (!testMode);

        }
        catch (Exception unknownException) {
            logger.log(Level.SEVERE,"Server error", unknownException);
        } finally {
            console.println(ConnectionMessages.SERVER_STOPPED.toString());
            disconnect();
        }
    }

    private void connectServerIfNotConnected() {
        if (isServerConnected()) {
            connect();
        }
    }

    private boolean isServerConnected() {
        return null == server || server.isClosed();
    }

    private void responseTaskReceived(DataOutputStream clientOutput, List<Task> tasks) throws IOException {
        console.println(tasks.toString());
        clientOutput.writeUTF(tasks.toString());
    }

    private List<Task> createTasks(DataOutputStream clientOutput, DataInputStream clientInput, int numberOfTasksToDo) throws IOException {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfTasksToDo; i++) {
            responseTaskToProcess(clientOutput, String.valueOf(i + 1));
            askForTaskDescription(clientOutput);
            String taskDescription = clientInput.readUTF();

            askForTaskStatus(clientOutput);
            String taskStatus = clientInput.readUTF();

            tasks.add(new Task(taskDescription, taskStatus));
        }

        return tasks;
    }

    private void askForTaskStatus(DataOutputStream clientOutput) throws IOException {
        output(clientOutput, Messages.REQUEST_TASK_STATUS, "Dime el estado de la tarea");
    }

    private void askForTaskDescription(DataOutputStream clientOutput) throws IOException {
        output(clientOutput, Messages.REQUEST_TASK_DESCRIPTION, "Dime la descripción de la tarea");
    }

    private void responseTaskToProcess(DataOutputStream clientOutput, String taskToBeProcess) throws IOException {
        output(clientOutput, Messages.RESPONSE_TASK_NUMBER, taskToBeProcess);
    }

    private void askUsername(DataOutputStream clientOutput) throws IOException {
        output(clientOutput, Messages.REQUEST_CLIENT_NAME, "Dime el nombre de tú usuario");
    }

    private void askNumberOfTaskToDo(DataOutputStream clientOutput) throws IOException {
        output(clientOutput, Messages.REQUEST_NUMBER_OF_TASKS_TO_DO, "Dime el número de tareas que quieres procesar");
    }

    private void output(DataOutputStream clientOutput, Messages message, String output) throws IOException {
        console.println(message.toString());
        clientOutput.writeUTF(output);
    }
}
