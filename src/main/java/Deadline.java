import java.time.LocalDate;

public class Deadline extends Task {

    protected String by;
    protected LocalDate date;

    public Deadline(String description, String by) throws DarwinException {
        super(description);
        this.by = by;
        this.date = DateParser.parseDate(by);
    }

    @Override
    public String toString() {
        String displayDate = DateParser.formatDateForDisplay(date);
        // Print in MMM dd yyyy format
        return "[D]" + super.toString() + " (by: " + displayDate + ")";
    }

    @Override
    public String toFileFormat() {
        // Save in yyyy-mm-dd format
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + date.toString();
    }
}