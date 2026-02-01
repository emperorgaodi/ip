package darwin.task;

import darwin.DarwinException;
import java.time.LocalDate;
import darwin.DateParser;

/**
 * Represents a task with a specific deadline. A <code>Deadline</code> object
 * extends the base Task with an additional due date that must be completed by.
 */
public class Deadline extends Task {

    private String by;
    private LocalDate date;

    /**
     * Constructs a new Deadline task with the specified description and due date.
     * The due date string is parsed into a LocalDate object.
     *
     * @param description The text description of the deadline task.
     * @param by The due date string in yyyy-mm-dd format (e.g., "2023-12-31").
     * @throws DarwinException If the date string cannot be parsed into a valid date.
     */
    public Deadline(String description, String by) throws DarwinException {
        super(description);
        this.by = by;
        this.date = DateParser.parseDate(by);
    }

    /**
     * Returns a string representation of the deadline task, including its type indicator [D],
     * status, description, and formatted due date.
     *
     * @return A string in the format "[D][status] description (by: MMM dd yyyy)".
     */
    @Override
    public String toString() {
        String displayDate = DateParser.formatDateForDisplay(date);
        // Print in MMM dd yyyy format
        return "[D]" + super.toString() + " (by: " + displayDate + ")";
    }

    /**
     * Returns a string representation of the deadline task suitable for file storage.
     * The format is: "D | status | description | yyyy-mm-dd"
     *
     * @return A string in file format with pipe-separated values.
     */
    @Override
    public String toFileFormat() {
        // Save in yyyy-mm-dd format
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + date.toString();
    }
}