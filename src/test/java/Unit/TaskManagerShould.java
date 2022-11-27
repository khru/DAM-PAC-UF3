package Unit;

import org.junit.jupiter.api.Test;
import server.Task;
import server.TaskManager;
import shared.Communication;
import shared.Printable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class TaskManagerShould {

    @Test
    public void create_a_task_from_an_input() throws IOException {
        Communication bus = mock(Communication.class);
        Printable console = mock(Printable.class);
        String username = "A_RANDOM_USER_FROM_A_TEST";
        TaskManager taskManager = new TaskManager(bus, console);
        List<Task> tasks = Arrays.asList(
            new Task("A_RANDOM_DESCRIPTION_1", "COMPLETED"),
            new Task("A_RANDOM_DESCRIPTION_2", "TO_DO")
        );

        when(bus.input()).thenReturn(
            username,
            String.valueOf(tasks.size()),
            tasks.get(0).description(),
            tasks.get(0).state(),
            tasks.get(1).description(),
            tasks.get(1).state()
        );

        taskManager.run();

        verify(console).println(contains(username));
        verify(console).println(contains("#{" + tasks.size() + "}"));
        verify(bus).output(tasks.toString());
    }
}
