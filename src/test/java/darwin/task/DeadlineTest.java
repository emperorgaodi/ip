package darwin.task;

import darwin.DarwinException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineTest {
    @Test
    public void testToString() {
        // Test basic toString with date formatting
        Deadline deadline = new Deadline("Submit report", "2024-03-15");
        assertEquals("[D][ ] Submit report (by: Mar 15 2024)", deadline.toString());

        // Test after marking done
        deadline.markAsDone();
        assertEquals("[D][X] Submit report (by: Mar 15 2024)", deadline.toString());

        // Test different date
        Deadline deadline2 = new Deadline("Pay bills", "2024-12-25");
        assertEquals("[D][ ] Pay bills (by: Dec 25 2024)", deadline2.toString());
    }

    @Test
    public void testToFileFormat() {
        // Test file format preserves yyyy-mm-dd
        Deadline deadline = new Deadline("Return book", "2024-05-10");
        assertEquals("D | 0 | Return book | 2024-05-10", deadline.toFileFormat());

        // Test file format for done deadline
        deadline.markAsDone();
        assertEquals("D | 1 | Return book | 2024-05-10", deadline.toFileFormat());

        // Test invalid date throws exception
        Exception exception = assertThrows(DarwinException.class, () -> {
            new Deadline("Invalid", "15-03-2024"); // Wrong format
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

}
