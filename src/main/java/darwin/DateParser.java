package darwin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    // custom formatter for display (MMM dd yyyy)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    // Parse yyyy-mm-dd string to LocalDate
    public static LocalDate parseDate(String dateString) throws DarwinException {
        try {
            return LocalDate.parse(dateString.trim()); // uses yyyy-mm-dd
        } catch (DateTimeParseException e) {
            throw new DarwinException(" Invalid date format. Please use yyyy-mm-dd (e.g., 2019-12-02)");
        }
    }

    // format LocalDate to MMM dd yyyy string
    public static String formatDateForDisplay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }
}
