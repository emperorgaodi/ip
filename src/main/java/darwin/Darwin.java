package darwin;

import java.util.ArrayList;
import darwin.command.Command;
import darwin.parser.Parser;
import darwin.storage.Storage;
import darwin.task.TaskList;
import darwin.task.Task;
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
        assert filePath != null : "File path cannot be null";

        ui = new Ui();
        assert ui != null : "UI component should be initialized";

        storage = new Storage(filePath); // load tasks from file
        assert storage != null : "Storage component should be initialized";

        try {
            ArrayList<Task> loadedTasks = storage.loadTasks();
            assert loadedTasks != null : "loadTasks() should never return null";

            tasks = new TaskList(loadedTasks);

            assert tasks != null : "TaskList should be initialized";
        } catch (DarwinException e) {
            ui.printError("Error loading tasks: " + e.getMessage());
            tasks = new TaskList();

            assert tasks != null : "TaskList should be initialized even on error";
            assert tasks.getTaskCount() == 0 : "New TaskList should be empty";
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
        assert input != null : "Input cannot be null";
        assert ui != null : "UI component must be initialized";
        assert tasks != null : "TaskList must be initialized";
        assert storage != null : "Storage must be initialized";

        try {
            StringBuilder responseBuilder = new StringBuilder();
            ui.clearResponse(); // clear any previous content
            ui.setResponseBuilder(responseBuilder); // set new ResponseBuilder

            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
            storage.saveTasks(tasks.getTasks());

            String response = responseBuilder.toString().trim();

            if (c.isExit()) {
                // For GUI, you can just close the window
                response += "Bye. Hope to see you again soon!\n" +
                        "[Darwin will now close...]";
            }

            return response;

        } catch (DarwinException e) {
            return e.getMessage();
        }
    }
}
