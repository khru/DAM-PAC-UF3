package shared;

import java.io.IOException;

public interface Communication {
    public void output(Messages message, String output) throws IOException;
    public void output(String output) throws IOException;
    public String input() throws IOException;

}
