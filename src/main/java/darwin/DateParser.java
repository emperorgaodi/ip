package darwin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses and formats dates for the Darwin application. A <code>DateParser</code> object
 * provides utility methods to convert between String representations and LocalDate objects,
 * handling date parsing errors with appropriate user-friendly messages.
 *
 * Input dates must be in yyyy-mm-dd format (e.g., "2024-03-15").
 * Display dates are formatted as MMM dd yyyy (e.g., "Mar 15 2024").
 */
public class DateParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Parses a date string in yyyy-mm-dd format into a LocalDate object.
     * If the string cannot be parsed, throws a DarwinException with a helpful error message.
     *
     * @param dateString The date string to parse in yyyy-mm-dd format (e.g., "2019-12-02").
     * @return The parsed LocalDate object.
     * @throws DarwinException If the date string is not in the expected yyyy-mm-dd format.
     */
    public static LocalDate parseDate(String dateString) throws DarwinException {
        assert dateString != null : "Date string cannot be null";

        try {
            LocalDate parsedDate = LocalDate.parse(dateString.trim());

            assert parsedDate != null : "Parsed date should not be null";

            return parsedDate; // uses yyyy-mm-dd
        } catch (DateTimeParseException e) {
            throw new DarwinException(" Invalid date format. Please use yyyy-mm-dd (e.g., 2019-12-02)");
        }
    }

    /**
     * Formats a LocalDate object into a human-readable display string in MMM dd yyyy format.
     * If the date is null, returns null.
     *
     * @param date The LocalDate object to format, or null.
     * @return A formatted date string in MMM dd yyyy format (e.g., "Dec 02 2019"), or null if input is null.
     */
    public static String formatDateForDisplay(LocalDate date) {
        if (date == null) {
            return null;
        }

        String formatted = date.format(DATE_FORMATTER);

        assert formatted != null : "Formatted date should not be null";
        assert !formatted.trim().isEmpty() : "Formatted date should not be empty";
        assert formatted.matches("[A-Z][a-z]{2} \\d{2} \\d{4}") :
                "Date should be in format 'MMM dd yyyy', but was: " + formatted;

        return formatted;
    }
}
