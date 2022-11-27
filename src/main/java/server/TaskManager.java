package server;

import server.Task;
import shared.Communication;
import shared.Messages;
import shared.Printable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Communication bus;
    private final Printable console;

    public TaskManager(Communication bus, Printable console) {
        this.bus = bus;
        this.console = console;
    }


    public void run() throws IOException {

        bus.output(Messages.REQUEST_CLIENT_NAME, "Dime el nombre de tú usuario");
        String username = bus.input();
        console.println("Nombre del usuario: " + username);

        bus.output(Messages.REQUEST_NUMBER_OF_TASKS_TO_DO, "Dime el número de tareas que quieres procesar");
        String numberOfTasksToDoInput = bus.input();
        console.println("El usuario quiere hacer #{" + numberOfTasksToDoInput + "} tareas");

        int numberOfTasksToDo = Integer.parseInt(numberOfTasksToDoInput);
        List<Task> tasks = createTasks(numberOfTasksToDo);

        bus.output(tasks.toString());
    }

    private List<Task> createTasks(int numberOfTasksToDo) throws IOException {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < numberOfTasksToDo; i++) {
            String taskId = String.valueOf(i + 1);
            bus.output(Messages.RESPONSE_TASK_NUMBER, taskId);
            bus.output(Messages.REQUEST_TASK_DESCRIPTION, "Dime la descripción de la tarea");
            String taskDescription = bus.input();

            bus.output(Messages.REQUEST_TASK_STATUS, "Dime el estado de la tarea");
            String taskStatus = bus.input();

            tasks.add(new Task(taskDescription, taskStatus));
        }

        return tasks;
    }


}
