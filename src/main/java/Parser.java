public class Parser {
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
            throw new DarwinException(" Unknown command :( Please use: todo, deadline, event, list, mark, unmark, " +
                    "delete or bye");
        }
    }

    private static MarkCommand parseMarkCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new MarkCommand(taskNumber, true);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'mark'.");
        }
    }

    private static MarkCommand parseUnmarkCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new MarkCommand(taskNumber, false);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'mark'.");
        }
    }

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

    private static DeleteCommand parseDeleteCommand(String input) throws DarwinException {
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            throw new DarwinException(" Please specify a task number.");
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            return new DeleteCommand(taskNumber);
        } catch (NumberFormatException e) {
            throw new DarwinException(" Please provide a valid task number after 'delete'.");
        }
    }
}
