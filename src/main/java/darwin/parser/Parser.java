package darwin.parser;

import darwin.DarwinException;
import darwin.command.Command;
import darwin.command.DeadlineCommand;
import darwin.command.DeleteCommand;
import darwin.command.EventCommand;
import darwin.command.ExitCommand;
import darwin.command.FindCommand;
import darwin.command.ListCommand;
import darwin.command.MarkCommand;
import darwin.command.TodoCommand;

/**
 * Represents a parser for user input commands. A <code>Parser</code> object
 * converts raw text input from the user into executable Command objects,
 * validating the input format and parameters.
 */
public class Parser {

    // ============ COMMAND KEYWORDS ============
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";

    // ============ COMMAND LENGTH CONSTANTS ============
    private static final int TODO_COMMAND_MIN_LENGTH = 5;
    private static final int DEADLINE_COMMAND_MIN_LENGTH = 9;
    private static final int EVENT_COMMAND_MIN_LENGTH = 6;
    private static final int FIND_COMMAND_MIN_LENGTH = 5;

    // ============ DELIMITERS ============
    private static final String WHITESPACE_DELIMITER = "\\s+";
    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_FROM_DELIMITER = " /from ";
    private static final String EVENT_TO_DELIMITER = " /to ";
    private static final int SPLIT_LIMIT_TWO = 2;

    // ============ INDEX CONSTANTS ============
    private static final int COMMAND_PART_INDEX = 0;
    private static final int TASK_NUMBER_INDEX = 1;
    private static final int DESCRIPTION_PART_INDEX = 0;
    private static final int DATE_PART_INDEX = 1;
    private static final int FROM_PART_INDEX = 0;
    private static final int TO_PART_INDEX = 1;

    /**
     * Parses a raw input string from the user and returns the corresponding Command object.
     * Validates the command format and extracts necessary parameters for command construction.
     *
     * @param input The user's input string to parse (e.g., "todo read book", "deadline assignment /by 2023-12-31").
     * @return A Command object corresponding to the parsed user input.
     * @throws DarwinException If the input string is empty, contains an unknown command,
     *         or has invalid format/missing parameters.
     */
    public static Command parse(String input) throws DarwinException {
        if (input.equalsIgnoreCase(COMMAND_BYE)) {
            return new ExitCommand();
        } else if (input.equalsIgnoreCase(COMMAND_LIST)) {
            return new ListCommand();
        } else if (input.startsWith(COMMAND_FIND)) {
            return parseFindCommand(input);
        } else if (input.startsWith(COMMAND_MARK)) {
            return parseMarkCommand(input);
        } else if (input.startsWith(COMMAND_UNMARK)) {
            return parseUnmarkCommand(input);
        } else if (input.startsWith(COMMAND_TODO)) {
            return parseTodoCommand(input);
        } else if (input.startsWith(COMMAND_DEADLINE)) {
            return parseDeadlineCommand(input);
        } else if (input.startsWith(COMMAND_EVENT)) {
            return parseEventCommand(input);
        } else if (input.startsWith(COMMAND_DELETE)) {
            return parseDeleteCommand(input);
        } else {
            throw new DarwinException("Unknown command :( Please use: todo, deadline, event, list, " +
                    "mark, unmark, delete, find or bye");
        }
    }

    private static Command parseFindCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        validateFindCommandLength(input);
        String keyword = extractFindKeyword(input);
        validateFindKeyword(keyword);
        return new FindCommand(keyword);
    }

    // parseFindCommand helpers - START
    private static void validateFindCommandLength(String input) throws DarwinException {
        if (input.length() <= FIND_COMMAND_MIN_LENGTH) {
            throw new DarwinException("Please use this format 'find <keyword>'!");
        }
    }

    private static String extractFindKeyword(String input) {
        assert input.length() > 5 : "Find command cannot have no arguments";
        return input.substring(FIND_COMMAND_MIN_LENGTH).trim();
    }

    private static void validateFindKeyword(String keyword) throws DarwinException {
        if (keyword.isEmpty()) {
            throw new DarwinException("Search keyword is empty! Please use this format 'find <keyword>'!");
        }
    }
    // parseFindCommand helpers - END

    private static MarkCommand parseMarkCommand(String input) throws DarwinException {
        int taskNumber = extractTaskNumber(input);
        return new MarkCommand(taskNumber, true);
    }

    private static MarkCommand parseUnmarkCommand(String input) throws DarwinException {
        int taskNumber = extractTaskNumber(input);
        return new MarkCommand(taskNumber, false);
    }

    // parseMarkCommand & parseUnmarkCommand helpers - START
    private static int extractTaskNumber(String input) throws DarwinException {
        assert input.length() > 5 : "Find command cannot have no arguments";

        String[] parts = splitByWhitespace(input);
        validateHasTaskNumber(parts);
        return parseTaskNumber(parts, input);
    }

    private static String[] splitByWhitespace(String input) {
        return input.split(WHITESPACE_DELIMITER);
    }

    private static void validateHasTaskNumber(String[] parts) throws DarwinException {
        if (parts.length < SPLIT_LIMIT_TWO) {
            throw new DarwinException(" Please specify a task number.");
        }
    }

    private static int parseTaskNumber(String[] parts, String input) throws DarwinException {
        try {
            int taskNumber = Integer.parseInt(parts[TASK_NUMBER_INDEX]);
            assert taskNumber > 0 : "Task number should be positive, but got: " + taskNumber;
            return taskNumber;
        } catch (NumberFormatException e) {
            String command = extractCommandName(input);
            throw new DarwinException(" Please provide a valid task number after '" + command + "'.");
        }
    }

    private static String extractCommandName(String input) {
        String[] parts = splitByWhitespace(input);
        return parts[COMMAND_PART_INDEX];
    }
    // parseMarkCommand & parseUnmarkCommand helpers - END

    private static TodoCommand parseTodoCommand(String input) throws DarwinException {
        validateTodoCommandLength(input);
        String description = extractTodoDescription(input);
        validateTodoDescription(description);
        return new TodoCommand(description);
    }

    // parseTodoCommand helpers - START
    private static void validateTodoCommandLength(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= TODO_COMMAND_MIN_LENGTH) {
            throw new DarwinException(" Please use this format 'todo <description>'!");
        }
    }

    private static String extractTodoDescription(String input) {
        assert input.length() > 5 : "Todo command cannot have no arguments";

        return input.substring(TODO_COMMAND_MIN_LENGTH).trim();
    }

    private static void validateTodoDescription(String description) throws DarwinException {
        if (description.isEmpty()) {
            throw new DarwinException(" Description is empty! Please use this format 'todo <description>'!");
        }
    }
    // parseTodoCommand helpers - END

    private static DeadlineCommand parseDeadlineCommand(String input) throws DarwinException {
        validateDeadlineCommandLength(input);

        String remaining = extractDeadlineContent(input);
        String[] parts = splitDeadlineParts(remaining);
        validateDeadlineParts(parts);

        String description = extractDeadlineDescription(parts);
        String by = extractDeadlineDate(parts);
        validateDeadlineComponents(description, by);

        return new DeadlineCommand(description, by);
    }

    // parseDeadlineCommand helpers - START
    private static void validateDeadlineCommandLength(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= DEADLINE_COMMAND_MIN_LENGTH) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }
    }

    private static String extractDeadlineContent(String input) {
        assert input.length() > 9 : "Deadline command cannot have no arguments";

        return input.substring(DEADLINE_COMMAND_MIN_LENGTH).trim();
    }

    private static String[] splitDeadlineParts(String remaining) {
        return remaining.split(DEADLINE_DELIMITER, SPLIT_LIMIT_TWO);
    }

    private static void validateDeadlineParts(String[] parts) throws DarwinException {
        if (parts.length < SPLIT_LIMIT_TWO) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }
    }

    private static String extractDeadlineDescription(String[] parts) {
        assert parts[DESCRIPTION_PART_INDEX] != null : "Description cannot be null after parsing";

        return parts[DESCRIPTION_PART_INDEX].trim();
    }

    private static String extractDeadlineDate(String[] parts) {
        assert parts[DATE_PART_INDEX] != null : "Date cannot be null after parsing";

        return parts[DATE_PART_INDEX].trim();
    }

    private static void validateDeadlineComponents(String description, String by)
            throws DarwinException {
        if (description.isEmpty() || by.isEmpty()) {
            throw new DarwinException(" Description and/or deadline is empty. Please use this format 'deadline " +
                    "<description> /by yyyy-mm-dd'!");
        }
    }
    // parseDeadlineCommand helpers - END

    private static EventCommand parseEventCommand(String input) throws DarwinException {
        validateEventCommandLength(input);

        String remaining = extractEventContent(input);
        String[] fromParts = splitEventFromParts(remaining);
        validateEventFromParts(fromParts);

        String description = extractEventDescription(fromParts);
        String[] timeParts = splitEventTimeParts(fromParts);
        validateEventTimeParts(timeParts);

        String from = extractEventStartDate(timeParts);
        String to = extractEventEndDate(timeParts);
        validateEventComponents(description, from, to);

        return new EventCommand(description, from, to);
    }

    // parseEventCommand helpers - START
    private static void validateEventCommandLength(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= EVENT_COMMAND_MIN_LENGTH) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }
    }

    private static String extractEventContent(String input) {
        assert input.length() > 6 : "Event command cannot have no arguments";

        return input.substring(EVENT_COMMAND_MIN_LENGTH).trim();
    }

    private static String[] splitEventFromParts(String remaining) {
        return remaining.split(EVENT_FROM_DELIMITER, SPLIT_LIMIT_TWO);
    }

    private static void validateEventFromParts(String[] parts) throws DarwinException {
        if (parts.length < SPLIT_LIMIT_TWO) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }
    }

    private static String extractEventDescription(String[] parts) {
        return parts[DESCRIPTION_PART_INDEX].trim();
    }

    private static String[] splitEventTimeParts(String[] fromParts) {
        return fromParts[DATE_PART_INDEX].split(EVENT_TO_DELIMITER, SPLIT_LIMIT_TWO);
    }

    private static void validateEventTimeParts(String[] timeParts) throws DarwinException {
        if (timeParts.length < SPLIT_LIMIT_TWO) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }
    }

    private static String extractEventStartDate(String[] timeParts) {
        return timeParts[FROM_PART_INDEX].trim();
    }

    private static String extractEventEndDate(String[] timeParts) {
        return timeParts[TO_PART_INDEX].trim();
    }

    private static void validateEventComponents(String description, String from, String to)
            throws DarwinException {
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DarwinException(" The description, start time, and/or end time is empty. Please use this " +
                    "format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }
    }
    // parseEventCommand helpers - END

    private static DeleteCommand parseDeleteCommand(String input) throws DarwinException {
        int taskNumber = extractTaskNumber(input);
        return new DeleteCommand(taskNumber);
    }
}
