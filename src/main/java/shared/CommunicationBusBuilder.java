package shared;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.Socket;

public class CommunicationBusBuilder {
    private Socket socket;
    private Printable console;

    public CommunicationBusBuilder setSocket(Socket socket) {
        this.socket = socket;
        return this;
    }


    public CommunicationBusBuilder setConsole(Printable console) {
        this.console = console;
        return this;
    }

    public CommunicationBus createCommunicationBus() throws IOException {
        return new CommunicationBus(socket, console);
    }
}