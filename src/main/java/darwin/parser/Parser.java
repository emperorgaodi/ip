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
        if (input.equalsIgnoreCase("bye")) {
            return new ExitCommand();
        } else if (input.equalsIgnoreCase("list")) {
            return new ListCommand();
        } else if (input.startsWith("find")) {
            return parseFindCommand(input);
        } else if (input.startsWith("mark")) {
            return parseMarkCommand(input);
        } else if (input.startsWith("unmark")) {
            return parseUnmarkCommand(input);
        } else if (input.startsWith("todo")) {
            return parseTodoCommand(input);
        } else if (input.startsWith("deadline")) {
            return parseDeadlineCommand(input);
        } else if (input.startsWith("event")) {
            return parseEventCommand(input);
        } else if (input.startsWith("delete")) {
            return parseDeleteCommand(input);
        } else {
            throw new DarwinException("Unknown command :( Please use: todo, deadline, event, list, " +
                    "mark, unmark, delete, find or bye");
        }
    }

    private static Command parseFindCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= 5) {
            throw new DarwinException("Please use this format 'find <keyword>'!");
        }

        assert input.length() > 5 : "Find command cannot have no arguments";

        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new DarwinException("Search keyword is empty! Please use this format 'find <keyword>'!");
        }

        return new FindCommand(keyword);
    }

    private static MarkCommand parseMarkCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        String[] parts = input.split("\\s+");

        assert parts.length == 2 : "Split operation should produce 2 parts";

        if (parts.length < 2) {
            throw new DarwinException("Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            assert taskNumber > 0 : "Task number should be positive, but got: " + taskNumber;

            return new MarkCommand(taskNumber, true);
        } catch (NumberFormatException e) {
            throw new DarwinException("Please provide a valid task number after 'mark'.");
        }
    }

    private static MarkCommand parseUnmarkCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        String[] parts = input.split("\\s+");

        assert parts.length == 2 : "Split operation should produce 2 parts";

        if (parts.length < 2) {
            throw new DarwinException("Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            assert taskNumber > 0 : "Task number should be positive, but got: " + taskNumber;

            return new MarkCommand(taskNumber, false);
        } catch (NumberFormatException e) {
            throw new DarwinException("Please provide a valid task number after 'mark'.");
        }
    }

    private static TodoCommand parseTodoCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= 5) {
            throw new DarwinException("Please use this format 'todo <description>'!");
        }

        assert input.length() > 5 : "Todo command cannot have no arguments";

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new DarwinException("Description is empty! Please use this format 'todo <description>'!");
        }

        return new TodoCommand(description);
    }

    private static DeadlineCommand parseDeadlineCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= 9) {
            throw new DarwinException("Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        assert input.length() > 9 : "Deadline command cannot have no arguments";

        String remaining = input.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            throw new DarwinException("Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        assert description != null : "Description cannot be null after parsing";
        assert by != null : "Date cannot be null after parsing";

        if (description.isEmpty() || by.isEmpty()) {
            throw new DarwinException("Description and/or deadline is empty. Please use this format 'deadline " +
                    "<description> /by yyyy-mm-dd'!");
        }

        return new DeadlineCommand(description, by);
    }

    private static EventCommand parseEventCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= 6) {
            throw new DarwinException("Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }

        assert input.length() > 6 : "Event command cannot have no arguments";

        String remaining = input.substring(6).trim();
        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            throw new DarwinException("Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }

        String description = parts[0].trim();
        String[] timeParts = parts[1].split(" /to ", 2);

        if (timeParts.length < 2) {
            throw new DarwinException("Please use this format: event <description> /from yyyy-mm-dd " +
                    "/to yyyy-mm-dd!");
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DarwinException("The description, start time, and/or end time is empty. Please use this " +
                    "format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        return new EventCommand(description, from, to);
    }

    private static DeleteCommand parseDeleteCommand(String input) throws DarwinException {
        assert input != null : "Input string cannot be null";

        if (input.length() <= 7) {
            throw new DarwinException("Please use this format: delete <task number>");
        }

        assert input.length() > 7 : "Delete command cannot have no arguments";

        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException("Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new DeleteCommand(taskNumber);
        } catch (NumberFormatException e) {
            throw new DarwinException("Please provide a valid task number after 'delete'.");
        }
    }
}
