package darwin.parser;

import darwin.DarwinException;
import darwin.command.Command;
import darwin.command.DeadlineCommand;
import darwin.command.DeleteCommand;
import darwin.command.EventCommand;
import darwin.command.ExitCommand;
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
        } else if (input.startsWith("mark ")) {
            return parseMarkCommand(input);
        } else if (input.startsWith("unmark ")) {
            return parseUnmarkCommand(input);
        } else if (input.startsWith("todo ")) {
            return parseTodoCommand(input);
        } else if (input.startsWith("deadline ")) {
            return parseDeadlineCommand(input);
        } else if (input.startsWith("event ")) {
            return parseEventCommand(input);
        } else if (input.startsWith("delete ")) {
            return parseDeleteCommand(input);
        } else {
            throw new DarwinException(" Unknown darwin.command :( Please use: todo, deadline, event, list, mark, unmark, " +
                    "delete or bye");
        }
    }

    /**
     * Parses a mark command from the input string.
     *
     * @param input The input string starting with "mark " (e.g., "mark 3").
     * @return A MarkCommand object to mark the specified task as done.
     * @throws DarwinException If the task number is missing or not a valid integer.
     */
    private static MarkCommand parseMarkCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a darwin.task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new MarkCommand(taskNumber, true);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid darwin.task number after 'mark'.");
        }
    }

    /**
     * Parses an unmark command from the input string.
     *
     * @param input The input string starting with "unmark " (e.g., "unmark 3").
     * @return A MarkCommand object to mark the specified task as not done.
     * @throws DarwinException If the task number is missing or not a valid integer.
     */
    private static MarkCommand parseUnmarkCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a darwin.task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new MarkCommand(taskNumber, false);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid darwin.task number after 'mark'.");
        }
    }

    /**
     * Parses a todo command from the input string.
     *
     * @param input The input string starting with "todo " (e.g., "todo read book").
     * @return A TodoCommand object to add a new todo task.
     * @throws DarwinException If the description is missing or empty.
     */
    private static TodoCommand parseTodoCommand(String input) throws DarwinException {
        if (input.length() <= 5) {
            throw new DarwinException(" Please use this format 'todo <description>'!");
        }

        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new DarwinException(" Description is empty! Please use this format 'todo <description>'!");
        }

        return new TodoCommand(description);
    }

    /**
     * Parses a deadline command from the input string.
     *
     * @param input The input string starting with "deadline " (e.g., "deadline assignment /by 2023-12-31").
     * @return A DeadlineCommand object to add a new deadline task.
     * @throws DarwinException If the format is invalid, description is missing,
     *         or the deadline date is missing/empty.
     */
    private static DeadlineCommand parseDeadlineCommand(String input) throws DarwinException {
        if (input.length() <= 9) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        String remaining = input.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format 'deadline <description> /by yyyy-mm-dd'!");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new DarwinException(" Description and/or deadline is empty. Please use this format 'deadline " +
                    "<description> /by yyyy-mm-dd'!");
        }

        return new DeadlineCommand(description, by);
    }

    /**
     * Parses an event command from the input string.
     *
     * @param input The input string starting with "event " (e.g., "event meeting /from 2023-12-01 /to 2023-12-02").
     * @return An EventCommand object to add a new event task.
     * @throws DarwinException If the format is invalid, description is missing,
     *         or the start/end dates are missing/empty.
     */
    private static EventCommand parseEventCommand(String input) throws DarwinException {
        if (input.length() <= 6) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String remaining = input.substring(6).trim();
        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String description = parts[0].trim();
        String[] timeParts = parts[1].split(" /to ", 2);

        if (timeParts.length < 2) {
            throw new DarwinException(" Please use this format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DarwinException(" The description, start time, and/or end time is empty. Please use this " +
                    "format: event <description> /from yyyy-mm-dd /to yyyy-mm-dd!");
        }

        return new EventCommand(description, from, to);
    }

    /**
     * Parses a delete command from the input string.
     *
     * @param input The input string starting with "delete " (e.g., "delete 3").
     * @return A DeleteCommand object to remove the specified task.
     * @throws DarwinException If the task number is missing or not a valid integer.
     */
    private static DeleteCommand parseDeleteCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a darwin.task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new DeleteCommand(taskNumber);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid darwin.task number after 'delete'.");
        }
    }
}
