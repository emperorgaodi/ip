public class Darwin {
    private static final String FILE_PATH = "./data/darwin.txt";
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;


    public Darwin(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath); // load tasks from file
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (Exception e) {
            ui.printError("Error loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.printGreeting();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.printLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DarwinException e) {
                ui.printError(e.getMessage());
            } finally {
                ui.printLine();
            }
        }

        ui.printGoodbye();
        ui.close();
    }


    public static void main(String[] args) {
        new Darwin(FILE_PATH).run();
    }
}
