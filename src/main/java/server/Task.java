package server;

public record Task(String description, String state) {
    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}
