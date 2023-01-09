package shared;

public interface Connection {
    public void connect();
    public void disconnect();

    public Communication bus();
}
