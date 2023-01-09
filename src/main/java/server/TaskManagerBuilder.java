package server;

import shared.Communication;
import shared.Printable;

public class TaskManagerBuilder {
    private Communication bus;
    private Printable console;

    public TaskManagerBuilder setBus(Communication bus) {
        this.bus = bus;
        return this;
    }

    public TaskManagerBuilder setConsole(Printable console) {
        this.console = console;
        return this;
    }

    public TaskManager createTaskManager() {
        return new TaskManager(bus, console);
    }
}