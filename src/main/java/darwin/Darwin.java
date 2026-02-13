package darwin;

import darwin.command.Command;
import darwin.parser.Parser;
import darwin.storage.Storage;
import darwin.task.TaskList;
import darwin.ui.Ui;

/**
 * Represents the main application for Darwin task manager. A <code>Darwin</code> object handles
 * the initialization of all components, manages the main application loop, and processes user commands
 * until termination.
 */
public class Darwin {

    private static final String FILE_PATH = "./data/darwin.txt";
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a new Darwin application instance with the specified file path.
     * Initializes the user interface, storage system, and loads existing tasks from the file.
     * If loading fails, starts with an empty task list.
     *
     * @param filePath The file path where tasks are persistently stored and loaded from.
     */
    public Darwin(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath); // load tasks from file
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (DarwinException e) {
            ui.printError("Error loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    public String getGreeting() {
        StringBuilder greetingBuilder = new StringBuilder();
        ui.setResponseBuilder(greetingBuilder);
        ui.printGreeting();
        return greetingBuilder.toString().trim();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            setupResponseBuilder();
            Command command = parseCommand(input);
            executeCommand(command);
            saveTasks();

            String response = buildResponse(command);  // Use buildResponse method
            return response;
        } catch (DarwinException e) {
            return e.getMessage();
        }
    }

    // getResponse helper - START
    private void setupResponseBuilder() {
        StringBuilder responseBuilder = new StringBuilder();
        ui.clearResponse();
        ui.setResponseBuilder(responseBuilder);
    }

    private Command parseCommand(String input) throws DarwinException {
        return Parser.parse(input);
    }

    private void executeCommand(Command command) throws DarwinException {
        command.execute(tasks, ui, storage);
    }

    private void saveTasks() {
        storage.saveTasks(tasks.getTasks());
    }

    private String buildResponse(Command command) {
        String response = ui.getResponseBuilder().toString().trim();

        if (command.isExit()) {
            response += "Bye. Hope to see you again soon!\n[Darwin will now close...]";
        }

        return response;
    }
    // getResponse helper - END
}
