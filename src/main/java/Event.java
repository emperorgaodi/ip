import java.time.LocalDate;

public class Event extends Task {

    protected LocalDate fromDate;
    protected LocalDate toDate;

    public Event(String description, String from, String to) throws DarwinException {
        super(description);
        this.fromDate = DateParser.parseDate(from);
        this.toDate = DateParser.parseDate(to);
    }

    @Override
    public String toString() {
        String fromDisplay = DateParser.formatDateForDisplay(fromDate);
        String toDisplay = DateParser.formatDateForDisplay(toDate);
        return "[E]" + super.toString() + " (from: " + fromDisplay + " to: " + toDisplay + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + fromDate.toString() + " | "
                + toDate.toString();
    }
}