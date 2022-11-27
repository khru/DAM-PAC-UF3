package shared;

public class Console implements Printable {

    @Override
    public void println(String message) {
        System.out.println(message);
    }
}
