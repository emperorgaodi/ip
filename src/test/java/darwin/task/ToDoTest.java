package darwin.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    @Test
    public void testToString() {
        // Test basic toString
        ToDo todo = new ToDo("Buy groceries");
        assertEquals("[T][ ] Buy groceries", todo.toString());

        // Test after marking done
        todo.markAsDone();
        assertEquals("[T][X] Buy groceries", todo.toString());

        // Test after marking not done again
        todo.markAsNotDone();
        assertEquals("[T][ ] Buy groceries", todo.toString());
    }

    @Test
    public void testToFileFormat() {
        // Test file format for undone task
        ToDo todo = new ToDo("Complete assignment");
        assertEquals("T | 0 | Complete assignment", todo.toFileFormat());

        // Test file format for done task
        todo.markAsDone();
        assertEquals("T | 1 | Complete assignment", todo.toFileFormat());

        // Test with special characters
        ToDo specialTodo = new ToDo("Task with | pipe");
        assertEquals("T | 0 | Task with | pipe", specialTodo.toFileFormat());
    }
}
