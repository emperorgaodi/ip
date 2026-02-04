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
//
//    /**
//     * Starts and runs the main application loop. Continuously reads user commands,
//     * parses them, executes the corresponding actions, and displays results until
//     * an exit command is received.
//     */
//    public void run() { // not used for GUI, kept for legacy usage
//        ui.printGreeting();
//
//        boolean isExit = false;
//        while (!isExit) {
//            try {
//                String fullCommand = ui.readCommand();
//                ui.printLine();
//                Command c = Parser.parse(fullCommand);
//                c.execute(tasks, ui, storage);
//                storage.saveTasks(tasks.getTasks());
//                isExit = c.isExit();
//            } catch (DarwinException e) {
//                ui.printError(e.getMessage());
//            } finally {
//                ui.printLine();
//            }
//        }
//
//        ui.printGoodbye();
//        ui.close();
//    }
//
//    /**
//     * The main entry point for the Darwin task management application.
//     * Creates a new Darwin instance and starts the application.
//     *
//     * @param args Command line arguments.
//     */
//    public static void main(String[] args) {
//        new Darwin(FILE_PATH).run();
//    }

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
            StringBuilder responseBuilder = new StringBuilder();
            ui.clearResponse(); // clear any previous content
            ui.setResponseBuilder(responseBuilder); // set new ResponseBuilder

            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
            storage.saveTasks(tasks.getTasks());

            String response = responseBuilder.toString().trim();

            if (c.isExit()) {
                // For GUI, you can just close the window
                // Or return a special message
                response += "Bye. Hope to see you again soon!\n" +
                        "[Darwin will now close...]";
            }

            return response;

        } catch (DarwinException e) {
            return e.getMessage();
        }
    }
}
