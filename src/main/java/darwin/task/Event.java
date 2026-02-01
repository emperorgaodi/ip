package darwin.task;

import darwin.DarwinException;
import darwin.DateParser;

import java.time.LocalDate;

/**
 * Represents a task that occurs during a specific time period. An <code>Event</code> object
 * extends the base Task with both start and end dates defining the event duration.
 */
public class Event extends Task {

    private LocalDate fromDate;
    private LocalDate toDate;

    /**
     * Constructs a new Event task with the specified description, start date, and end date.
     * Both date strings are parsed into LocalDate objects.
     *
     * @param description The text description of the event.
     * @param from The start date string in yyyy-mm-dd format (e.g., "2023-12-01").
     * @param to The end date string in yyyy-mm-dd format (e.g., "2023-12-02").
     * @throws DarwinException If either date string cannot be parsed into a valid date.
     */
    public Event(String description, String from, String to) throws DarwinException {
        super(description);
        this.fromDate = DateParser.parseDate(from);
        this.toDate = DateParser.parseDate(to);
    }

    /**
     * Returns a string representation of the event task, including its type indicator [E],
     * status, description, and formatted start and end dates.
     *
     * @return A string in the format "[E][status] description (from: MMM dd yyyy to: MMM dd yyyy)".
     */
    @Override
    public String toString() {
        String fromDisplay = DateParser.formatDateForDisplay(fromDate);
        String toDisplay = DateParser.formatDateForDisplay(toDate);
        return "[E]" + super.toString() + " (from: " + fromDisplay + " to: " + toDisplay + ")";
    }

    /**
     * Returns a string representation of the event task suitable for file storage.
     * The format is: "E | status | description | yyyy-mm-dd | yyyy-mm-dd"
     *
     * @return A string in file format with pipe-separated values.
     */
    @Override
    public String toFileFormat() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + fromDate.toString() + " | "
                + toDate.toString();
    }
}