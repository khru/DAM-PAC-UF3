package shared;

import java.io.*;
import java.net.Socket;

public class CommunicationBus implements Communication {

    private final DataOutput outputStream;
    private final DataInput inputStream;
    private final Printable console;

    public CommunicationBus(Socket socket, Printable console) throws IOException {
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.console = console;
    }


    @Override
    public void output(Messages message, String output) throws IOException {
        console.println(message.toString());
        outputStream.writeUTF(output);
    }

    @Override
    public void output(String output) throws IOException {
        console.println(output);
        outputStream.writeUTF(output);
    }

    @Override
    public String input() throws IOException {
        return inputStream.readUTF();
    }
}
